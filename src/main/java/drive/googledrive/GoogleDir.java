package drive.googledrive;

import app.task.Progress;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import drive.Dir;
import model.entity.Entity;
import model.entity.FileEntity;
import model.result.*;
import model.result.Error;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class GoogleDir implements Dir {

    private final String path;
    private final Drive service;

    public GoogleDir(final String path, final Drive service) {
        this.path = path;
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
                        .setQ(String.format("'%s' in parents", path))
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
                    files.add(getFileEntity(file));
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

    private FileEntity getFileEntity(final File file) {
        String id;
        String name;
        Long size;
        String typeName;
        boolean isDirectory = false;
        Instant instant = Instant.ofEpochMilli(file.getModifiedTime().getValue());
        LocalDateTime modifiedDate = LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault());

        id = (file.getId() != null) ? file.getId() : "";
        name = (file.getName() != null) ? file.getName() : "";
        size = (file.getSize() != null) ? file.getSize() : null;
        typeName = (file.getFileExtension() != null) ? file.getFileExtension() : "";

        if (file.getMimeType().equals("application/vnd.google-apps.folder")) {
            typeName = "dir";
            isDirectory = true;
        }
        return new FileEntity(id, name, modifiedDate, size, typeName, isDirectory);
    }
}
