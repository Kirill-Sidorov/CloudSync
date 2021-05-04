package drive.googledrive;

import app.task.Progress;
import com.google.api.services.drive.Drive;
import drive.CloudFile;
import model.entity.Entity;
import model.result.*;
import model.result.Error;

import java.io.FileOutputStream;
import java.io.IOException;

public class GoogleFile implements CloudFile {
    private final Entity fileEntity;
    private final Drive service;

    public GoogleFile(final Entity fileEntity, final Drive service) {
        this.fileEntity = fileEntity;
        this.service = service;
    }

    @Override
    public Result download(Entity destFile, Progress progress) {
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
