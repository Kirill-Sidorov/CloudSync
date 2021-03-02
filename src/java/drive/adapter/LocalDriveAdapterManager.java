package drive.adapter;

import drive.local.LocalDriveManager;
import model.result.DataResult;
import model.result.Error;
import model.result.PathResult;
import model.result.Result;

public class LocalDriveAdapterManager implements AdapterManageable {
    @Override
    public Result executeFile(String id) { return LocalDriveManager.executeFile(id); }

    @Override
    public PathResult getNextDirectory(String dirId, String readablePath) {
        return (LocalDriveManager.isFileExist(dirId)) ? new PathResult(dirId, dirId) : new PathResult(Error.FILE_NOT_FOUND_ERROR);
    }

    @Override
    public PathResult getPreviousDirectory(String dirId, String readablePath) { return LocalDriveManager.getParentDirectory(dirId); }

    @Override
    public DataResult getListFiles(String dirId, ProgressUpdater updater) { return LocalDriveManager.getListDirectoryFiles(dirId, updater); }
}
