package app.logic;

import model.disk.Disk;
import model.entity.Entity;
import model.result.Result;

public class FileClickLogic {

    private final Entity file;
    private final Disk disk;
    private final String humanReadablePath;

    public FileClickLogic(final Entity file, final Disk disk, final String humanReadablePath) {
        this.file = file;
        this.disk = disk;
        this.humanReadablePath = humanReadablePath;
    }

    public Result perform() {
        if (file.isDirectory()) {
            return disk.nextDirPath(file.path(), humanReadablePath);
        } else {
            return disk.execute(file.path());
        }
    }
}
