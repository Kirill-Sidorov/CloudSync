package drive.local;

import model.disk.Disk;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LocalFS {
    public Map<String, Disk> drives() {
        Map<String, Disk> localDrives = new HashMap<>();
        for (Path path : FileSystems.getDefault().getRootDirectories()) {
            localDrives.put(path.toString(), new model.disk.Local(path.toString()));
        }
        return localDrives;
    }
}
