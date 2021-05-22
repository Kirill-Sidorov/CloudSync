import drive.local.LocalFS;
import drive.local.LocalFile;
import model.disk.Disk;
import model.entity.Entity;
import model.result.*;
import model.result.Error;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class LocalDiskTest {

    private Disk localDisk;
    private List<Entity> rootFiles;

    @Before
    public void setUp() {
        localDisk = new LocalFS().drives().get("C:\\");
        DirResult result = localDisk.getDir(localDisk.getRootDir()).getFiles((i) -> {}, () -> false);
        if (result.error() == Error.NO) {
            rootFiles = result.files();
        }
    }

    @Test
    public void getFilesFromDirTest() {
        Entity dir = null;
        for (Entity file : rootFiles) {
            if (file.isDirectory()) {
                dir = file;
                break;
            }
        }

        if (dir != null) {
            Error expected = Error.NO;
            DirResult result = localDisk.getDir(dir).getFiles((i) -> {}, () -> false);
            Error actual = result.error();
            Assert.assertEquals("failed get dir files - " + dir.name(), expected, actual);
        } else {
            Assert.fail("dir for getting files not found");
        }
    }

    @Test
    public void getDirIntoTest() {
        Error expected = Error.NO;
        EntityResult result = localDisk.getDir(localDisk.getRootDir()).getDirInto("Desktop");
        Error actual = result.error();
        Assert.assertEquals("failed get dir - Desktop", expected, actual);
    }

    @Test
    public void searchFileIntoTest() {
        Status expected = Status.FILE_EXIST;
        EntityResult result = localDisk.getDir(localDisk.getRootDir()).searchFileInto("test.txt");
        Status actual = result.status();
        Assert.assertEquals("failed search file with name test.txt", expected, actual);
    }

    @Test
    public void executeFileTest() {
        Entity exeFile = null;
        for (Entity file : rootFiles) {
            if (file.typeName().equals("txt")) {
                exeFile = file;
                break;
            }
        }
        if (exeFile != null) {
            Status expected = Status.OK;
            Result result = new LocalFile(exeFile).execute();
            Status actual = result.status();
            Assert.assertEquals("execute file text", expected, actual);
        } else {
            Assert.fail("exe file not found");
        }
    }
}
