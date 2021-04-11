package app.logic;

import model.disk.Disk;
import model.file.FileEntity;
import model.result.Result;

public class FileClickLogic {

    private final FileEntity file;
    private final Disk disk;
    private final String humanReadablePath;

    public FileClickLogic(final FileEntity file, final Disk disk, final String humanReadablePath) {
        this.file = file;
        this.disk = disk;
        this.humanReadablePath = humanReadablePath;
    }

    public Result perform() {
        if (file.isDirectory()) {
            return disk.nextDirPath(file.getPath(), humanReadablePath);
        } else {
            return disk.execute(file.getPath());
        }
    }
}
