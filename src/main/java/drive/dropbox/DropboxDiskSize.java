package drive.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.SpaceUsage;
import drive.DiskSize;

public class DropboxDiskSize implements DiskSize {

    private final DbxClientV2 client;

    private long totalSpace;
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
    public long totalSpace() { return totalSpace; }

    @Override
    public long unallocatedSpace() { return unallocatedSpace; }
}
