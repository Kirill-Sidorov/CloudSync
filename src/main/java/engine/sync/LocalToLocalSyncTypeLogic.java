package engine.sync;

import app.logic.SyncMode;
import app.task.LabelUpdating;
import app.task.Progress;
import model.result.SyncResult;

import java.util.ResourceBundle;

public class LocalToLocalSyncTypeLogic implements SyncTypeLogic {

    private final SyncData leftData;
    private final SyncData rightData;
    private final SyncMode syncMode;

    public LocalToLocalSyncTypeLogic(final SyncData leftData, final SyncData rightData, final SyncMode syncMode) {
        this.leftData = leftData;
        this.rightData = rightData;
        this.syncMode = syncMode;
    }

    @Override
    public SyncResult execute(Progress progress, LabelUpdating labelUpdating, ResourceBundle bundle) {
        return null;
    }
}
