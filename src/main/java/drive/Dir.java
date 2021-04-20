package drive;

import app.task.Progress;
import model.result.DirResult;

public interface Dir {
    DirResult files(final Progress progress);
}
