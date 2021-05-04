package drive;

import app.task.Progress;
import app.task.TaskState;
import model.entity.Entity;
import model.result.DirResult;
import model.result.EntityResult;

public interface Dir {
    DirResult files(final Progress progress, final TaskState state);
    EntityResult getDirInto(final String dirName);
    EntityResult searchFileInto(final String name, final boolean isDir);
}
