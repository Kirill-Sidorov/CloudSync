package engine.sync.type;

import app.task.LabelUpdating;
import app.task.Progress;
import engine.sync.SyncAction;
import model.disk.Disk;
import model.entity.CompDirEntity;
import model.entity.CompFileEntity;
import model.entity.Entity;
import model.result.EntityResult;
import model.result.Result;
import model.result.Status;
import model.result.SyncResult;

import java.util.ResourceBundle;

public class CloudToLocalSync implements Sync {

    private final Disk srcDisk;
    private final CompDirEntity srcDir;
    private final Disk destDisk;
    private final Entity destEntity;
    private final SyncAction syncAction;

    public CloudToLocalSync(final Disk srcDisk, final CompDirEntity srcDir, final Disk destDisk, final Entity destEntity, final SyncAction syncAction) {
        this.srcDisk = srcDisk;
        this.srcDir = srcDir;
        this.destDisk = destDisk;
        this.destEntity = destEntity;
        this.syncAction = syncAction;
    }

    @Override
    public SyncResult sync(LabelUpdating labelUpdating, ResourceBundle bundle) {
        StringBuilder errorMessage = new StringBuilder();
        for (Entity file : srcDir.files()) {
            labelUpdating.text(file.name());
            if (file.isDirectory()) {
                EntityResult newDestEntity = destDisk.dir(destEntity).getDirInto(file.name());
                if (newDestEntity.status() == Status.FILE_EXIST) {
                    SyncResult syncResult = new CloudToLocalSync(srcDisk, (CompDirEntity)file, destDisk, newDestEntity.entity(), syncAction)
                            .sync(labelUpdating, bundle);
                    errorMessage.append(syncResult.errorMessage());
                } else {
                    errorMessage.append(String.format("%s : %s\n", file.name(), newDestEntity.error().getMessage(bundle)));
                }
            } else {
                if (((CompFileEntity)file).isLastModified()) {
                    Result result = syncAction.execute(file, destEntity);
                    if (result.status() == Status.ERROR) {
                        errorMessage.append(String.format("%s : %s\n", file.name(), result.error().getMessage(bundle)));
                    }
                }
            }
        }
        return new SyncResult(errorMessage.toString());
    }
}
