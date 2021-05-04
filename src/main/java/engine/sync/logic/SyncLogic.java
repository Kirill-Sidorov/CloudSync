package engine.sync.logic;

import app.task.LabelUpdating;
import app.task.Progress;
import model.result.SyncResult;

import java.util.ResourceBundle;

public interface SyncLogic {
    SyncResult execute(final Progress progress, final LabelUpdating labelUpdating, final ResourceBundle bundle);
}
