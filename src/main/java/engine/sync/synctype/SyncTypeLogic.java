package engine.sync.synctype;

import app.task.LabelUpdating;
import app.task.Progress;
import model.result.Result;

import java.util.ResourceBundle;

public interface SyncTypeLogic {
    Result execute(final Progress progress, final LabelUpdating labelUpdating, final ResourceBundle bundle);
}
