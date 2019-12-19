package edu.lnu.musicly.streaming.services;

import edu.lnu.musicly.streaming.dto.StoredFileData;
import edu.lnu.musicly.streaming.dto.StoredFileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FilesService {
    StoredFileInfo saveMusicFile(MultipartFile file);

    StoredFileData fetchMusicFile(String id);
}
