package edu.lnu.musicly.streaming.entities;

import edu.lnu.musicly.streaming.entities.enums.StorageType;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@NamedQueries({
        @NamedQuery(name = StoredFile.STORED_FILE_QUERY, query = "SELECT sf FROM StoredFile sf WHERE sf.uuid = :id " +
                " AND sf.storageType = :storageType")
})
public class StoredFile {
    public static final String STORED_FILE_QUERY = "storedFileQuery";

    @Id
    private String uuid;
    private String fileName;
    @Enumerated(value = EnumType.STRING)
    private StorageType storageType;
}
