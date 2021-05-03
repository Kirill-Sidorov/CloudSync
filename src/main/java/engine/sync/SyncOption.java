package engine.sync;

import app.logic.SyncMode;
import engine.sync.logic.CloudToCloudSyncLogic;
import engine.sync.logic.CloudToLocalSyncLogic;
import engine.sync.logic.LocalToLocalSyncLogic;
import engine.sync.logic.SyncLogic;

public enum SyncOption {
    LOCAL_TO_LOCAL {
        @Override
        public SyncLogic syncLogic(SyncData leftData, SyncData rightData, SyncMode syncMode) {
            return new LocalToLocalSyncLogic(leftData, rightData, syncMode);
        }
    },
    CLOUD_TO_LOCAL {
        @Override
        public SyncLogic syncLogic(SyncData leftData, SyncData rightData, SyncMode syncMode) {
            return new CloudToLocalSyncLogic(leftData, rightData, syncMode);
        }
    },
    CLOUD_TO_CLOUD {
        @Override
        public SyncLogic syncLogic(SyncData leftData, SyncData rightData, SyncMode syncMode) {
            return new CloudToCloudSyncLogic(leftData, rightData, syncMode);
        }
    };

    public abstract SyncLogic syncLogic(final SyncData leftData, final SyncData rightData, final SyncMode syncMode);
}
