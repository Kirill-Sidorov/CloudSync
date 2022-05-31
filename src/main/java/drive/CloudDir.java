package drive;

import app.task.Progress;
import app.task.TaskState;
import model.entity.Entity;
import model.result.Result;

/**
 * Каталог облачного хранилища данных
 */
public interface CloudDir {
    /**
     * Отправить локальный файл в каталог
     * @param srcFile Отправляемый файл
     * @param progress Прогресс выполнения отправления
     * @param state Состояние задачи (отменена или нет)
     * @return Результат отправления
     */
    Result upload(final Entity srcFile, final Progress progress, final TaskState state);
}