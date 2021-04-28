package model.disk;

import app.task.Progress;
import model.entity.Entity;
import model.result.DirResult;
import model.result.Result;

public interface Disk {
    String name();
    Entity rootFile();
    DirResult files(final Entity file, final Progress progress);
    Result execute(final Entity file);
}
