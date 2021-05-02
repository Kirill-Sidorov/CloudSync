package drive;

import model.entity.Entity;
import model.result.Result;

public interface CloudFileAction {
    Result upload(final Entity destinationFile);
    Result download(final Entity destinationFile);
}
