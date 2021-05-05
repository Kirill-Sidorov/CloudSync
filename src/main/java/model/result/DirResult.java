package model.result;

import model.entity.Entity;

import java.util.List;

public class DirResult implements Result {

    private final List<Entity> files;
    private final long totalSpace;
    private final long unallocatedSpace;
    private final Result result;

    public DirResult(final List<Entity> files, final long totalSpace, final long unallocatedSpace, final ErrorResult result) {
        this.files = files;
        this.totalSpace = totalSpace;
        this.unallocatedSpace = unallocatedSpace;
        this.result = result;
    }
    @Override
    public Status status() { return Status.NEED_UPDATE_VIEW_PANEL; }

    @Override
    public Error error() { return result.error(); }

    public List<Entity> files() { return files; }
    public long totalSpace() { return totalSpace; }
    public long unallocatedSpace() { return unallocatedSpace; }
}
