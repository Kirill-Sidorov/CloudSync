package drive.local;

import model.disk.Disk;
import model.disk.LocalDisk;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Локальная файловая система
 */
public class LocalFS {
    /**
     * Получить локальные хранилища данных
     * @return Локальные диски
     */
    public Map<String, Disk> drives() {
        Map<String, Disk> localDrives = new HashMap<>();
        for (Path path : FileSystems.getDefault().getRootDirectories()) {
            localDrives.put(path.toString(), new LocalDisk(new LocalFileEntity(path.toFile()).create()));
        }
        return localDrives;
    }
}
