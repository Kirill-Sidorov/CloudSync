package drive;

public interface DiskSize {
    void request();
    long totalSpace();
    long unallocatedSpace();
}
