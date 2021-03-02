package drive;

import drive.local.LocalDriveManager;
import model.entity.DriveEntity;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class MappedDriveManager {
    private static MappedDriveManager instance = new MappedDriveManager();

    private Map<String, DriveEntity> cloudDrives = new Hashtable<String, DriveEntity>();

    public static MappedDriveManager getInstance() {
        return instance;
    }

    public DriveEntity getDriveByName(String name) {
        DriveEntity drive = cloudDrives.get(name);
        if (drive == null) {
            drive = LocalDriveManager.getDriveEntityByName(name);
        } else {
            drive = drive.clone();
        }
        return drive;
    }

    public Set<String> getDrives() {
        Set<String> drives = LocalDriveManager.getRootDirectories();
        drives.addAll(cloudDrives.keySet());
        return drives;
    }
}
