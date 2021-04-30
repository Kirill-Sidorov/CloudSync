package engine;

import app.task.LabelUpdating;
import app.task.Progress;
import model.disk.Disk;
import model.entity.CompDirEntity;
import model.entity.CompFileEntity;
import model.entity.Entity;
import model.result.*;
import model.result.Error;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DirStructure {

    private final Disk disk;
    private final Entity file;

    public DirStructure(final Disk disk, final Entity file) {
        this.disk = disk;
        this.file = file;
    }

    public FileNotExistResult get(final Progress progress, final LabelUpdating labelUpdating, final ResourceBundle bundle) {
        List<Entity> files = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();

        DirResult dirResult = disk.files(file, progress);
        if (dirResult.error() != Error.NO) {
            errorMessage.append(String.format("%s : %s\n", file.name(), dirResult.error().getMessage(bundle)));
        }
        for (Entity fileEntity : dirResult.files()) {
            labelUpdating.text(fileEntity.name());
            if (fileEntity.isDirectory()) {
                FileNotExistResult result = new DirStructure(disk, fileEntity).get(progress, labelUpdating, bundle);
                if (result.status() == Status.ERROR) {
                    errorMessage.append(result.errorMessage());
                }
                files.add(result.file());
            } else {
                files.add(new CompFileEntity(fileEntity));
            }
        }

        return new FileNotExistResult(new CompDirEntity(file, files), errorMessage.toString());
    }
}
