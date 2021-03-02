package drive.local;

import drive.adapter.ProgressUpdater;
import model.entity.DriveEntity;
import model.entity.DriveType;
import model.entity.FileEntity;
import model.result.*;
import model.result.Error;
import utility.BundleHolder;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class LocalDriveManager {

    private LocalDriveManager() {}

    public static DataResult getListDirectoryFiles(final String path, final ProgressUpdater updater) {
        Error error;
        List<FileEntity> listFileEntity = new ArrayList<>();
        File dir = new File(path);
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File[] files = fileSystemView.getFiles(dir, true);
        if (files != null) {
            int workDone = 1;
            long max = files.length;
            for (File file : files) {
                listFileEntity.add(getFileEntity(file));
                updater.update(workDone++, max);
            }
            error = Error.NO;
        } else {
            error = Error.FAILED_GET_DIRECTORY_FILES;
        }
        return new DataResult(listFileEntity, dir.getTotalSpace(), dir.getUsableSpace(), error);
    }

    private static FileEntity getFileEntity(File file) {
        Long size;
        String typeName;
        LocalDateTime modifiedDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneOffset.systemDefault());
        if (file.isDirectory()) {
            size = null;
            typeName = BundleHolder.getBundle().getString("message.name.directory");
        } else {
            size = file.length();
            typeName = getExtension(file.getName()).orElse("");
        }
        return new FileEntity(file.getPath(), file.getName(), modifiedDate, size,typeName);
    }

    private static Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    /*
    public static long getNumberFilesInDirectory(Path path) {
        long numberFiles;
        try {
            numberFiles = Files.list(path).count();
        } catch (IOException e) {
            numberFiles = 0L;
        }
        return numberFiles;
    }
    */

    public static DriveEntity getDriveEntityByName(final String name) {
        return new DriveEntity(name, DriveType.LOCAL);
    }

    public static Set<String> getRootDirectories() {
        return StreamSupport.stream(FileSystems.getDefault().getRootDirectories().spliterator(), false)
                .map(Path::toString)
                .collect(Collectors.toSet());
    }

    public static boolean isFileExist(final String path) { return Paths.get(path).toFile().exists(); }

    public static Result executeFile(final String path) {
        Result result;
        if (Desktop.isDesktopSupported()) {
            try {
                File file = Paths.get(path).toFile();
                if (file.exists()) {
                    Desktop.getDesktop().open(file);
                    result = new Result(Status.OK);
                } else {
                    result = new Result(Error.FILE_NOT_FOUND_ERROR);
                }
            } catch (IOException e) {
                result = new Result(Error.FILE_NOT_RUN_ERROR);
            }
        } else {
            result = new Result(Error.FILE_NOT_RUN_ERROR);
        }
        return result;
    }

    public static PathResult getParentDirectory(final String path) {
        PathResult result;
        Path dirPath = Paths.get(path);
        if (dirPath.toFile().exists()) {
            Path parentDir = dirPath.getParent();
            if (parentDir != null) {
                result = new PathResult(parentDir.toString(), parentDir.toString());
            } else {
                result = new PathResult(path, path);
            }
        } else {
            result = new PathResult(Error.FILE_NOT_FOUND_ERROR);
        }
        return result;
    }
}
