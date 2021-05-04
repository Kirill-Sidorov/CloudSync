package drive.local;

import app.task.Progress;
import app.task.TaskState;
import drive.Dir;
import model.entity.Entity;
import model.result.*;
import model.result.Error;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalDir implements Dir {

    private final Entity fileEntity;

    public LocalDir(final Entity fileEntity) {
        this.fileEntity = fileEntity;
    }

    @Override
    public DirResult files(Progress progress, TaskState state) {
        ErrorResult result = new ErrorResult(Error.NO);
        List<Entity> listFileEntity = new ArrayList<>();
        File dir = new File(fileEntity.path());
        if (!state.isCancel()) {
            File[] files = FileSystemView.getFileSystemView().getFiles(dir, false);
            if (files != null) {
                double i = 0;
                double chunk = 0;
                progress.value(0);
                if (files.length > 0) {
                    chunk = (double) 100 / files.length;
                }
                System.out.println(files.length);
                for (File file : files) {
                    listFileEntity.add(new LocalFileEntity(file).create());
                    i += chunk;
                    progress.value((int)i);
                }
            } else {
                result = new ErrorResult(Error.FAILED_GET_DIRECTORY_FILES);
            }
        }
        return new DirResult(listFileEntity, dir.getTotalSpace(), dir.getUsableSpace(), result);
    }

    @Override
    public Entity giveOrCreateDirInto(String dirName) {
        File dir = new File(fileEntity.path() + "\\" + dirName);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdir();
        }
        return new LocalFileEntity(dir).create();
    }
}
