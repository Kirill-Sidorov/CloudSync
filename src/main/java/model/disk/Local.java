package model.disk;

import app.task.Progress;
import drive.local.LocalDir;
import drive.local.LocalLocalFile;
import drive.local.LocalPath;
import model.result.DirResult;
import model.result.PathResult;
import model.result.Result;

public class Local implements Disk {
    private final String name;
    private final String rootPath;

    public Local(final String name) {
        this.name = name;
        this.rootPath = name;
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
        return new LocalDir(path).files(progress);
    }

    @Override
    public Result execute(String path) {
        return new LocalLocalFile(path).execute();
    }

    @Override
    public PathResult nextDirPath(String path, String humanReadablePath) { return new LocalPath(path).nextDirPath(); }

    @Override
    public PathResult previousDirPath(String path, String humanReadablePath) { return new LocalPath(path).previousDirPath(); }
}
