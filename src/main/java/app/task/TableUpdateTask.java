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
    private final Updating updating;

    public TableUpdateTask(final Disk disk, final String path, final JProgressBar progress, final Updating updating) {
        this.disk = disk;
        this.path = path;
        this.progress = progress;
        this.updating = updating;
    }

    @Override
    protected DirResult doInBackground() throws Exception {
        progress.setVisible(true);
        return disk.files(path, progress::setValue);
    }

    @Override
    protected void done() {
        try {
            updating.result(get());
        } catch (Exception e) {
            updating.result(new DirResult(new ArrayList<>(), 0L, 0L, new ErrorResult(Error.UPDATE_THREAD_CRASH)));
        }
        progress.setVisible(false);
        progress.setValue(0);
    }
}
