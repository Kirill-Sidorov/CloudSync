package engine.sync;

import app.logic.SyncMode;
import app.task.LabelUpdating;
import app.task.Progress;
import model.result.SyncResult;

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

    public SyncResult start(final Progress progress, final LabelUpdating labelUpdating, final ResourceBundle bundle) {
        SyncType syncType;
        boolean leftDiskType = leftData.disk().isCloud();
        boolean rightDiskType = rightData.disk().isCloud();

        if (leftDiskType && rightDiskType) {
            syncType = SyncType.CLOUD_TO_CLOUD;
        } else if (!leftDiskType && !rightDiskType) {
            syncType = SyncType.LOCAL_TO_LOCAL;
        } else {
            syncType = SyncType.CLOUD_TO_LOCAL;
        }
        return syncType.syncLogic(leftData, rightData, syncMode).execute(progress, labelUpdating, bundle);
    }
}
