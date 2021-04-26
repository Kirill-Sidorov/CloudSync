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
    private final ViewUpdating viewUpdating;

    public TableUpdateTask(final Disk disk, final String path, final ViewUpdating viewUpdating) {
        this.disk = disk;
        this.path = path;
        this.viewUpdating = viewUpdating;
    }

    @Override
    protected DirResult doInBackground() throws Exception {
        return disk.files(path, this::setProgress);
    }

    @Override
    protected void done() {
        try {
            viewUpdating.result(get());
        } catch (Exception e) {
            viewUpdating.result(new DirResult(new ArrayList<>(), 0L, 0L, new ErrorResult(Error.UPDATE_THREAD_CRASH)));
        }
    }
}
