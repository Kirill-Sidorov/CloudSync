package engine;

import model.disk.Disk;
import model.file.FileEntity;
import model.result.DirResult;

import java.util.HashSet;
import java.util.Set;

public class DirsComparison {

    private final String leftPath;
    private final String rightPath;

    private final Disk leftDisk;
    private final Disk rightDisk;

    public DirsComparison(final Disk leftDisk, final String leftPath, final Disk rightDisk, final String rightPath) {
        this.leftDisk = leftDisk;
        this.leftPath = leftPath;
        this.rightDisk = rightDisk;
        this.rightPath = rightPath;
    }

    public void compare() {
        DirResult result = leftDisk.files(leftPath, value -> {});
        Set<FileEntity> leftFiles = new HashSet<>(result.files());
        result = rightDisk.files(rightPath, value -> {});
        Set<FileEntity> rightFiles = new HashSet<>(result.files());

        for (FileEntity file : leftFiles) {
            if (!file.isDirectory()) {

            }
        }
    }
}
