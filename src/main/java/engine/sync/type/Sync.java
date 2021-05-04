package engine.sync.type;

import app.task.LabelUpdating;
import model.result.SyncResult;

import java.util.ResourceBundle;

public interface Sync {
    SyncResult sync(final LabelUpdating labelUpdating, final ResourceBundle bundle);
}
