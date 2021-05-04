package drive.googledrive;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import drive.CloudFile;
import model.entity.Entity;
import model.result.*;
import model.result.Error;
import org.apache.tika.Tika;

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
    public Result upload(Entity destFile) {
        Result result;
        File fileMetadata = new File();
        fileMetadata.setName(destFile.name());
        try {
            System.out.println(fileEntity.path());
            System.out.println(destFile.path());
            java.io.File file = new java.io.File(destFile.path());
            FileContent mediaContent = new FileContent(new Tika().detect(file), file);
            service.files().create(fileMetadata, mediaContent).execute();
            result = new SuccessResult(Status.OK);
        } catch (IOException e) {
            e.printStackTrace();
            result = new ErrorResult(Error.FILE_NOT_UPLOAD_ERROR);
        }
        return result;
    }

    @Override
    public Result download(Entity destFile) {
        Result result;
        try (FileOutputStream outputStream = new FileOutputStream(destFile.path() + "\\" + fileEntity.name())) {
            Drive.Files.Get request = service.files().get(fileEntity.path());
            request.executeMediaAndDownloadTo(outputStream);
            result = new SuccessResult(Status.OK);
        } catch (IOException e) {
            result = new ErrorResult(Error.FILE_NOT_UPLOAD_ERROR);
        }
        return result;
    }
}
