package drive.google;

import app.task.Progress;
import app.task.TaskState;
import com.google.api.services.drive.Drive;
import drive.CloudFile;
import model.entity.Entity;
import model.result.*;
import model.result.Error;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Файл хранилища Google
 */
public class GoogleFile implements CloudFile {
    /** Сущность файла */
    private final Entity fileEntity;
    /** Объект для работы хранилищем учетной записи */
    private final Drive service;

    public GoogleFile(final Entity fileEntity, final Drive service) {
        this.fileEntity = fileEntity;
        this.service = service;
    }

    @Override
    public Result download(Entity destFile, Progress progress, TaskState state) {
        if (state.isCancel()) {
            return new ErrorResult(Error.FILE_NOT_DOWNLOAD_ERROR);
        }
        Result result;
        try (FileOutputStream outputStream = new FileOutputStream(destFile.path() + "\\" + fileEntity.name())) {
            Drive.Files.Get request = service.files().get(fileEntity.path());
            request.getMediaHttpDownloader().setProgressListener(downloader -> progress.value((int)(downloader.getProgress() * 100)));
            request.executeMediaAndDownloadTo(outputStream);
            result = new SuccessResult(Status.OK);
        } catch (IOException e) {
            e.printStackTrace();
            result = new ErrorResult(Error.FILE_NOT_DOWNLOAD_ERROR);
        }
        return result;
    }
}
