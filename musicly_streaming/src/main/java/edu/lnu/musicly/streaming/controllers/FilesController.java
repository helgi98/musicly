package edu.lnu.musicly.streaming.controllers;

import edu.lnu.musicly.streaming.dto.StoredFileData;
import edu.lnu.musicly.streaming.dto.StoredFileInfo;
import edu.lnu.musicly.streaming.services.FilesService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@Log4j
public class FilesController {

    private final FilesService filesService;

    @Autowired
    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }


    @PostMapping("/files/music/add")
    public StoredFileInfo addMusicFile(@RequestPart("file") MultipartFile file) {
        return filesService.saveMusicFile(file);
    }

    @GetMapping("/files/music/download/{id}")
    public void downloadFile(@PathVariable String id, HttpServletResponse response) {
        try {
            StoredFileData contentData = filesService.fetchMusicFile(id);

            response.setContentType("audio/mpeg");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-length", Long.toString(contentData.getLength()));
            response.setHeader("Content-Disposition", String.format("inline; filename=\"%s\"", contentData.getFileName()));

            IOUtils.copy(new ByteArrayInputStream(contentData.getContent()), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            log.info("Error writing file to output stream.", ex);
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
}
