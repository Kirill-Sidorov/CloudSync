package engine.comp;

import model.disk.Disk;
import model.entity.Entity;

/**
 * Данные для сравнения
 */
public class CompData {

    private final Disk disk;
    private final Entity fileEntity;

    public CompData(final Disk disk, final Entity fileEntity) {
        this.disk = disk;
        this.fileEntity = fileEntity;
    }

    public Disk disk() { return disk; }
    public Entity fileEntity() { return fileEntity; }
}
