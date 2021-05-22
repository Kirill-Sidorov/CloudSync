package drive;

import app.task.Progress;
import app.task.TaskState;
import model.result.DirResult;
import model.result.EntityResult;

/**
 * Каталог
 */
public interface Dir {
    /**
     * Получить файлы каталога
     * @param progress Прогресс выполнения получения
     * @param state Состояние задачи (отменена или нет)
     * @return Результат получения файлов
     */
    DirResult getFiles(final Progress progress, final TaskState state);

    /**
     * Получить каталог внутри
     * @param dirName Имя каталога
     * @return Результат получения каталога
     */
    EntityResult getDirInto(final String dirName);

    /**
     * Искать файл внутри
     * @param fileName Имя файла
     * @return Результат поиска файла
     */
    EntityResult searchFileInto(final String fileName);
}
