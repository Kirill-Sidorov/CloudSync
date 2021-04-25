package model.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class FileEntity implements Entity {
    private String path;
    private String name;
    private LocalDateTime modifiedDate;
    private Long size;
    private String typeName;
    private boolean isDirectory;

    public FileEntity(String path, String name, LocalDateTime modifiedDate, Long size, String typeName, boolean isDirectory) {
        this.path = path;
        this.name = name;
        this.modifiedDate = modifiedDate;
        this.size = size;
        this.typeName = typeName;
        this.isDirectory = isDirectory;
    }
// delete ?
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEntity that = (FileEntity) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
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
