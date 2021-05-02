package engine.sync;

import app.task.LabelUpdating;
import app.task.Progress;
import model.result.SyncResult;

import java.util.ResourceBundle;

public interface Sync {
    SyncResult sync(final Progress progress, final LabelUpdating labelUpdating, final ResourceBundle bundle);
}
