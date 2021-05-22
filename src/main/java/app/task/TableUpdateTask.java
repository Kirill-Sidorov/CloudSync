package app.task;

import model.disk.Disk;
import model.entity.Entity;
import model.result.DirResult;
import model.result.Error;
import model.result.ErrorResult;

import javax.swing.*;
import java.util.ArrayList;

public class TableUpdateTask extends SwingWorker<DirResult, Void> {

    private final Disk disk;
    private final Entity fileEntity;
    private final ViewUpdating viewUpdating;

    public TableUpdateTask(final Disk disk, final Entity fileEntity, final ViewUpdating viewUpdating) {
        this.disk = disk;
        this.fileEntity = fileEntity;
        this.viewUpdating = viewUpdating;
    }

    @Override
    protected DirResult doInBackground() {
        return disk.getDir(fileEntity).getFiles(this::setProgress, this::isCancelled);
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
