package engine.comp;

import app.task.LabelUpdating;
import app.task.Progress;
import app.task.TaskState;
import model.disk.Disk;
import model.entity.CompFileEntity;
import model.entity.Entity;
import model.result.FileNotExistResult;

import java.util.ResourceBundle;

/**
 * Алгоритм действий, когда файл с таким же
 * именем не найден при сравнении каталогов
 */
public class FileNotExistLogic {

    /** Сущность файла */
    private final Entity file;
    /** Сущность хранилища данных */
    private final Disk disk;

    public FileNotExistLogic(final Entity file, final Disk disk) {
        this.file = file;
        this.disk = disk;
    }

    /**
     * Выполнить алгоритм действий
     * @param progress Прогресс выполнения
     * @param labelUpdating Обновлении информации о выполнении алгоритма
     * @param state Состояние задачи (отменена или нет)
     * @param bundle Строки программы
     * @return Результат выполнения алгоритма
     */
    public FileNotExistResult execute(final Progress progress, final LabelUpdating labelUpdating, final TaskState state, final ResourceBundle bundle) {
        if (file.isDirectory()) {
            return new DirStructure(disk, file).get(progress, labelUpdating, state, bundle);
        } else {
            return new FileNotExistResult(new CompFileEntity(file));
        }
    }
}
