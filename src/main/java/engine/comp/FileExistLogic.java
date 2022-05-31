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
 * Алгоритм действий, когда найдены файлы
 * с одинаковыми именами при сравнении каталогов
 */
public class FileExistLogic {

    /** Сущность левого каталога */
    private final Entity leftFile;
    /** Сущность левого хранилища */
    private final Disk leftDisk;
    /** Сущность левого каталога */
    private final Entity rightFile;
    /** Сущность правого хранилища */
    private final Disk rightDisk;

    public FileExistLogic(final Entity leftFile, final Disk leftDisk, final Entity rightFile, final Disk rightDisk) {
        this.leftFile = leftFile;
        this.leftDisk = leftDisk;
        this.rightFile = rightFile;
        this.rightDisk = rightDisk;
    }

    /**
     * Выполнить алгоритм действий
     * @param progress Прогресс выполнения
     * @param labelUpdating Обновлении информации о выполнении алгоритма
     * @param state Состояние задачи (отменена или нет)
     * @param bundle Строки программы
     * @return Результат выполнения алгоритма
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
