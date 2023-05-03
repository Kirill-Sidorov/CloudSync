package drive.remoteserver;

import app.task.Progress;
import app.task.TaskState;
import drive.CloudFile;
import model.entity.Entity;
import model.result.Result;

public class RemoteServerFile implements CloudFile {
    @Override
    public Result download(Entity destFile, Progress progress, TaskState state) {
        return null;
    }
}
