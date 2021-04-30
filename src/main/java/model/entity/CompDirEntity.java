package model.entity;

import java.time.LocalDateTime;
import java.util.List;

public class CompDirEntity implements Entity {

    private final Entity file;
    private final List<Entity> files;

    public CompDirEntity(final Entity file, final List<Entity> files) {
        this.file = file;
        this.files = files;
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

    public List<Entity> files() { return files; }
}
