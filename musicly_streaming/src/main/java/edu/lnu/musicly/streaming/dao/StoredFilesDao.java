package edu.lnu.musicly.streaming.dao;

import edu.lnu.musicly.streaming.entities.enums.StorageType;
import edu.lnu.musicly.streaming.entities.StoredFile;

import java.util.Optional;

public interface StoredFilesDao extends GenericDao<StoredFile, String> {
    Optional<StoredFile> findFileById(String uuid, StorageType storageType);
}
