package drive.adapter;

@FunctionalInterface
public interface ProgressUpdater {
    void update(long workDone, long max);
}
