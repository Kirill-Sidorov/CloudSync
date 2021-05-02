package engine.sync;

import app.logic.SyncMode;
import app.task.LabelUpdating;
import app.task.Progress;
import model.disk.Cloud;
import model.entity.CompDirEntity;
import model.entity.Entity;
import model.result.Error;
import model.result.ErrorResult;
import model.result.Result;

import java.util.ResourceBundle;

public class SyncEngine {
    private final SyncData leftData;
    private final SyncData rightData;
    private final SyncMode syncMode;

    public SyncEngine(final SyncData leftData, final SyncData rightData, final SyncMode syncMode) {
        this.leftData = leftData;
        this.rightData = rightData;
        this.syncMode = syncMode;
    }

    public Result sync(final Progress progress, final LabelUpdating labelUpdating, final ResourceBundle bundle) {
        Result result = new ErrorResult(Error.CURRENT_DISKS_CANNOT_BE_SYNCED);

        int cloudCount = 0;
        if (leftData.disk().isCloud()) {
            cloudCount++;
        } else if (rightData.disk().isCloud()) {
            cloudCount++;
        }

        if (cloudCount == 1) {

        }

        return result;
    }

    private SyncAction findSyncActionForLeftSync() {
        if (leftData.disk().isCloud()) {
            return (src, dest) -> ((Cloud)leftData.disk()).actionWithFile(src).upload(dest);
        } else {
            return (src, dest) -> ((Cloud)rightData.disk()).actionWithFile(src).download(dest);
        }
    }

    private SyncAction findSyncActionForRightSync() {
        if (leftData.disk().isCloud()) {
            return (src, dest) -> ((Cloud)leftData.disk()).actionWithFile(src).download(dest);
        } else {
            return (src, dest) -> ((Cloud)rightData.disk()).actionWithFile(src).upload(dest);
        }
    }

    private Result syncOneWay(SyncAction syncAction, CompDirEntity source, CompDirEntity dest) {
        for (Entity file : source.files()) {
            // if dir? isNewDir? add createDir()
        }
    }
}
