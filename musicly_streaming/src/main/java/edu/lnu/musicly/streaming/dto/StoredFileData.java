package edu.lnu.musicly.streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoredFileData {
    private byte[] content;
    private long length;
    private String fileName;
}
