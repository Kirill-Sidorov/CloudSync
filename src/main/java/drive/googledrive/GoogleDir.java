package drive.googledrive;

import app.task.Progress;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import drive.Dir;
import model.entity.Entity;
import model.result.*;
import model.result.Error;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleDir implements Dir {

    private final Entity fileEntity;
    private final Drive service;

    public GoogleDir(final Entity fileEntity, final Drive service) {
        this.fileEntity = fileEntity;
        this.service = service;
    }

    @Override
    public DirResult files(Progress progress) {
        ErrorResult result = new ErrorResult(Error.NO);
        List<Entity> files = new ArrayList<>();
        String pageToken = null;
        do {
            try {
                FileList fileList = service.files().list()
                        .setQ(String.format("'%s' in parents", fileEntity.path()))
                        .setFields("nextPageToken, files(id, name, size, modifiedTime, mimeType, fileExtension)")
                        .execute();
                double i = 0;
                double chunk = 0;
                progress.value(0);
                int size = fileList.getFiles().size();
                if (size > 0) {
                    chunk = (double) 100 / size;
                }
                for (File file : fileList.getFiles()) {
                    files.add(new GoogleFileData(file).create());
                    i += chunk;
                    progress.value((int)i);
                }
                pageToken = fileList.getNextPageToken();
            } catch (IOException e) {
                result = new ErrorResult(Error.FAILED_GET_DIRECTORY_FILES);
            }
        } while (pageToken != null);
        GoogleDiskSize diskSize = new GoogleDiskSize(service);
        diskSize.request();
        return new DirResult(files, diskSize.totalSpace(), diskSize.unallocatedSpace(), result);
    }
}
