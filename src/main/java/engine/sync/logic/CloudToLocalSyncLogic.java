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
                result = new CloudToLocalSync(rightData.disk(), rightData.syncDir(), leftData.disk(), leftData.syncDir(), findSyncActionForLeftSync(progress))
                        .sync(labelUpdating, bundle);
                break;
            case RIGHT:
                result = new CloudToLocalSync(leftData.disk(), leftData.syncDir(), rightData.disk(), rightData.syncDir(), findSyncActionForRightSync(progress))
                        .sync(labelUpdating, bundle);
                break;
            case ALL:
                StringBuilder errorMessage = new StringBuilder();
                SyncResult leftResult = new CloudToLocalSync(rightData.disk(), rightData.syncDir(), leftData.disk(), leftData.syncDir(), findSyncActionForLeftSync(progress))
                                        .sync(labelUpdating, bundle);
                SyncResult rightResult = new CloudToLocalSync(leftData.disk(), leftData.syncDir(), rightData.disk(), rightData.syncDir(), findSyncActionForRightSync(progress))
                                        .sync(labelUpdating, bundle);
                errorMessage.append(leftResult.errorMessage());
                errorMessage.append(rightResult.errorMessage());
                result = new SyncResult(errorMessage.toString());
                break;
        }
        return result;
    }

    private SyncAction findSyncActionForLeftSync(Progress progress) {
        if (leftData.disk().isCloud()) {
            return (local, cloud) -> ((CloudDir)leftData.disk().dir(cloud)).upload(local, progress);
        } else {
            return (cloud, local) -> ((Cloud)rightData.disk()).cloudFile(cloud).download(local, progress);
        }
    }

    private SyncAction findSyncActionForRightSync(Progress progress) {
        if (leftData.disk().isCloud()) {
            return (cloud, local) -> ((Cloud)leftData.disk()).cloudFile(cloud).download(local, progress);
        } else {
            return (local, cloud) -> ((CloudDir)rightData.disk().dir(cloud)).upload(local, progress);
        }
    }
}
