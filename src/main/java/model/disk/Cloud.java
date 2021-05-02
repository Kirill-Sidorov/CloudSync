package model.disk;

import drive.CloudFileAction;
import model.entity.Entity;

public interface Cloud {
    CloudFileAction actionWithFile(final Entity file);
}
