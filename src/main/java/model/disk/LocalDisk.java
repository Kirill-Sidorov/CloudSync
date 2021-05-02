package model.disk;

import app.task.Progress;
import app.task.TaskState;
import drive.local.LocalDir;
import drive.local.LocalFileAction;
import model.entity.Entity;
import model.result.DirResult;

public class LocalDisk implements Disk, Local {

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
    public DirResult files(Entity file, Progress progress, TaskState state) {
        return new LocalDir(file).files(progress, state);
    }

    @Override
    public LocalFileAction actionWithFile(Entity file) { return new LocalFileAction(file); }
}
