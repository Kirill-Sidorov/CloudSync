package engine.sync;

import app.task.LabelUpdating;
import app.task.Progress;
import model.entity.CompDirEntity;
import model.result.SyncResult;

import java.util.ResourceBundle;

public class CloudToLocalSync implements Sync {

    private final CompDirEntity source;
    private final CompDirEntity dest;
    private final SyncAction syncAction;

    public CloudToLocalSync(final CompDirEntity source, final CompDirEntity dest, final SyncAction syncAction) {
        this.source = source;
        this.dest = dest;
        this.syncAction = syncAction;
    }

    @Override
    public SyncResult sync(Progress progress, LabelUpdating labelUpdating, ResourceBundle bundle) {
        return null;
    }
}
