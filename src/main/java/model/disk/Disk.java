package model.disk;

import app.task.Progress;
import model.result.DirResult;
import model.result.PathResult;
import model.result.Result;

public interface Disk {
    String name();
    String rootPath();
    DirResult files(final String path, final Progress progress);
    Result execute(final String path);
    PathResult nextDirPath(final String path, final String humanReadablePath);
    PathResult previousDirPath(final String path, final String humanReadablePath);
}
