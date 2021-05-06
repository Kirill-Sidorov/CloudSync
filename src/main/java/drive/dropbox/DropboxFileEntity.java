package drive.dropbox;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import drive.FileEntity;
import model.entity.Entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DropboxFileEntity implements FileEntity {

    private final Metadata file;

    public DropboxFileEntity(final Metadata file) {
        this.file = file;
    }

    @Override
    public Entity create() {
        String path = "";
        String name = "";
        LocalDateTime modifiedDate = null;
        Long size = null;
        String typeName = "";
        boolean isDirectory = false;

        if (file instanceof FileMetadata) {
            isDirectory = false;
            FileMetadata fileMetadata = (FileMetadata)file;
            path = (fileMetadata.getPathDisplay() != null) ? fileMetadata.getPathDisplay() : "";
            name = (fileMetadata.getName() != null) ? fileMetadata.getName() : "";
            if (fileMetadata.getClientModified() != null) {
                modifiedDate = LocalDateTime.ofInstant(fileMetadata.getClientModified().toInstant(), ZoneOffset.systemDefault());
            } else {
                modifiedDate = null;
            }
            size = fileMetadata.getSize();
            typeName = name.contains(".") ? name.substring(name.lastIndexOf(".") + 1) : "";
        }  else if (file instanceof FolderMetadata) {
            isDirectory = true;
            FolderMetadata folderMetadata = (FolderMetadata) file;
            path = (folderMetadata.getPathDisplay() != null) ? folderMetadata.getPathDisplay() : "";
            name = (folderMetadata.getName() != null) ? folderMetadata.getName() : "";
            typeName = "dir";
        }
        return new model.entity.FileEntity(path, name, modifiedDate, size, typeName, isDirectory);
    }
}
