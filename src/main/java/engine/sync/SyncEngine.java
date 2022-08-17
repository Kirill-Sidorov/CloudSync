package engine.sync;

import app.logic.SyncMode;
import app.task.LabelUpdating;
import app.task.Progress;
import app.task.TaskState;
import model.result.SyncResult;

import java.util.ResourceBundle;

/**
 * Запускающий синхроинизацию класс
 */
public class SyncEngine {
    /** Данные левой таблицы файлов */
    private final SyncData leftData;
    /** Данные левой таблицы файлов */
    private final SyncData rightData;
    /** Направление синхронизации */
    private final SyncMode syncMode;

    public SyncEngine(final SyncData leftData, final SyncData rightData, final SyncMode syncMode) {
        this.leftData = leftData;
        this.rightData = rightData;
        this.syncMode = syncMode;
    }

    /**
     * Начать алгоритм действий для синхронизации
     * @param progress Прогресс выполнения
     * @param labelUpdating Обновлении информации о выполнении синхронизации
     * @param state Состояние задачи (отменена или нет)
     * @param bundle Строки программы
     * @return Результат выполнения алгоритма
     */
    public SyncResult start(final Progress progress, final LabelUpdating labelUpdating, final TaskState state, final ResourceBundle bundle) {
        SyncOption syncOption;
        boolean leftDiskType = leftData.disk().isCloud();
        boolean rightDiskType = rightData.disk().isCloud();

        if (leftDiskType && rightDiskType) {
            syncOption = SyncOption.CLOUD_TO_CLOUD;
        } else if (!leftDiskType && !rightDiskType) {
            syncOption = SyncOption.LOCAL_TO_LOCAL;
        } else {
            syncOption = SyncOption.CLOUD_TO_LOCAL;
        }
        return syncOption.syncLogic(leftData, rightData, syncMode).execute(progress, labelUpdating, state, bundle);
    }
}
