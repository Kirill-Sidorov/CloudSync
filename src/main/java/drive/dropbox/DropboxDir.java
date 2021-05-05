package drive.dropbox;

import app.task.Progress;
import app.task.TaskState;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import drive.CloudDir;
import drive.Dir;
import model.entity.Entity;
import model.result.DirResult;
import model.result.EntityResult;
import model.result.Error;
import model.result.ErrorResult;
import model.result.Result;

import java.util.ArrayList;
import java.util.List;

public class DropboxDir implements Dir, CloudDir {

    private final Entity fileEntity;
    private final DbxClientV2 client;

    public DropboxDir(final Entity fileEntity, final DbxClientV2 client) {
        this.fileEntity = fileEntity;
        this.client = client;
    }

    @Override
    public DirResult getFiles(Progress progress, TaskState state) {
        ErrorResult result = new ErrorResult(model.result.Error.NO);
        List<Entity> files = new ArrayList<>();
        progress.value(0);
        try {
            ListFolderResult fileList = client.files().listFolder(fileEntity.path());
            do {
                double i = 0;
                double chunk = 0;
                progress.value(0);
                int size = fileList.getEntries().size();
                if (size > 0) {
                    chunk = (double) 100 / size;
                }
                for (Metadata metadata : fileList.getEntries()) {
                    files.add(new DropboxFileEntity(metadata).create());
                    i += chunk;
                    progress.value((int)i);
                }
                fileList = client.files().listFolderContinue(fileList.getCursor());
            } while (fileList.getHasMore());

        } catch (DbxException e) {
            result = new ErrorResult(Error.FAILED_GET_DIRECTORY_FILES);
        }
        DropboxDiskSize diskSize = new DropboxDiskSize(client);
        diskSize.request();
        return new DirResult(files, diskSize.totalSpace(), diskSize.unallocatedSpace(), result);
    }

    @Override
    public EntityResult getDirInto(String dirName) {
        return null;
    }

    @Override
    public EntityResult searchFileInto(String fileName) {
        return null;
    }

    @Override
    public Result upload(Entity srcFile, Progress progress, TaskState state) {
        return null;
    }
}
