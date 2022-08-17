package engine.sync.logic;

import app.task.LabelUpdating;
import app.task.Progress;
import app.task.TaskState;
import model.result.SyncResult;

import java.util.ResourceBundle;

/**
 * Алгоритм действий перед синхроинзацией
 */
public interface SyncLogic {
    /**
     * Выполнить алгоритм действий
     * @param progress Прогресс выполнения
     * @param labelUpdating Обновлении информации о выполнении алгоритма
     * @param state Состояние задачи (отменена или нет)
     * @param bundle Строки программы
     * @return Результат выполнения алгоритма
     */
    SyncResult execute(final Progress progress, final LabelUpdating labelUpdating, final TaskState state, final ResourceBundle bundle);
}
