package model.disk;

import model.result.DirResult;
import model.result.PathResult;
import model.result.Result;

import javax.swing.*;

public interface Disk {
    String name();
    String rootPath();
    DirResult files(String path, JProgressBar progress);
    Result execute(String path);
    PathResult nextDirPath(String path, String humanReadablePath);
    PathResult previousDirPath(String path, String humanReadablePath);
}
