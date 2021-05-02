package drive;

import app.task.Progress;
import app.task.TaskState;
import model.result.DirResult;

public interface Dir {
    DirResult files(final Progress progress, final TaskState state);
}
