package drive.googledrive;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import drive.Path;
import model.result.Error;
import model.result.ErrorResult;
import model.result.PathResult;

import java.io.IOException;

public class GooglePath implements Path {

    private final String path;
    private final String humanReadablePath;
    private final Drive service;

    public GooglePath(final String path, final String humanReadablePath, final Drive service) {
        this.path = path;
        this.humanReadablePath = humanReadablePath;
        this.service = service;
    }

    @Override
    public PathResult nextDirPath() {
        PathResult pathResult;
        try {
            File file = service.files().get(path).setFields("name").execute();
            if (file.getName() != null) {
                pathResult = new PathResult(path, humanReadablePath + "\\" + file.getName());
            } else {
                pathResult = new PathResult(path, humanReadablePath);
            }
        } catch (IOException e) {
            pathResult = new PathResult(new ErrorResult(Error.FAILED_GET_DIRECTORY_FILES));
        }
        return pathResult;
    }

    @Override
    public PathResult previousDirPath() {
        PathResult pathResult;
        try {
            File file = service.files().get(path).setFields("parents").execute();
            if (file.getParents() != null) {
                String parentId = file.getParents().get(0);
                if (parentId.length() != 0){
                    int index = humanReadablePath.lastIndexOf("\\");
                    String newHumanReadablePath = (index == -1) ? humanReadablePath : humanReadablePath.substring(0, index);
                    pathResult = new PathResult(parentId, newHumanReadablePath);
                } else {
                    pathResult = new PathResult(path, humanReadablePath);
                }
            } else {
                pathResult = new PathResult(new ErrorResult(Error.FILE_NOT_FOUND_ERROR));
            }
        } catch (IOException e) {
            pathResult = new PathResult(new ErrorResult(Error.FAILED_GET_DIRECTORY_FILES));
        }
        return pathResult;
    }
}
