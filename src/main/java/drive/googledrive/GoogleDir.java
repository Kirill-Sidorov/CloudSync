package drive.googledrive;

import app.task.Progress;
import app.task.TaskState;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import drive.Dir;
import model.entity.Entity;
import model.result.*;
import model.result.Error;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoogleDir implements Dir {

    private final Entity fileEntity;
    private final Drive service;

    public GoogleDir(final Entity fileEntity, final Drive service) {
        this.fileEntity = fileEntity;
        this.service = service;
    }

    @Override
    public DirResult files(Progress progress, TaskState state) {
        ErrorResult result = new ErrorResult(Error.NO);
        List<Entity> files = new ArrayList<>();
        String pageToken = null;
        do {
            if (state.isCancel()) {
                break;
            }
            try {
                FileList fileList = service.files().list()
                        .setPageToken(pageToken)
                        .setQ(String.format("'%s' in parents", fileEntity.path()))
                        .setFields("nextPageToken, files(id, name, size, modifiedTime, mimeType, fileExtension)")
                        .execute();
                double i = 0;
                double chunk = 0;
                progress.value(0);
                int size = fileList.getFiles().size();
                System.out.println(size);
                if (size > 0) {
                    chunk = (double) 100 / size;
                }
                for (File file : fileList.getFiles()) {
                    files.add(new GoogleFileEntity(file).create());
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

    @Override
    public Entity giveOrCreateDirInto(String dirName) {
        Entity dir = null;
        try {
            FileList result = service.files().list()
                    .setQ(String.format("mimeType='application/vnd.google-apps.folder' and '%s' in parents and name = '%s'", fileEntity.path(), dirName))
                    .setFields("files(id, name, size, modifiedTime, mimeType, fileExtension)")
                    .execute();
            if (result.getFiles().size() == 0) {
                File fileMetadata = new File();
                fileMetadata.setName(dirName);
                fileMetadata.setMimeType("application/vnd.google-apps.folder");
                fileMetadata.setParents(Collections.singletonList(fileEntity.path()));
                File file = service.files().create(fileMetadata)
                        .setFields("id, name, size, modifiedTime, mimeType, fileExtension")
                        .execute();
                dir = new GoogleFileEntity(file).create();
            } else {
                dir = new GoogleFileEntity(result.getFiles().get(0)).create();
            }
        } catch (IOException e) {
            System.out.println("Google drive - dir not gave or created");
        }
        return dir;
    }
}
