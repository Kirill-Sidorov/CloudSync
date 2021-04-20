package model.disk;

import app.task.Progress;
import com.google.api.services.drive.Drive;
import drive.googledrive.GoogleDir;
import drive.googledrive.GooglePath;
import model.result.*;

public class Google implements Disk {

    private final String name;
    private final String rootPath;
    private final Drive service;

    public Google(final String name, final String rootPath, final Drive service) {
        this.name = name;
        this.rootPath = rootPath;
        this.service = service;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String rootPath() {
        return rootPath;
    }

    @Override
    public DirResult files(String path, Progress progress) {
        return new GoogleDir(path, service).files(progress);
    }

    @Override
    public Result execute(String path) {
        return new SuccessResult(Status.OK);
    }

    @Override
    public PathResult nextDirPath(String path, String humanReadablePath) {
        return new GooglePath(path, humanReadablePath, service).nextDirPath();
    }

    @Override
    public PathResult previousDirPath(String path, String humanReadablePath) {
        return new GooglePath(path, humanReadablePath, service).previousDirPath();
    }
}
