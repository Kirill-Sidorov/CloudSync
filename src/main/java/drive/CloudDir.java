package drive;

import model.entity.Entity;
import model.result.Result;

public interface CloudDir {
    Result upload(final Entity srcFile);
}
