package engine.sync;

import model.disk.Disk;
import model.entity.CompDirEntity;

public class SyncData {

    private final Disk disk;
    private final CompDirEntity dir;

    public SyncData(final Disk disk, final CompDirEntity dir) {
        this.disk = disk;
        this.dir = dir;
    }

    public Disk disk() { return disk; }
    public CompDirEntity dir() { return dir; }
}
