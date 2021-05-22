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
    public String getName() { return rootFile.path().replace("\\", ""); }

    @Override
    public boolean isCloud() { return false; }

    @Override
    public Entity getRootDir() { return rootFile; }

    @Override
    public Dir getDir(Entity file) { return new LocalDir(file); }
}
