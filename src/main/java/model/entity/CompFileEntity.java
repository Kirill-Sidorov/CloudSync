package model.entity;

import java.time.LocalDateTime;

public class CompFileEntity implements Entity {

    private final Entity file;
    private final CompFileEntity linkedFile;
    private boolean isLastModified;

    public CompFileEntity(final Entity file) {
        this.file = file;
        this.isLastModified = true;
        this.linkedFile = null;
    }

    public CompFileEntity(final Entity file, final boolean isLastModified, final CompFileEntity linkedFile) {
        this.file = file;
        this.isLastModified = isLastModified;
        this.linkedFile = linkedFile;
    }

    public CompFileEntity(final Entity file, final boolean isLastModified, final Entity rightFile) {
        this.file = file;
        this.isLastModified = isLastModified;
        this.linkedFile = new CompFileEntity(rightFile, !isLastModified, this);
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

    public boolean isNewFile() { return linkedFile == null; }
    public boolean isLastModified() { return isLastModified; }
    public void switchLastModified() {
        isLastModified = !isLastModified;
    }
    public CompFileEntity linkedFile() { return linkedFile; }
}
