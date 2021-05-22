package model.disk;

import com.google.api.services.drive.Drive;
import drive.CloudFile;
import drive.Dir;
import drive.google.GoogleDir;
import drive.google.GoogleFile;
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
    public String getName() { return name; }

    @Override
    public boolean isCloud() { return true; }

    @Override
    public Entity getRootDir() { return rootFile; }

    @Override
    public Dir getDir(Entity file) { return new GoogleDir(file, service); }

    @Override
    public CloudFile cloudFile(Entity file) { return new GoogleFile(file, service); }
}
