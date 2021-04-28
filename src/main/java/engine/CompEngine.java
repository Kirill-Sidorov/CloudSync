package engine;

import app.task.LabelUpdating;
import app.task.Progress;
import model.entity.Entity;
import model.result.*;

import java.util.*;

public class CompEngine {

    private final CompData leftData;
    private final CompData rightData;

    private StringBuilder errorMessage;
    private Status status;

    public CompEngine(final CompData leftData, final CompData rightData) {
        this.leftData = leftData;
        this.rightData = rightData;
    }

    public CompResult compare(final Progress progress, final LabelUpdating labelUpdating, final ResourceBundle bundle) {
        status = Status.EQUAL;
        errorMessage = new StringBuilder();
        List<Entity> leftList = new ArrayList<>();
        List<Entity> rightList = new ArrayList<>();

        labelUpdating.text("Get dirs files");

        DirResult dirResult = leftData.disk().files(leftData.dirPath(), progress);
        Map<String, Entity> leftMap = new HashMap<>();
        dirResult.files().forEach(file -> leftMap.put(file.name(), file));

        dirResult = rightData.disk().files(rightData.dirPath(), progress);

        for (Entity rightFile : dirResult.files()) {
            labelUpdating.text(rightFile.name());
            Entity leftFile = leftMap.get(rightFile.name());
            if (leftFile == null) {
                status = Status.NOT_EQUAL;
                FileNotExistResult result = new FileNotExistLogic(rightFile, rightData.disk()).execute(progress, labelUpdating, bundle);
                errorMessage.append(result.errorMessage());
                rightList.add(result.file());
            } else {
                FileExistResult result = new FileExistLogic(leftFile, leftData.disk(), rightFile, rightData.disk()).execute(progress, labelUpdating, bundle);
                errorMessage.append(result.errorMessage());
                if (result.status() == Status.NOT_EQUAL) {
                    status = Status.NOT_EQUAL;
                    rightList.add(result.rightFile());
                    leftList.add(result.leftFile());
                } else {
                    leftMap.remove(leftFile.name());
                }
            }
        }
        for (Entity leftFile : leftMap.values()) {
            status = Status.NOT_EQUAL;
            labelUpdating.text(leftFile.name());
            FileNotExistResult result = new FileNotExistLogic(leftFile, leftData.disk()).execute(progress, labelUpdating, bundle);
            errorMessage.append(result.errorMessage());
            leftList.add(result.file());
        }
        return new CompResult(leftList, rightList, errorMessage.toString(), status);
    }
}
