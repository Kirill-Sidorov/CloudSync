package drive.googledrive;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.About;
import drive.DiskSize;

import java.io.IOException;

public class GoogleDiskSize implements DiskSize {

    private final Drive service;

    private long totalSpace;
    private long unallocatedSpace;

    public GoogleDiskSize(final Drive service) {
        this.service = service;
        totalSpace = 0L;
        unallocatedSpace = 0L;
    }

    @Override
    public void request() {
        try {
            About about = service.about().get().setFields("storageQuota(limit, usageInDrive)").execute();
            totalSpace = (about.getStorageQuota().getLimit() != null) ? about.getStorageQuota().getLimit() : 0L;
            unallocatedSpace = (about.getStorageQuota().getUsageInDrive() != null) ? totalSpace - about.getStorageQuota().getUsageInDrive() : 0L;
        } catch (IOException e) {
            totalSpace = 0L;
            unallocatedSpace = 0L;
        }
    }

    @Override
    public long totalSpace() { return totalSpace; }

    @Override
    public long unallocatedSpace() { return unallocatedSpace; }
}
