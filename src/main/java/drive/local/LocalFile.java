package drive.local;

import model.entity.Entity;
import model.result.*;
import model.result.Error;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LocalFile {
    private final Entity fileEntity;

    public LocalFile(final Entity fileEntity) {
        this.fileEntity = fileEntity;
    }

    public Result execute() {
        Result result;
        if (Desktop.isDesktopSupported()) {
            try {
                File file = new File(fileEntity.path());
                if (file.exists()) {
                    Desktop.getDesktop().open(file);
                    result = new SuccessResult(Status.OK);
                } else {
                    result = new ErrorResult(Error.FILE_NOT_FOUND_ERROR);
                }
            } catch (IOException e) {
                result = new ErrorResult(Error.FILE_NOT_RUN_ERROR);
            }
        } else {
            result = new ErrorResult(Error.FILE_NOT_RUN_ERROR);
        }
        return result;
    }
}
