package engine.sync.logic;

import app.logic.SyncMode;
import app.task.LabelUpdating;
import app.task.Progress;
import engine.sync.SyncData;
import model.result.Error;
import model.result.ErrorResult;
import model.result.SyncResult;

import java.util.ResourceBundle;

public class LocalToLocalSyncLogic implements SyncLogic {

    private final SyncData leftData;
    private final SyncData rightData;
    private final SyncMode syncMode;

    public LocalToLocalSyncLogic(final SyncData leftData, final SyncData rightData, final SyncMode syncMode) {
        this.leftData = leftData;
        this.rightData = rightData;
        this.syncMode = syncMode;
    }

    @Override
    public SyncResult execute(Progress progress, LabelUpdating labelUpdating, ResourceBundle bundle) {
        return new SyncResult(new ErrorResult(Error.CURRENT_DISKS_CANNOT_BE_SYNCED), bundle);
    }
}
