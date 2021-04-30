package model.entity;

import java.time.LocalDateTime;

public class CompFileEntity implements Entity {

    private final Entity file;
    private final boolean isNewFile;
    private boolean isLastModified;
    private CompFileEntity linkedFile;

    public CompFileEntity(final Entity file) {
        this.file = file;
        this.isNewFile = true;
        this.isLastModified = true;
    }

    public CompFileEntity(final Entity file, final boolean isLastModified) {
        this.file = file;
        this.isNewFile = false;
        this.isLastModified = isLastModified;
    }
    @Override
    public String toString() { return file.name(); }

    @Override
    public String path() { return file.path(); }

    @Override
    public String name() { return file.name(); }

    @Override
    public LocalDateTime modifiedDate() { return file.modifiedDate(); }

    @Override
    public Long size() { return file.size(); }

    @Override
    public String typeName() { return file.typeName(); }

    @Override
    public boolean isDirectory() { return file.isDirectory(); }

    public boolean isNewFile() { return isNewFile; }
    public boolean isLastModified() { return isLastModified; }
    public void switchLastModified() {
        isLastModified = !isLastModified;
    }
    public void setLinkedFile(CompFileEntity linkedFile) { this.linkedFile = linkedFile; }
    public CompFileEntity linkedFile() { return linkedFile; }
}
