package engine.sync;

import model.disk.Disk;
import model.entity.CompDirEntity;

/**
 * Данные для синхронизации
 */
public class SyncData {
    /** Сущность хранилища данных */
    private final Disk disk;
    /** Сущность синхронизируемого каталога */
    private final CompDirEntity syncDir;

    public SyncData(final Disk disk, final CompDirEntity syncDir) {
        this.disk = disk;
        this.syncDir = syncDir;
    }

    public Disk disk() { return disk; }
    public CompDirEntity syncDir() { return syncDir; }
}
