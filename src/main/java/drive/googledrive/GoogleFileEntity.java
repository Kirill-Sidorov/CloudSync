package drive.googledrive;

import com.google.api.services.drive.model.File;
import drive.FileEntity;
import model.entity.Entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class GoogleFileEntity implements FileEntity {

    private final File file;

    public GoogleFileEntity(final File file) {
        this.file = file;
    }

    @Override
    public Entity create() {
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
            typeName = "<dir>";
            isDirectory = true;
        }
        return new model.entity.FileEntity(id, name, modifiedDate, size, typeName, isDirectory);
    }
}
