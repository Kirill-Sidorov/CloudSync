package drive.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.SpaceUsage;
import drive.DiskSize;

/**
 * Размер хранилища Dropbox
 */
public class DropboxDiskSize implements DiskSize {

    /** Объект для работы хранилищем учетной записи */
    private final DbxClientV2 client;
    /** Размер хранилища */
    private long totalSpace;
    /** Незанятое пространство */
    private long unallocatedSpace;

    public DropboxDiskSize(final DbxClientV2 client) {
        this.client = client;
        this.totalSpace = 0L;
        this.unallocatedSpace = 0L;
    }

    @Override
    public void request() {
        try {
            SpaceUsage spaceUsage = client.users().getSpaceUsage();
            totalSpace = spaceUsage.getAllocation().getIndividualValue().getAllocated();
            unallocatedSpace = totalSpace - spaceUsage.getUsed();
        } catch (DbxException e) {
            totalSpace = 0L;
            unallocatedSpace = 0L;
        }
    }

    @Override
    public long getTotalSpace() { return totalSpace; }

    @Override
    public long getUnallocatedSpace() { return unallocatedSpace; }
}
