package model.disk;

import drive.Dir;
import model.entity.Entity;

public class RemoteServerDisk implements Disk {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isCloud() {
        return false;
    }

    @Override
    public Entity getRootDir() {
        return null;
    }

    @Override
    public Dir getDir(Entity file) {
        return null;
    }
}
