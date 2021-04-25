package app.task;

import model.disk.Disk;
import model.result.DirResult;
import model.result.Error;
import model.result.ErrorResult;

import javax.swing.*;
import java.util.ArrayList;

public class TableUpdateTask extends SwingWorker<DirResult, Void> {

    private final Disk disk;
    private final String path;
    private final JProgressBar progress;
    private final ViewUpdating viewUpdating;

    public TableUpdateTask(final Disk disk, final String path, final JProgressBar progress, final ViewUpdating viewUpdating) {
        this.disk = disk;
        this.path = path;
        this.progress = progress;
        this.viewUpdating = viewUpdating;
    }

    @Override
    protected DirResult doInBackground() throws Exception {
        progress.setVisible(true);
        return disk.files(path, progress::setValue);
    }

    @Override
    protected void done() {
        try {
            viewUpdating.result(get());
        } catch (Exception e) {
            viewUpdating.result(new DirResult(new ArrayList<>(), 0L, 0L, new ErrorResult(Error.UPDATE_THREAD_CRASH)));
        }
        progress.setVisible(false);
        progress.setValue(0);
    }
}
