package model.disk;

import com.dropbox.core.v2.DbxClientV2;
import drive.CloudFile;
import drive.Dir;
import drive.dropbox.DropboxDir;
import drive.dropbox.DropboxFile;
import model.entity.Entity;

public class DropboxDisk implements Disk, Cloud {

    private final String name;
    private final Entity rootFile;
    private final DbxClientV2 client;

    public DropboxDisk(final String name, final Entity rootFile, final DbxClientV2 client) {
        this.name = name;
        this.rootFile = rootFile;
        this.client = client;
    }

    @Override
    public String name() { return name; }

    @Override
    public boolean isCloud() { return true; }

    @Override
    public Entity rootFile() { return rootFile; }

    @Override
    public Dir dir(Entity file) {
        return new DropboxDir(file, client);
    }

    @Override
    public CloudFile cloudFile(Entity fileEntity) { return new DropboxFile(fileEntity, client); }
}
