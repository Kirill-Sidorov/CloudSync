package model.entity;

import java.time.LocalDateTime;

public class FileEntity {
    private String id;
    private String name;
    private LocalDateTime modifiedDate;
    private Long size;
    private String typeName;

    public FileEntity(String id, String name, LocalDateTime modifiedDate, Long size, String typeName) {
        this.id = id;
        this.name = name;
        this.modifiedDate = modifiedDate;
        this.size = size;
        this.typeName = typeName;
    }

    public String getId() { return id; }

    public String getName() {
        return name;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public Long getSize() {
        return size;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isDirectory() { return false; }
}
