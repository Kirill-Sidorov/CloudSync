package engine.comp;

import app.task.LabelUpdating;
import app.task.Progress;
import app.task.TaskState;
import model.disk.Disk;
import model.entity.CompDirEntity;
import model.entity.CompFileEntity;
import model.entity.Entity;
import model.result.*;
import model.result.Error;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Содрежимое каталога и его подкаталогов
 */
public class DirStructure {
    /** Сущность хранилища данных */
    private final Disk disk;
    /** Сущность каталога */
    private final Entity file;

    public DirStructure(final Disk disk, final Entity file) {
        this.disk = disk;
        this.file = file;
    }

    /**
     * Получить содержимое каталога и его подкаталогов
     * @param progress Прогресс получения содержимого
     * @param labelUpdating Обновлении информации о получении файлов
     * @param state Состояние задачи (отменена или нет)
     * @param bundle Строки программы
     * @return Результат получения содержимого
     */
    public FileNotExistResult get(final Progress progress, final LabelUpdating labelUpdating, final TaskState state, final ResourceBundle bundle) {
        List<Entity> files = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();

        DirResult dirResult = disk.getDir(file).getFiles(progress, state);
        if (dirResult.error() != Error.NO) {
            errorMessage.append(String.format("%s : %s\n", file.name(), dirResult.error().getMessage(bundle)));
        }
        for (Entity fileEntity : dirResult.files()) {
            labelUpdating.text(fileEntity.name());
            if (fileEntity.isDirectory()) {
                FileNotExistResult result = new DirStructure(disk, fileEntity).get(progress, labelUpdating, state, bundle);
                if (result.status() == Status.ERROR) {
                    errorMessage.append(result.errorMessage());
                }
                files.add(result.file());
            } else {
                files.add(new CompFileEntity(fileEntity));
            }
        }

        return new FileNotExistResult(new CompDirEntity(file, files), errorMessage.toString());
    }
}
