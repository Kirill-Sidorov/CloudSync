package model.disk;

import app.task.Progress;
import app.task.TaskState;
import com.google.api.services.drive.Drive;
import drive.CloudFileAction;
import drive.googledrive.GoogleDir;
import drive.googledrive.GoogleFileAction;
import model.entity.Entity;
import model.result.*;

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
    public DirResult files(Entity file, Progress progress, TaskState state) {
        return new GoogleDir(file, service).files(progress, state);
    }
    @Override
    public CloudFileAction actionWithFile(Entity file) { return new GoogleFileAction(file); }
}
