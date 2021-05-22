package engine.comp;

import app.task.LabelUpdating;
import app.task.Progress;
import app.task.TaskState;
import model.entity.CompDirEntity;
import model.entity.Entity;
import model.result.*;

import java.util.*;

/**
 * Сравнение каталогов
 */
public class CompEngine {

    private final CompData leftData;
    private final CompData rightData;

    public CompEngine(final CompData leftData, final CompData rightData) {
        this.leftData = leftData;
        this.rightData = rightData;
    }

    /**
     * Сравнить каталоги
     * @param progress Прогресс выполнения сравнения
     * @param labelUpdating Обновление информации о сравнении файлов
     * @param state Состояние задачи (отменена или нет)
     * @param bundle Строки программы
     * @return Результат сравнения каталогов
     */
    public CompResult compare(final Progress progress, final LabelUpdating labelUpdating, final TaskState state, final ResourceBundle bundle) {
        Status status = Status.EQUAL;
        StringBuilder errorMessage = new StringBuilder();
        List<Entity> leftList = new ArrayList<>();
        List<Entity> rightList = new ArrayList<>();

        labelUpdating.text(leftData.fileEntity().name());
        DirResult dirResult = leftData.disk().getDir(leftData.fileEntity()).getFiles(progress, state);
        Map<String, Entity> leftMap = new HashMap<>();
        dirResult.files().forEach(file -> leftMap.put(file.name(), file));

        labelUpdating.text(rightData.fileEntity().name());
        dirResult = rightData.disk().getDir(rightData.fileEntity()).getFiles(progress, state);

        for (Entity rightFile : dirResult.files()) {
            labelUpdating.text(rightFile.name());
            Entity leftFile = leftMap.get(rightFile.name());
            if (leftFile == null) {
                status = Status.NOT_EQUAL;
                FileNotExistResult result = new FileNotExistLogic(rightFile, rightData.disk())
                        .execute(progress, labelUpdating, state, bundle);
                errorMessage.append(result.errorMessage());
                rightList.add(result.file());
            } else {
                FileExistResult result = new FileExistLogic(leftFile, leftData.disk(), rightFile, rightData.disk())
                        .execute(progress, labelUpdating, state, bundle);
                errorMessage.append(result.errorMessage());
                if (result.status() == Status.NOT_EQUAL) {
                    status = Status.NOT_EQUAL;
                    rightList.add(result.rightFile());
                    leftList.add(result.leftFile());
                }
                leftMap.remove(leftFile.name());
            }
        }
        for (Entity leftFile : leftMap.values()) {
            status = Status.NOT_EQUAL;
            labelUpdating.text(leftFile.name());
            FileNotExistResult result = new FileNotExistLogic(leftFile, leftData.disk())
                    .execute(progress, labelUpdating, state, bundle);
            errorMessage.append(result.errorMessage());
            leftList.add(result.file());
        }
        return new CompResult(
                new CompDirEntity(leftData.fileEntity(), leftList),
                new CompDirEntity(rightData.fileEntity(), rightList),
                errorMessage.toString(),
                status);
    }
}
