package model.disk;

import app.task.Progress;
import drive.local.LocalDir;
import drive.local.LocalLocalFile;
import model.entity.Entity;
import model.result.DirResult;
import model.result.Result;

public class LocalDisk implements Disk {

    private final Entity rootFile;

    public LocalDisk(final Entity rootFile) {
        this.rootFile = rootFile;
    }

    @Override
    public String name() { return rootFile.path().replace("\\", ""); }

    @Override
    public Entity rootFile() { return rootFile; }

    @Override
    public DirResult files(Entity file, Progress progress) {
        return new LocalDir(file).files(progress);
    }

    @Override
    public Result execute(Entity file) {
        return new LocalLocalFile(file).execute();
    }
}
