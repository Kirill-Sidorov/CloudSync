package drive.dropbox;

import app.task.Progress;
import app.task.TaskState;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import drive.CloudFile;
import model.entity.Entity;
import model.result.*;
import model.result.Error;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Файл хранилища Dropbox
 */
public class DropboxFile implements CloudFile {
    /** Сущность файла */
    private final Entity fileEntity;
    /** Объект для работы хранилищем учетной записи */
    private final DbxClientV2 client;

    public DropboxFile(final Entity fileEntity, final DbxClientV2 client) {
        this.fileEntity = fileEntity;
        this.client = client;
    }

    @Override
    public Result download(Entity destFile, Progress progress, TaskState state) {
        if (state.isCancel()) {
            return new ErrorResult(Error.FILE_NOT_DOWNLOAD_ERROR);
        }
        Result result;
        try (FileOutputStream outputStream = new FileOutputStream(destFile.path() + "\\" + fileEntity.name())) {
            progress.value(0);
            long size = fileEntity.size();
            client.files().download(fileEntity.path()).download(outputStream, l -> progress.value((int) (100 * (l / (double) size))));
            result = new SuccessResult(Status.OK);
        } catch (DbxException | IOException e) {
            result = new ErrorResult(Error.FILE_NOT_DOWNLOAD_ERROR);
        }
        return result;
    }
}
