package drive;

import app.task.Progress;
import model.entity.Entity;
import model.result.Result;

public interface CloudFile {
    Result download(final Entity destFile, final Progress progress);
}
