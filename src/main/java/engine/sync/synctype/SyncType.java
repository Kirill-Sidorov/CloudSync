package engine.sync.synctype;

import app.logic.SyncMode;
import engine.sync.SyncData;

public enum SyncType {
    LOCAL_TO_LOCAL {
        @Override
        public SyncTypeLogic syncLogic(SyncData leftData, SyncData rightData, SyncMode syncMode) {
            return new LocalToLocalSyncTypeLogic(leftData, rightData, syncMode);
        }
    },
    CLOUD_TO_LOCAL {
        @Override
        public SyncTypeLogic syncLogic(SyncData leftData, SyncData rightData, SyncMode syncMode) {
            return new CloudToLocalSyncTypeLogic(leftData, rightData, syncMode);
        }
    },
    CLOUD_TO_CLOUD {
        @Override
        public SyncTypeLogic syncLogic(SyncData leftData, SyncData rightData, SyncMode syncMode) {
            return new CloudToCloudSyncTypeLogic(leftData, rightData, syncMode);
        }
    };

    public abstract SyncTypeLogic syncLogic(final SyncData leftData, final SyncData rightData, final SyncMode syncMode);
}
