package model.disk;

import drive.CloudFile;
import model.entity.Entity;

public interface Cloud {
    CloudFile cloudFile(final Entity file);
}
