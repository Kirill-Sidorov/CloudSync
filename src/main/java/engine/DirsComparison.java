package engine;

import model.disk.Disk;
import model.entity.Entity;
import model.result.ComparisonResult;
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

    public ComparisonResult compare() {

        DirResult result = leftDisk.files(leftPath, value -> {});
        Set<Entity> leftFiles = new HashSet<>(result.files());

        result = rightDisk.files(rightPath, value -> {});
        Set<Entity> rightFiles = new HashSet<>(result.files());

        for (Entity rightFile : result.files()) {



            if (leftFiles.contains(rightFile)) {



            }



            if (rightFile.isDirectory()) {

            } else {
                if (leftFiles.remove(rightFile)) {
                    rightFiles.remove(rightFile);
                }
            }
        }
        return null;
    }
}
