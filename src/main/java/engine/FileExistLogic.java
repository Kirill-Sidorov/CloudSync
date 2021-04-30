package engine;

import app.task.LabelUpdating;
import app.task.Progress;
import model.disk.Disk;
import model.entity.CompFileEntity;
import model.entity.Entity;
import model.result.CompResult;
import model.result.FileExistResult;
import model.result.Status;

import java.util.ResourceBundle;

public class FileExistLogic {

    private final Entity leftFile;
    private final Disk leftDisk;
    private final Entity rightFile;
    private final Disk rightDisk;

    public FileExistLogic(final Entity leftFile, final Disk leftDisk, final Entity rightFile, final Disk rightDisk) {
        this.leftFile = leftFile;
        this.leftDisk = leftDisk;
        this.rightFile = rightFile;
        this.rightDisk = rightDisk;
    }

    public FileExistResult execute(final Progress progress, final LabelUpdating labelUpdating, final ResourceBundle bundle) {
        if (rightFile.isDirectory()) {
            CompResult result = new CompEngine(new CompData(leftDisk, leftFile), new CompData(rightDisk, rightFile))
                    .compare(progress, labelUpdating, bundle);
            return new FileExistResult(result.leftDir(), result.rightDir(), result.errorMessage(), result.status());
        } else {
            Status status = Status.EQUAL;
            boolean isLastModified = false;
            if (!rightFile.modifiedDate().isEqual(leftFile.modifiedDate())) {
                status = Status.NOT_EQUAL;
                isLastModified = rightFile.modifiedDate().isBefore(leftFile.modifiedDate());
            }
            CompFileEntity left = new CompFileEntity(leftFile, isLastModified);
            CompFileEntity right = new CompFileEntity(rightFile, !isLastModified);
            left.setLinkedFile(right);
            right.setLinkedFile(left);
            return new FileExistResult(left, right, status);
        }
    }
}
