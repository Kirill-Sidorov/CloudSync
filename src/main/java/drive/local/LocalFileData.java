package drive.local;

import drive.FileData;
import model.entity.Entity;
import model.entity.FileEntity;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalFileData implements FileData {

    private final File file;

    public LocalFileData(final File file) {
        this.file = file;
    }

    @Override
    public Entity create() {
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
