package engine.sync;

import app.logic.SyncMode;
import app.task.LabelUpdating;
import app.task.Progress;
import model.disk.Cloud;
import model.result.Status;
import model.result.SyncResult;

import java.util.ResourceBundle;

public class CloudToLocalSyncTypeLogic implements SyncTypeLogic {

    private final SyncData leftData;
    private final SyncData rightData;
    private final SyncMode syncMode;

    public CloudToLocalSyncTypeLogic(final SyncData leftData, final SyncData rightData, final SyncMode syncMode) {
        this.leftData = leftData;
        this.rightData = rightData;
        this.syncMode = syncMode;
    }

    @Override
    public SyncResult execute(Progress progress, LabelUpdating labelUpdating, ResourceBundle bundle) {
        SyncResult result = null;
        switch (syncMode) {
            case LEFT:
                result = new CloudToLocalSync(rightData.dir(), leftData.dir(), findSyncActionForLeftSync())
                        .sync(progress, labelUpdating, bundle);
                break;
            case RIGHT:
                result = new CloudToLocalSync(leftData.dir(), rightData.dir(), findSyncActionForRightSync())
                        .sync(progress, labelUpdating, bundle);
                break;
            case ALL:
                SyncResult leftResult = new CloudToLocalSync(rightData.dir(), leftData.dir(), findSyncActionForLeftSync())
                                        .sync(progress, labelUpdating, bundle);
                SyncResult rightResult = new CloudToLocalSync(leftData.dir(), rightData.dir(), findSyncActionForRightSync())
                                        .sync(progress, labelUpdating, bundle);
                StringBuilder errorMessage = new StringBuilder();
                if (leftResult.status() == Status.ERROR) {
                    errorMessage.append(leftResult.errorMessage());
                }
                if (rightResult.status() == Status.ERROR) {
                    errorMessage.append(rightResult.errorMessage());
                }
                Status status = errorMessage.length() == 0 ? Status.OK : Status.ERROR;
                result = new SyncResult(status, errorMessage.toString());
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
