package engine.sync;

import app.task.LabelUpdating;
import app.task.Progress;
import model.disk.Disk;
import model.entity.CompDirEntity;
import model.entity.Entity;
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
    public SyncResult sync(Progress progress, LabelUpdating labelUpdating, ResourceBundle bundle) {
        StringBuilder errorMessage = new StringBuilder();
        for (Entity file : srcDir.files()) {
            if (file.isDirectory()) {
                Entity newDestEntity = destDisk.dir(destEntity).giveOrCreateDirInto(file.name());
                SyncResult syncResult = new CloudToLocalSync(srcDisk, (CompDirEntity)file, destDisk, newDestEntity, syncAction)
                        .sync(progress, labelUpdating, bundle);
                errorMessage.append(syncResult.errorMessage());
            } else {
                Result result = syncAction.execute(file, destEntity);
                if (result.status() == Status.ERROR) {
                    errorMessage.append(String.format("%s : %s\n", file.name(), result.error().getMessage(bundle)));
                }
            }
        }
        return new SyncResult(errorMessage.toString());
    }
}
