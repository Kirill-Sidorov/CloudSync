package drive;

import model.entity.Entity;
import model.result.Result;

public interface CloudFile {
    Result upload(final Entity destFile);
    Result download(final Entity destFile);
}
