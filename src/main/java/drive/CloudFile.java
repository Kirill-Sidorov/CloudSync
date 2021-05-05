package drive;

import app.task.Progress;
import app.task.TaskState;
import model.entity.Entity;
import model.result.Result;

public interface CloudFile {
    Result download(final Entity destFile, final Progress progress, final TaskState state);
}
