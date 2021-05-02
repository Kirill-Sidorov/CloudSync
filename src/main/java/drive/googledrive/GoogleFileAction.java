package drive.googledrive;

import drive.CloudFileAction;
import model.entity.Entity;
import model.result.Result;

public class GoogleFileAction implements CloudFileAction {
    private final Entity fileEntity;

    public GoogleFileAction(final Entity fileEntity) {
        this.fileEntity = fileEntity;
    }

    @Override
    public Result upload(Entity destinationFile) {
        return null;
    }

    @Override
    public Result download(Entity destinationFile) {
        return null;
    }
}
