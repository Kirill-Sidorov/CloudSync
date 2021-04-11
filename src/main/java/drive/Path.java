package drive;

import model.result.PathResult;

public interface Path {
    PathResult nextDirPath();
    PathResult previousDirPath();
}
