package engine.sync.type;

import app.task.LabelUpdating;
import model.result.SyncResult;

import java.util.ResourceBundle;

/**
 * Синхронизация каталогов
 */
public interface Sync {
    /**
     * Синхронизировать
     * @param labelUpdating Обновлении информации о выполнении синхронизации
     * @param bundle Строки программы
     * @return Результат синхронизации
     */
    SyncResult sync(final LabelUpdating labelUpdating, final ResourceBundle bundle);
}
