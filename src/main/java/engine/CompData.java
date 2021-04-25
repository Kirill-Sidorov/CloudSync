package engine;

import model.disk.Disk;

public class CompData {

    private final Disk disk;
    private final String dirPath;

    public CompData(final Disk disk, final String dirPath) {
        this.disk = disk;
        this.dirPath = dirPath;
    }

    public Disk disk() { return disk; }
    public String dirPath() { return dirPath; }
}
