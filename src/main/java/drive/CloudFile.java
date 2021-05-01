package drive;

import model.result.Result;

public interface CloudFile {
    Result upload();
    Result download();
    Result copy();
}
