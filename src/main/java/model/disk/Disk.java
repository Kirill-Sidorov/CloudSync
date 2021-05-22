package model.disk;

import drive.Dir;
import model.entity.Entity;

public interface Disk {
    String getName();
    boolean isCloud();
    Entity getRootDir();
    Dir getDir(final Entity file);
}
