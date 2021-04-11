package model.result;

import model.file.FileEntity;

import java.util.List;

public class DirResult implements Result {

    private final List<FileEntity> files;
    private long totalSpace;
    private long unallocatedSpace;
    private final Result result;

    public DirResult(final List<FileEntity> files, long totalSpace, long unallocatedSpace, final ErrorResult result) {
        this.files = files;
        this.totalSpace = totalSpace;
        this.unallocatedSpace = unallocatedSpace;
        this.result = result;
    }
    @Override
    public Status status() { return Status.NEED_UPDATE_VIEW_PANEL; }

    @Override
    public Error error() { return result.error(); }

    public List<FileEntity> files() { return files; }
    public long totalSpace() { return totalSpace; }
    public long unallocatedSpace() { return unallocatedSpace; }
}
