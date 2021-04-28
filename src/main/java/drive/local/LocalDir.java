package drive.local;

import app.task.Progress;
import drive.Dir;
import model.entity.Entity;
import model.entity.FileEntity;
import model.result.*;
import model.result.Error;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class LocalDir implements Dir {

    private final String path;

    public LocalDir(final String path) {
        this.path = path;
    }

    @Override
    public DirResult files(Progress progress) {
        ErrorResult result;
        List<Entity> listFileEntity = new ArrayList<>();
        File dir = new File(path);
        File[] files = FileSystemView.getFileSystemView().getFiles(dir, false);
        if (files != null) {
            double i = 0;
            double chunk = 0;
            progress.value(0);
            if (files.length > 0) {
                chunk = (double) 100 / files.length;
            }
            for (File file : files) {
                listFileEntity.add(getFileEntity(file));
                i += chunk;
                progress.value((int)i);
            }
            result = new ErrorResult(Error.NO);
        } else {
            result = new ErrorResult(Error.FAILED_GET_DIRECTORY_FILES);
        }
        return new DirResult(listFileEntity, dir.getTotalSpace(), dir.getUsableSpace(), result);
    }

    private FileEntity getFileEntity(final File file) {
        Long size;
        String typeName;
        LocalDateTime modifiedDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneOffset.systemDefault());
        boolean isDirectory = false;
        if (file.isDirectory()) {
            size = null;
            typeName = "dir";
            isDirectory = true;
        } else {
            size = file.length();
            String fileName = file.getName();
            typeName = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
        }
        return new FileEntity(file.getPath(), file.getName(), modifiedDate, size, typeName, isDirectory);
    }
}
