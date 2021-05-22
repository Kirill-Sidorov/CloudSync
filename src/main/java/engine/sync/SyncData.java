package engine.sync;

import model.disk.Disk;
import model.entity.CompDirEntity;

/**
 * Данные для синхронизации
 */
public class SyncData {

    private final Disk disk;
    private final CompDirEntity syncDir;

    public SyncData(final Disk disk, final CompDirEntity syncDir) {
        this.disk = disk;
        this.syncDir = syncDir;
    }

    public Disk disk() { return disk; }
    public CompDirEntity syncDir() { return syncDir; }
}
