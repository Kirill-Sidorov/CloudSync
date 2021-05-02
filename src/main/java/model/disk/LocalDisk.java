package model.disk;

import drive.Dir;
import drive.local.LocalDir;
import model.entity.Entity;

public class LocalDisk implements Disk {

    private final Entity rootFile;

    public LocalDisk(final Entity rootFile) {
        this.rootFile = rootFile;
    }

    @Override
    public String name() { return rootFile.path().replace("\\", ""); }

    @Override
    public boolean isCloud() { return false; }

    @Override
    public Entity rootFile() { return rootFile; }

    @Override
    public Dir dir(Entity file) { return new LocalDir(file); }
}
