package engine.sync.type;

import app.task.LabelUpdating;
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

/**
 * Синхронизация каталогов из облачного
 * хранилища в локальное или наоборот
 */
public class CloudToLocalSync implements Sync {

    /** Сущность хранилища - источника данных */
    private final Disk srcDisk;
    /** Каталог - источник данных */
    private final CompDirEntity srcDir;
    /** Сущность хранилища - пункта назначения */
    private final Disk destDisk;
    /** Каталог - пункт назначения */
    private final Entity destEntity;
    /** Операция синхронизации */
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
                EntityResult newDestEntity = destDisk.getDir(destEntity).getDirInto(file.name());
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
