package edu.lnu.musicly.streaming.dao.impl;

import edu.lnu.musicly.streaming.dao.StoredFilesDao;
import edu.lnu.musicly.streaming.entities.enums.StorageType;
import edu.lnu.musicly.streaming.entities.StoredFile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class StoredFilesDaoImpl extends HibernateDao<StoredFile, String> implements StoredFilesDao {
    private static final String FILE_ID = "id";
    private static final String STORAGE_TYPE = "storageType";

    public StoredFilesDaoImpl() {
        super(StoredFile.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StoredFile> findFileById(String uuid, StorageType storageType) {
        TypedQuery<StoredFile> query = session().createNamedQuery(StoredFile.STORED_FILE_QUERY, StoredFile.class);
        query.setParameter(FILE_ID, uuid);
        query.setParameter(STORAGE_TYPE, storageType);

        return query.getResultList().stream().findAny();
    }
}
