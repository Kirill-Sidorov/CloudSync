package drive.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;
import drive.Auth;
import model.disk.Disk;
import model.disk.DropboxDisk;
import model.entity.Entity;
import model.entity.FileEntity;

/**
 * Авторизация учетной записи облачного хранилища Dropbox
 */
public class DropboxAuth implements Auth {

    /** Ключ досупа к Dropbox */
    private final String ACCESS_TOKEN = "V0Wx66JK6L8AAAAAAAAAAe-Y3QeCAG_jz1C1KZDjDBaQe0JpGGcJt0k2g9_51Pio";
    /** Путь к ключу от учетной записи */
    private final String tokenPath;

    public DropboxAuth(final String tokenPath) {
        this.tokenPath = tokenPath;
    }

    @Override
    public Disk authorize() {
        String name = "";
        Entity fileEntity = null;
        DbxRequestConfig config = DbxRequestConfig.newBuilder("FileManager_KirillSidorov/1.0").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        try {
            FullAccount account = client.users().getCurrentAccount();
            name = "Dropbox-" + account.getName().getDisplayName();
            fileEntity = new FileEntity("", name, null, 0L, "<dir>", true);
        } catch (DbxException e) {
            System.out.println("dropbox authorize error");
        }
        return new DropboxDisk(name, fileEntity, client);
    }
}
