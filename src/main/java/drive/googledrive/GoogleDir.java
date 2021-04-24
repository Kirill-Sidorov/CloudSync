package drive.googledrive;

import app.task.Progress;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.About;
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
                int size = fileList.getFiles().size();
                int chunk = size > 100 ? size / 100 : 100 / size;
                int i = 0;
                for (File file : fileList.getFiles()) {
                    files.add(getFileEntity(file));
                    progress.increase(i += chunk);
                }
                pageToken = fileList.getNextPageToken();
            } catch (IOException e) {
                result = new ErrorResult(Error.FAILED_GET_DIRECTORY_FILES);
            }
        } while (pageToken != null);
        long[] sizeInfo = getDriveSizeInfo();
        return new DirResult(files, sizeInfo[0], sizeInfo[1], result);
    }

    public long[] getDriveSizeInfo() {
        long totalSpace = 0L;
        long unallocatedSpace = 0L;
        try {
            About about = service.about().get().setFields("storageQuota(limit, usageInDrive)").execute();
            totalSpace = (about.getStorageQuota().getLimit() != null) ? about.getStorageQuota().getLimit() : 0L;
            unallocatedSpace = (about.getStorageQuota().getUsageInDrive() != null) ? totalSpace - about.getStorageQuota().getUsageInDrive() : 0L;
        } catch (IOException e) {
            totalSpace = 0L;
            unallocatedSpace = 0L;
        }
        return new long[]{totalSpace, unallocatedSpace};
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
