package model.entity;

import java.time.LocalDateTime;

public class ComparableEntity implements Entity {

    private final FileEntity file;

    public ComparableEntity(final FileEntity file) {
        this.file = file;
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
}
