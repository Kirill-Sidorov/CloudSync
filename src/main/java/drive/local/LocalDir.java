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
import java.util.Optional;

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
        File[] files = FileSystemView.getFileSystemView().getFiles(dir, true);
        if (files.length != 0) {
            int chunk = files.length > 100 ? files.length / 100 : 100 / files.length;
            int i = 0;
            for (File file : files) {
                listFileEntity.add(getFileEntity(file));
                progress.value(i += chunk);
            }
            result = new ErrorResult(Error.NO);
        } else {
            result = new ErrorResult(Error.FAILED_GET_DIRECTORY_FILES);
        }
        return new DirResult(listFileEntity, dir.getTotalSpace(), dir.getUsableSpace(), result);
    }

    private FileEntity getFileEntity(File file) {
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
            typeName = getExtension(file.getName()).orElse("");
        }
        return new FileEntity(file.getPath(), file.getName(), modifiedDate, size, typeName, isDirectory);
    }

    private Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
