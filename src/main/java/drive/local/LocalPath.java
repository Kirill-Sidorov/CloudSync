package drive.local;

import model.result.*;
import model.result.Error;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalPath implements drive.Path {
    private String path;

    public LocalPath(final String path) {
        this.path = path;
    }

    @Override
    public PathResult nextDirPath() {
        if (Paths.get(path).toFile().exists()){
            return new PathResult(path, path);
        } else {
            return new PathResult(new ErrorResult(Error.FILE_NOT_FOUND_ERROR));
        }
    }

    @Override
    public PathResult previousDirPath() {
        PathResult result;
        Path dirPath = Paths.get(path);
        if (dirPath.toFile().exists()) {
            Path previousDir = dirPath.getParent();
            if (previousDir != null) {
                result = new PathResult(previousDir.toString(), previousDir.toString());
            } else {
                result = new PathResult(path, path);
            }
        } else {
            result = new PathResult(new ErrorResult(Error.FILE_NOT_FOUND_ERROR));
        }
        return result;
    }
}
