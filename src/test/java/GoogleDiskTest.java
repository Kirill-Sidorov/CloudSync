import drive.CloudDir;
import drive.google.GoogleAuth;
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

public class GoogleDiskTest {

    private Disk googleDisk;
    private List<Entity> rootFiles;

    @Before
    public void setUp() {
        googleDisk = new GoogleAuth("tokens/google/google1").authorize();
        DirResult result = googleDisk.getDir(googleDisk.getRootDir()).getFiles((i) -> {}, () -> false);
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
            DirResult result = googleDisk.getDir(dir).getFiles((i) -> {}, () -> false);
            Error actual = result.error();
            Assert.assertEquals("failed get dir files - " + dir.name(), expected, actual);
        } else {
            Assert.fail("dir for getting files not found");
        }
    }

    @Test
    public void getDirIntoTest() {
        Error expected = Error.NO;
        EntityResult result = googleDisk.getDir(googleDisk.getRootDir()).getDirInto("Google Sync");
        Error actual = result.error();
        Assert.assertEquals("failed get dir - Google Sync", expected, actual);
    }

    @Test
    public void searchFileIntoTest() {
        Status expected = Status.FILE_EXIST;
        EntityResult result = googleDisk.getDir(googleDisk.getRootDir()).searchFileInto("test.txt");
        Status actual = result.status();
        Assert.assertEquals("failed search file with name test.txt", expected, actual);
    }

    @Test
    public void uploadFileTest() {

        File uploadedFile = new File("C:\\Users\\user\\Downloads\\test.txt");

        if (uploadedFile.exists()) {
            Status expected = Status.OK;

            Entity srcFile = new LocalFileEntity(uploadedFile).create();
            Result result = ((CloudDir) googleDisk.getDir(googleDisk.getRootDir())).upload(srcFile, (i) -> {}, () -> false);
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
            Cloud cloudDisk = (Cloud)googleDisk;
            Result  result = cloudDisk.cloudFile(downloadedFile).download(destFile, (i) -> {}, () -> false);
            Status actual = result.status();
            Assert.assertEquals("file not download", expected, actual);
        } else {
            Assert.fail("file for download not found");
        }
    }
}
