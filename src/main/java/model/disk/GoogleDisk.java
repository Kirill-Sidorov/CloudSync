package model.disk;

import com.google.api.services.drive.Drive;
import drive.CloudFile;
import drive.Dir;
import drive.googledrive.GoogleDir;
import drive.googledrive.GoogleFile;
import model.entity.Entity;

public class GoogleDisk implements Disk, Cloud {

    private final String name;
    private final Entity rootFile;
    private final Drive service;

    public GoogleDisk(final String name, final Entity rootFile, final Drive service) {
        this.name = name;
        this.rootFile = rootFile;
        this.service = service;
    }

    @Override
    public String name() { return name; }

    @Override
    public boolean isCloud() { return true; }

    @Override
    public Entity rootFile() { return rootFile; }

    @Override
    public Dir dir(Entity file) { return new GoogleDir(file, service); }

    @Override
    public CloudFile cloudFile(Entity fileEntity) { return new GoogleFile(fileEntity); }
}
