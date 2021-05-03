package engine.sync.logic;

import app.task.LabelUpdating;
import app.task.Progress;
import model.result.Result;

import java.util.ResourceBundle;

public interface SyncLogic {
    Result execute(final Progress progress, final LabelUpdating labelUpdating, final ResourceBundle bundle);
}
