package drive.remoteserver;

import app.task.Progress;
import app.task.TaskState;
import drive.Dir;
import model.result.DirResult;
import model.result.EntityResult;

public class RemoteServerDir implements Dir {
    @Override
    public DirResult getFiles(Progress progress, TaskState state) {
        return null;
    }

    @Override
    public EntityResult getDirInto(String dirName) {
        return null;
    }

    @Override
    public EntityResult searchFileInto(String fileName) {
        return null;
    }
}
