package engine;

import app.task.LabelUpdating;
import app.task.Progress;
import model.disk.Disk;
import model.entity.ComparableDirEntity;
import model.entity.ComparableFileEntity;
import model.entity.Entity;
import model.result.ComparisonResult;
import model.result.DirResult;
import model.result.Error;
import model.result.Status;
import model.result.SuccessResult;

import java.util.*;

public class CompEngine {

    private final CompData leftData;
    private final CompData rightData;
    private final Progress progress;
    private final LabelUpdating labelUpdating;
    private final ResourceBundle bundle;

    private StringBuilder errorMessage;
    private Status status;

    public CompEngine(final CompData leftData, final CompData rightData, final Progress progress, final LabelUpdating labelUpdating, final ResourceBundle bundle) {
        this.leftData = leftData;
        this.rightData = rightData;
        this.progress = progress;
        this.labelUpdating = labelUpdating;
        this.bundle = bundle;
    }

    public ComparisonResult compare() {
        status = Status.EQUAL;
        errorMessage = new StringBuilder();
        List<Entity> leftList = new ArrayList<>();
        List<Entity> rightList = new ArrayList<>();

        labelUpdating.text("Get dirs files");
        progress.value(0);
        DirResult dirResult = leftData.disk().files(leftData.dirPath(), progress);
        Map<String, Entity> leftMap = new HashMap<>();
        dirResult.files().forEach(file -> leftMap.put(file.name(), file));

        progress.value(0);
        dirResult = rightData.disk().files(rightData.dirPath(), progress);
        Map<String, Entity> rightMap = new HashMap<>();
        dirResult.files().forEach(file -> rightMap.put(file.name(), file));

        for (Entity rightFile : rightMap.values()) {
            labelUpdating.text(rightFile.name());
            Entity leftFile = leftMap.get(rightFile.name());
            if (leftFile == null) {
                executeWhenFileNotExist(rightFile, rightData.disk(), rightList);
            } else {
                executeWhenFileExist(leftFile, rightFile, leftList, rightList, leftMap);
            }
        }

        for (Entity leftFile : leftMap.values()) {
            labelUpdating.text(leftFile.name());
            executeWhenFileNotExist(leftFile, leftData.disk(), leftList);
        }
        return new ComparisonResult(leftList, rightList, errorMessage.toString(), new SuccessResult(status));
    }

    private void executeWhenFileNotExist(Entity file, Disk disk, List<Entity> list) {
        status = Status.NOT_EQUAL;
        if (file.isDirectory()) {
            progress.value(0);
            DirResult result = disk.files(file.path(), progress);
            if (result.error() != Error.NO) {
                errorMessage.append(String.format("%s : %s\n", file.name(), result.error().getMessage(bundle)));
            }
            list.add(new ComparableDirEntity(file, result.files()));
        } else {
            list.add(new ComparableFileEntity(file, true, true));
        }
    }

    public void executeWhenFileExist(Entity leftFile, Entity rightFile, List<Entity> leftList, List<Entity> rightList, Map<String, Entity> leftMap) {
        if (rightFile.isDirectory()) {
            ComparisonResult result = new CompEngine(
                    new CompData(leftData.disk(), leftFile.path()),
                    new CompData(rightData.disk(), rightFile.path()),
                    progress,
                    labelUpdating,
                    bundle)
                    .compare();
            if (result.status() == Status.EQUAL) {
                leftMap.remove(leftFile.name());
            } else {
                status = Status.NOT_EQUAL;
                errorMessage.append(String.format("%s\n", result.errorMessage()));
                rightList.add(new ComparableDirEntity(rightFile, result.rightFiles()));
                leftList.add(new ComparableDirEntity(leftFile, result.leftFiles()));
            }
        } else {
            if (rightFile.modifiedDate().isEqual(leftFile.modifiedDate())) {
                leftMap.remove(leftFile.name());
            } else {
                status = Status.NOT_EQUAL;
                boolean isLastModified = rightFile.modifiedDate().isBefore(leftFile.modifiedDate());
                rightList.add(new ComparableFileEntity(rightFile, false, isLastModified));
                leftList.add(new ComparableFileEntity(leftFile, false, !isLastModified));
            }
        }
    }
}
