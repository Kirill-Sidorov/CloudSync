package engine.sync.synctype;

import app.logic.SyncMode;
import app.task.LabelUpdating;
import app.task.Progress;
import engine.sync.SyncData;
import model.result.Error;
import model.result.ErrorResult;
import model.result.Result;

import java.util.ResourceBundle;

public class CloudToCloudSyncTypeLogic implements SyncTypeLogic {

    private final SyncData leftData;
    private final SyncData rightData;
    private final SyncMode syncMode;

    public CloudToCloudSyncTypeLogic(final SyncData leftData, final SyncData rightData, final SyncMode syncMode) {
        this.leftData = leftData;
        this.rightData = rightData;
        this.syncMode = syncMode;
    }

    @Override
    public Result execute(Progress progress, LabelUpdating labelUpdating, ResourceBundle bundle) {
        return new ErrorResult(Error.CURRENT_DISKS_CANNOT_BE_SYNCED);
    }
}
