package drive.local;

import app.task.Progress;
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
    public DirResult files(Progress progress) {
        ErrorResult result;
        List<Entity> listFileEntity = new ArrayList<>();
        File dir = new File(fileEntity.path());
        File[] files = FileSystemView.getFileSystemView().getFiles(dir, false);
        if (files != null) {
            double i = 0;
            double chunk = 0;
            progress.value(0);
            if (files.length > 0) {
                chunk = (double) 100 / files.length;
            }
            for (File file : files) {
                listFileEntity.add(new LocalFileData(file).create());
                i += chunk;
                progress.value((int)i);
            }
            result = new ErrorResult(Error.NO);
        } else {
            result = new ErrorResult(Error.FAILED_GET_DIRECTORY_FILES);
        }
        return new DirResult(listFileEntity, dir.getTotalSpace(), dir.getUsableSpace(), result);
    }
}
