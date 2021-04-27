package model.entity;

import java.time.LocalDateTime;

public class CompFileEntity implements Entity {

    private final Entity file;
    private final boolean isNewFile;
    private final boolean isLastModified;

    public CompFileEntity(final Entity file, final boolean isNewFile, final boolean isLastModified) {
        this.file = file;
        this.isNewFile = isNewFile;
        this.isLastModified = isLastModified;
    }

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
}
