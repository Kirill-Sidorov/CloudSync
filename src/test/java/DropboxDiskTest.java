import drive.CloudDir;
import drive.dropbox.DropboxAuth;
import drive.local.LocalFileEntity;
import model.disk.Cloud;
import model.disk.Disk;
import model.entity.Entity;
import model.result.*;
import model.result.Error;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class DropboxDiskTest {

    private Disk dropboxDisk;
    private List<Entity> rootFiles;

    @Before
    public void setUp() {
        dropboxDisk = new DropboxAuth("").authorize();
        DirResult result = dropboxDisk.getDir(dropboxDisk.getRootDir()).getFiles((i) -> {}, () -> false);
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
            DirResult result = dropboxDisk.getDir(dir).getFiles((i) -> {}, () -> false);
            Error actual = result.error();
            Assert.assertEquals("failed get dir files - " + dir.name(), expected, actual);
        } else {
            Assert.fail("dir for getting files not found");
        }
    }

    @Test
    public void getDirIntoTest() {
        Error expected = Error.NO;
        EntityResult result = dropboxDisk.getDir(dropboxDisk.getRootDir()).getDirInto("Dropbox Sync");
        Error actual = result.error();
        Assert.assertEquals("failed get dir - Dropbox Sync", expected, actual);
    }

    @Test
    public void searchFileIntoTest() {
        Status expected = Status.FILE_EXIST;
        EntityResult result = dropboxDisk.getDir(dropboxDisk.getRootDir()).searchFileInto("test.txt");
        Status actual = result.status();
        Assert.assertEquals("failed search file with name test.txt", expected, actual);
    }

    @Test
    public void uploadFileTest() {

        File uploadedFile = new File("C:\\Users\\user\\Downloads\\test.txt");

        if (uploadedFile.exists()) {
            Status expected = Status.OK;

            Entity srcFile = new LocalFileEntity(uploadedFile).create();
            Result result = ((CloudDir) dropboxDisk.getDir(dropboxDisk.getRootDir())).upload(srcFile, (i) -> {}, () -> false);
            Status actual = result.status();
            Assert.assertEquals("file not upload", expected, actual);

        } else {
            Assert.fail("file for upload not found");
        }
    }

    @Test
    public void downloadFileTest() {
        Entity downloadedFile = null;
        for (Entity file : rootFiles) {
            if (file.name().equals("test.txt")) {
                downloadedFile = file;
                break;
            }
        }
        if (downloadedFile != null) {
            Status expected = Status.OK;

            Entity destFile = new LocalFileEntity(new File("C:\\Users\\user\\Downloads")).create();
            Cloud cloudDisk = (Cloud) dropboxDisk;
            Result  result = cloudDisk.cloudFile(downloadedFile).download(destFile, (i) -> {}, () -> false);
            Status actual = result.status();
            Assert.assertEquals("file not download", expected, actual);
        } else {
            Assert.fail("file for download not found");
        }
    }
}
