package drive.local;

import drive.LocalFile;
import model.result.*;
import model.result.Error;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class LocalLocalFile implements LocalFile {
    private final String path;

    public LocalLocalFile(final String path) {
        this.path = path;
    }

    @Override
    public Result execute() {
        Result result;
        if (Desktop.isDesktopSupported()) {
            try {
                File file = Paths.get(path).toFile();
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
