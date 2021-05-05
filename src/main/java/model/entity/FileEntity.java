package model.entity;

import java.time.LocalDateTime;

public class FileEntity implements Entity {
    private final String path;
    private final String name;
    private final LocalDateTime modifiedDate;
    private final Long size;
    private final String typeName;
    private final boolean isDirectory;

    public FileEntity(final String path, final String name, final LocalDateTime modifiedDate, final Long size, final String typeName, final boolean isDirectory) {
        this.path = path;
        this.name = name;
        this.modifiedDate = modifiedDate;
        this.size = size;
        this.typeName = typeName;
        this.isDirectory = isDirectory;
    }

    @Override
    public String path() { return path; }

    @Override
    public String name() {
        return name;
    }

    @Override
    public LocalDateTime modifiedDate() {
        return modifiedDate;
    }

    @Override
    public Long size() { return size; }

    @Override
    public String typeName() {
        return typeName;
    }

    @Override
    public boolean isDirectory() { return isDirectory; }

}
