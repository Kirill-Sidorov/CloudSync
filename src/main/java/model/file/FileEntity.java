package model.file;

import java.time.LocalDateTime;
import java.util.Objects;

public class FileEntity {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEntity that = (FileEntity) o;
        return path.equals(that.path) &&
                modifiedDate.equals(that.modifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, modifiedDate);
    }

    public String getPath() { return path; }

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

    public boolean isDirectory() { return isDirectory; }

}
