package drive.dropbox;

import app.task.Progress;
import app.task.TaskState;
import com.dropbox.core.v2.DbxClientV2;
import drive.CloudFile;
import model.entity.Entity;
import model.result.Result;

public class DropboxFile implements CloudFile {
    private final Entity fileEntity;
    private final DbxClientV2 client;

    public DropboxFile(final Entity fileEntity, final DbxClientV2 client) {
        this.fileEntity = fileEntity;
        this.client = client;
    }

    @Override
    public Result download(Entity destFile, Progress progress, TaskState state) {
        return null;
    }
}
