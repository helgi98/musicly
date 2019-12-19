package edu.lnu.musicly.streaming.services.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import edu.lnu.musicly.streaming.dao.StoredFilesDao;
import edu.lnu.musicly.streaming.dto.StoredFileData;
import edu.lnu.musicly.streaming.dto.StoredFileInfo;
import edu.lnu.musicly.streaming.entities.StoredFile;
import edu.lnu.musicly.streaming.entities.enums.StorageType;
import edu.lnu.musicly.streaming.exceptions.ServiceException;
import edu.lnu.musicly.streaming.exceptions.ValidationException;
import edu.lnu.musicly.streaming.services.FilesService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class AwsFilesService implements FilesService {
    public static final Regions S3_REGION = Regions.EU_CENTRAL_1;

    private final Environment env;

    private final StoredFilesDao storedFilesDao;
    private final AmazonS3 s3Client;

    private final String bucketName;

    @Autowired
    public AwsFilesService(Environment env, StoredFilesDao storedFilesDao) {
        this.env = env;
        this.bucketName = env.getProperty("aws.bucket.name");

        this.storedFilesDao = storedFilesDao;

        // This code expects that you have AWS credentials set up per:
        // https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(S3_REGION)
                .build();
    }

    @Cacheable("musicfiles")
    public StoredFileData fetchMusicFile(String id) {
        return storedFilesDao.findFileById(id, StorageType.AWS_S3).map(fileInfo -> {
            S3Object object = s3Client.getObject(bucketName, id);
            long contentLength = object.getObjectMetadata().getContentLength();
            InputStream contentStream = object.getObjectContent();

            try {
                return new StoredFileData(IOUtils.toByteArray(contentStream), contentLength, fileInfo.getFileName());
            } catch (IOException e) {
                throw new ServiceException("Failed to read music file");
            }
        })
                .orElseThrow(() -> new ValidationException("Couldn't find requested file. Please check."));
    }

    @Transactional
    @Override
    public StoredFileInfo saveMusicFile(MultipartFile file) {
        try {
            String fileObjKeyName = UUID.randomUUID().toString();

            StoredFile storedFile = new StoredFile();
            storedFile.setUuid(fileObjKeyName);
            storedFile.setFileName(file.getName());
            storedFile.setStorageType(StorageType.AWS_S3);

            storedFilesDao.persist(storedFile);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            PutObjectRequest request = new PutObjectRequest(bucketName, fileObjKeyName, file.getInputStream(), metadata);

            s3Client.putObject(request);

            return new StoredFileInfo(fileObjKeyName);
        } catch (SdkClientException | IOException e) {
            throw new ServiceException("Failed to save file to S3");
        }
    }
}
