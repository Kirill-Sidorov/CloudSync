package drive.remoteserver;

import drive.DiskSize;

public class RemoteSeverDiskSize implements DiskSize {
    @Override
    public void request() {

    }

    @Override
    public long getTotalSpace() {
        return 0;
    }

    @Override
    public long getUnallocatedSpace() {
        return 0;
    }
}
