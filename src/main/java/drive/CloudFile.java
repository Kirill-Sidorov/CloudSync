package drive;

import app.task.Progress;
import app.task.TaskState;
import model.entity.Entity;
import model.result.Result;

/**
 * Файл облачного хранилища данных
 */
public interface CloudFile {
    /**
     * Загрузить файл
     * @param destFile Пункт назначения файла (каталог)
     * @param progress Прогресс выполнения отправления
     * @param state Состояние задачи (отменена или нет)
     * @return Результат загрузки
     */
    Result download(final Entity destFile, final Progress progress, final TaskState state);
}
