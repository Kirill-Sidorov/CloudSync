package engine.sync.logic;

import app.logic.SyncMode;
import app.task.LabelUpdating;
import app.task.Progress;
import engine.sync.type.CloudToLocalSync;
import engine.sync.SyncAction;
import engine.sync.SyncData;
import model.disk.Cloud;
import model.result.Result;
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
    public Result execute(Progress progress, LabelUpdating labelUpdating, ResourceBundle bundle) {
        SyncResult result = null;
        switch (syncMode) {
            case LEFT:
                result = new CloudToLocalSync(rightData.disk(), rightData.dir(), leftData.disk(), leftData.dir(), findSyncActionForLeftSync())
                        .sync(progress, labelUpdating, bundle);
                break;
            case RIGHT:
                result = new CloudToLocalSync(leftData.disk(), leftData.dir(), rightData.disk(), rightData.dir(), findSyncActionForRightSync())
                        .sync(progress, labelUpdating, bundle);
                break;
            case ALL:
                StringBuilder errorMessage = new StringBuilder();
                SyncResult leftResult = new CloudToLocalSync(rightData.disk(), rightData.dir(), leftData.disk(), leftData.dir(), findSyncActionForLeftSync())
                                        .sync(progress, labelUpdating, bundle);
                SyncResult rightResult = new CloudToLocalSync(leftData.disk(), leftData.dir(), rightData.disk(), rightData.dir(), findSyncActionForRightSync())
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
            return (src, dest) -> ((Cloud)leftData.disk()).cloudFile(src).upload(dest);
        } else {
            return (src, dest) -> ((Cloud)rightData.disk()).cloudFile(src).download(dest);
        }
    }

    private SyncAction findSyncActionForRightSync() {
        if (leftData.disk().isCloud()) {
            return (src, dest) -> ((Cloud)leftData.disk()).cloudFile(src).download(dest);
        } else {
            return (src, dest) -> ((Cloud)rightData.disk()).cloudFile(src).upload(dest);
        }
    }
}
