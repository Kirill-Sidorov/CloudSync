package drive.dropbox;

import app.task.Progress;
import app.task.TaskState;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import drive.CloudDir;
import drive.Dir;
import model.entity.Entity;
import model.result.*;
import model.result.Error;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Каталог Dropbox
 */
public class DropboxDir implements Dir, CloudDir {

    /** Сущность каталога */
    private final Entity fileEntity;
    /** Объект для работы хранилищем учетной записи */
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
        return new DirResult(files, diskSize.getTotalSpace(), diskSize.getUnallocatedSpace(), result);
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
                CreateFolderResult createResult = client.files().createFolderV2(fileEntity.path() + "/" + dirName);
                result = new EntityResult(new DropboxFileEntity(createResult.getMetadata()).create(), new SuccessResult(Status.FILE_EXIST));
            } catch (DbxException e) {
                result = new EntityResult(new ErrorResult(Error.DIR_NOT_CREATED));
            }
        }

        return result;
    }

    @Override
    public EntityResult searchFileInto(String fileName) {
        EntityResult searchResult = new EntityResult(new ErrorResult(Error.FILE_NOT_FOUND_ERROR));
        try {
            boolean isSearch = true;
            ListFolderResult fileList = client.files().listFolder(fileEntity.path());
            do {
                for (Metadata metadata : fileList.getEntries()) {
                    if (metadata.getName().equals(fileName)) {
                        searchResult = new EntityResult(new DropboxFileEntity(metadata).create(), new SuccessResult(Status.FILE_EXIST));
                        isSearch = false;
                        break;
                    }
                }
                fileList = client.files().listFolderContinue(fileList.getCursor());
            } while (fileList.getHasMore() && isSearch);
        } catch (DbxException e) {
            searchResult = new EntityResult(new ErrorResult(Error.UNKNOWN));
        }
        return searchResult;
    }

    @Override
    public Result upload(Entity srcFile, Progress progress, TaskState state) {
        if (state.isCancel()) {
            return new ErrorResult(Error.FILE_NOT_UPLOAD_ERROR);
        }
        Result result;
        try (InputStream in = new FileInputStream(srcFile.path())) {
            progress.value(0);
            long size = srcFile.size();
            client.files().uploadBuilder(fileEntity.path() + "/" + srcFile.name())
                    .withMode(WriteMode.OVERWRITE).uploadAndFinish(in, l -> progress.value((int) (100 * (l / (double) size))));
            result = new SuccessResult(Status.OK);
        } catch (DbxException | IOException e) {
            result = new ErrorResult(Error.FILE_NOT_UPLOAD_ERROR);
        }
        return result;
    }
}
