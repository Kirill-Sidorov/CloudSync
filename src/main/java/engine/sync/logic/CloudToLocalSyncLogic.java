package engine.sync.logic;

import app.logic.SyncMode;
import app.task.LabelUpdating;
import app.task.Progress;
import drive.CloudDir;
import engine.sync.type.CloudToLocalSync;
import engine.sync.SyncAction;
import engine.sync.SyncData;
import model.disk.Cloud;
import model.result.SyncResult;

import java.util.ResourceBundle;

public class CloudToLocalSyncLogic implements SyncLogic {

    private final SyncData leftData;
    private final SyncData rightData;
    private final SyncMode syncMode;

    public CloudToLocalSyncLogic(final SyncData leftData, final SyncData rightData, final SyncMode syncMode) {
        this.leftData = leftData;
        this.rightData = rightData;
        this.syncMode = syncMode;
    }

    @Override
    public SyncResult execute(Progress progress, LabelUpdating labelUpdating, ResourceBundle bundle) {
        SyncResult result = null;
        switch (syncMode) {
            case LEFT:
                result = new CloudToLocalSync(rightData.disk(), rightData.syncDir(), leftData.disk(), leftData.syncDir(), findSyncActionForLeftSync())
                        .sync(progress, labelUpdating, bundle);
                break;
            case RIGHT:
                result = new CloudToLocalSync(leftData.disk(), leftData.syncDir(), rightData.disk(), rightData.syncDir(), findSyncActionForRightSync())
                        .sync(progress, labelUpdating, bundle);
                break;
            case ALL:
                StringBuilder errorMessage = new StringBuilder();
                SyncResult leftResult = new CloudToLocalSync(rightData.disk(), rightData.syncDir(), leftData.disk(), leftData.syncDir(), findSyncActionForLeftSync())
                                        .sync(progress, labelUpdating, bundle);
                SyncResult rightResult = new CloudToLocalSync(leftData.disk(), leftData.syncDir(), rightData.disk(), rightData.syncDir(), findSyncActionForRightSync())
                                        .sync(progress, labelUpdating, bundle);
                errorMessage.append(leftResult.errorMessage());
                errorMessage.append(rightResult.errorMessage());
                result = new SyncResult(errorMessage.toString());
                break;
        }
        return result;
    }

    private SyncAction findSyncActionForLeftSync() {
        if (leftData.disk().isCloud()) {
            return (left, right) -> ((CloudDir)leftData.disk().dir(left)).upload(right);
        } else {
            return (left, right) -> ((Cloud)rightData.disk()).cloudFile(right).download(left);
        }
    }

    private SyncAction findSyncActionForRightSync() {
        if (leftData.disk().isCloud()) {
            return (left, right) -> ((Cloud)leftData.disk()).cloudFile(left).download(right);
        } else {
            return (left, right) -> ((CloudDir)rightData.disk().dir(right)).upload(left);
        }
    }
}
