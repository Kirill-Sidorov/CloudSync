package model.disk;

import drive.CloudFile;
import drive.Dir;
import model.entity.Entity;

public class RemoteServerDisk implements Disk, Cloud {
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

    @Override
    public CloudFile cloudFile(Entity file) {
        return null;
    }
}
