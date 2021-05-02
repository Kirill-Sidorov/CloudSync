package model.disk;

import app.task.Progress;
import app.task.TaskState;
import model.entity.Entity;
import model.result.DirResult;

public interface Disk {
    String name();
    boolean isCloud();
    Entity rootFile();
    DirResult files(final Entity file, final Progress progress, final TaskState state);
}
