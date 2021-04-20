package model.disk;

import app.task.Progress;
import model.result.DirResult;
import model.result.PathResult;
import model.result.Result;

public interface Disk {
    String name();
    String rootPath();
    DirResult files(String path, Progress progress);
    Result execute(String path);
    PathResult nextDirPath(String path, String humanReadablePath);
    PathResult previousDirPath(String path, String humanReadablePath);
}
