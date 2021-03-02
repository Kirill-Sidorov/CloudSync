package drive.adapter;

import model.result.DataResult;
import model.result.PathResult;
import model.result.Result;

public interface AdapterManageable {
    Result executeFile(final String id);
    PathResult getNextDirectory(final String dirId, final String readablePath);
    PathResult getPreviousDirectory(final String dirId, final String readablePath);
    DataResult getListFiles(final String dirId, final ProgressUpdater updater);
}
