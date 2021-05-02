package model.disk;

import drive.local.LocalFileAction;
import model.entity.Entity;

public interface Local {
    LocalFileAction actionWithFile(final Entity file);
}
