package model.disk;

import app.task.Progress;
import com.google.api.services.drive.Drive;
import drive.googledrive.GoogleDir;
import model.entity.Entity;
import model.result.*;

public class GoogleDisk implements Disk {

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
    public Entity rootFile() { return rootFile; }

    @Override
    public DirResult files(Entity file, Progress progress) {
        return new GoogleDir(file, service).files(progress);
    }

    @Override
    public Result execute(Entity file) {
        return new SuccessResult(Status.OK);
    }
}
