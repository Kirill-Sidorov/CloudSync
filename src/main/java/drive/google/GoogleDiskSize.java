package drive.google;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.About;
import drive.DiskSize;

import java.io.IOException;

/**
 * Размер хранилища Google
 */
public class GoogleDiskSize implements DiskSize {

    /** Объект для работы хранилищем учетной записи */
    private final Drive service;
    /** Размер хранилища */
    private long totalSpace;
    /** Незанятое пространство */
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
    public long getTotalSpace() { return totalSpace; }

    @Override
    public long getUnallocatedSpace() { return unallocatedSpace; }
}
