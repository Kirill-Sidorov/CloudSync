package engine.comp;

import app.task.LabelUpdating;
import app.task.Progress;
import app.task.TaskState;
import model.disk.Disk;
import model.entity.CompFileEntity;
import model.entity.Entity;
import model.result.FileNotExistResult;

import java.util.ResourceBundle;

public class FileNotExistLogic {

    private final Entity file;
    private final Disk disk;

    public FileNotExistLogic(final Entity file, final Disk disk) {
        this.file = file;
        this.disk = disk;
    }

    public FileNotExistResult execute(final Progress progress, final LabelUpdating labelUpdating, final TaskState state, final ResourceBundle bundle) {
        if (file.isDirectory()) {
            return new DirStructure(disk, file).get(progress, labelUpdating, state, bundle);
        } else {
            return new FileNotExistResult(new CompFileEntity(file));
        }
    }
}
