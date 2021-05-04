package drive;

import app.task.Progress;
import model.entity.Entity;
import model.result.Result;

public interface CloudDir {
    Result upload(final Entity srcFile, final Progress progress);
}
