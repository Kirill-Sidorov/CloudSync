package drive;

import model.entity.Entity;
import model.result.Result;

public interface CloudFile {
    Result download(final Entity destFile);
}
