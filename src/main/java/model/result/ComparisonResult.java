package model.result;

import model.entity.FileEntity;

import java.util.List;

public class ComparisonResult implements Result {

    private final List<FileEntity> leftFiles;
    private final List<FileEntity> rightFiles;

    public ComparisonResult(final List<FileEntity> leftFiles, final List<FileEntity> rightFiles) {
        this.leftFiles = leftFiles;
        this.rightFiles = rightFiles;
    }

    @Override
    public Status status() {
        return Status.OK;
    }

    @Override
    public Error error() {
        return Error.NO;
    }
}
