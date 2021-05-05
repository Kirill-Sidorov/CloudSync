package drive.googledrive;

import app.task.Progress;
import app.task.TaskState;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import drive.CloudDir;
import drive.Dir;
import model.entity.Entity;
import model.result.*;
import model.result.Error;
import org.apache.tika.Tika;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoogleDir implements Dir, CloudDir {

    private final Entity fileEntity;
    private final Drive service;

    public GoogleDir(final Entity fileEntity, final Drive service) {
        this.fileEntity = fileEntity;
        this.service = service;
    }

    @Override
    public DirResult getFiles(Progress progress, TaskState state) {
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
    public EntityResult getDirInto(String dirName) {
        boolean isNeedCreate = true;
        EntityResult searchResult = searchFileInto(dirName);
        EntityResult result = searchResult;
        if (searchResult.status() == Status.FILE_EXIST) {
            if (searchResult.entity().isDirectory()) {
                isNeedCreate = false;
            }
        }

        if (isNeedCreate) {
            try {
                File fileMetadata = new File();
                fileMetadata.setName(dirName);
                fileMetadata.setMimeType("application/vnd.google-apps.folder");
                fileMetadata.setParents(Collections.singletonList(fileEntity.path()));
                File file = service.files().create(fileMetadata)
                        .setFields("id, name, size, modifiedTime, mimeType, fileExtension")
                        .execute();
                result = new EntityResult(new GoogleFileEntity(file).create(), new SuccessResult(Status.FILE_EXIST));
            } catch (IOException e) {
                result = new EntityResult(new ErrorResult(Error.DIR_NOT_CREATED));
            }
        }
        return result;
    }

    @Override
    public EntityResult searchFileInto(String fileName) {
        EntityResult searchResult;
        try {
            FileList result = service.files().list()
                    .setQ(String.format("'%s' in parents and name = '%s'", fileEntity.path(), fileName))
                    .setFields("files(id, name, size, modifiedTime, mimeType, fileExtension)")
                    .execute();
            if (result.getFiles().size() == 0) {
                searchResult = new EntityResult(new ErrorResult(Error.FILE_NOT_FOUND_ERROR));
            } else {
                searchResult = new EntityResult(
                        new GoogleFileEntity(result.getFiles().get(0)).create(),
                        new SuccessResult(Status.FILE_EXIST)
                );
            }
        } catch (IOException e) {
            searchResult = new EntityResult(new ErrorResult(Error.UNKNOWN));
        }
        return searchResult;
    }

    @Override
    public Result upload(Entity srcFile, Progress progress, TaskState state) {
        Result result;
        progress.value(0);
        if (state.isCancel()) {
            return new ErrorResult(Error.FILE_NOT_UPLOAD_ERROR);
        }
        EntityResult searchResult = searchFileInto(srcFile.name());
        if (searchResult.status() == Status.FILE_EXIST) {
            try {
                File fileMetadata = new File();
                java.io.File file = new java.io.File(srcFile.path());
                FileContent mediaContent = new FileContent(new Tika().detect(file), file);
                Drive.Files.Update update = service.files().update(searchResult.entity().path(), fileMetadata, mediaContent);
                update.getMediaHttpUploader().setProgressListener(downloader -> progress.value((int)(downloader.getProgress() * 100)));
                update.execute();
                result = new SuccessResult(Status.OK);
            } catch (IOException e) {
                e.printStackTrace();
                result = new ErrorResult(Error.FILE_NOT_UPLOAD_ERROR);
            }
        } else {
            File fileMetadata = new File();
            fileMetadata.setName(srcFile.name());
            fileMetadata.setParents(Collections.singletonList(fileEntity.path()));
            try {
                java.io.File file = new java.io.File(srcFile.path());
                FileContent mediaContent = new FileContent(new Tika().detect(file), file);
                Drive.Files.Create create = service.files().create(fileMetadata, mediaContent);
                create.getMediaHttpUploader().setProgressListener(downloader -> progress.value((int)(downloader.getProgress() * 100)));
                create.execute();
                result = new SuccessResult(Status.OK);
            } catch (IOException e) {
                e.printStackTrace();
                result = new ErrorResult(Error.FILE_NOT_UPLOAD_ERROR);
            }
        }
        return result;
    }
}
