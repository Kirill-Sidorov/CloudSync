package engine.comp;

import app.task.LabelUpdating;
import app.task.Progress;
import app.task.TaskState;
import model.disk.Disk;
import model.entity.CompFileEntity;
import model.entity.Entity;
import model.result.CompResult;
import model.result.FileExistResult;
import model.result.Status;

import java.util.ResourceBundle;

/**
 * �������� ��������, ����� ������� �����
 * � ����������� ������� ��� ��������� ���������
 */
public class FileExistLogic {

    /** �������� ������ �������� */
    private final Entity leftFile;
    /** �������� ������ ��������� */
    private final Disk leftDisk;
    /** �������� ������ �������� */
    private final Entity rightFile;
    /** �������� ������� ��������� */
    private final Disk rightDisk;

    public FileExistLogic(final Entity leftFile, final Disk leftDisk, final Entity rightFile, final Disk rightDisk) {
        this.leftFile = leftFile;
        this.leftDisk = leftDisk;
        this.rightFile = rightFile;
        this.rightDisk = rightDisk;
    }

    /**
     * ��������� �������� ��������
     * @param progress �������� ����������
     * @param labelUpdating ���������� ���������� � ���������� ���������
     * @param state ��������� ������ (�������� ��� ���)
     * @param bundle ������ ���������
     * @return ��������� ���������� ���������
     */
    public FileExistResult execute(final Progress progress, final LabelUpdating labelUpdating, final TaskState state, final ResourceBundle bundle) {
        if (rightFile.isDirectory()) {
            CompResult result = new CompEngine(new CompData(leftDisk, leftFile), new CompData(rightDisk, rightFile))
                    .compare(progress, labelUpdating, state, bundle);
            return new FileExistResult(result.leftDir(), result.rightDir(), result.errorMessage(), result.status());
        } else {
            Status status = Status.EQUAL;
            boolean isLastModified = false;
            if (!rightFile.size().equals(leftFile.size())) {
                status = Status.NOT_EQUAL;
                isLastModified = rightFile.modifiedDate().isBefore(leftFile.modifiedDate());
            }
            CompFileEntity left = new CompFileEntity(leftFile, isLastModified, rightFile);
            return new FileExistResult(left, left.linkedFile(), status);
        }
    }
}
