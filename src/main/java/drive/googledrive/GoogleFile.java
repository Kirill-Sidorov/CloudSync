package drive.googledrive;

import drive.CloudFile;
import model.entity.Entity;
import model.result.Result;

public class GoogleFile implements CloudFile {
    private final Entity fileEntity;

    public GoogleFile(final Entity fileEntity) {
        this.fileEntity = fileEntity;
    }

    @Override
    public Result upload(Entity destFile) {
        return null;
    }

    @Override
    public Result download(Entity destFile) {
        return null;
    }
}
