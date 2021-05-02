package model.disk;

import drive.Dir;
import model.entity.Entity;

public interface Disk {
    String name();
    boolean isCloud();
    Entity rootFile();
    Dir dir(final Entity file);
}
