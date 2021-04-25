package model.result;

import model.entity.Entity;

import java.util.List;

public class ComparisonResult implements Result {

    private final List<Entity> leftFiles;
    private final List<Entity> rightFiles;
    private final String errorMessage;
    private final Result result;

    public ComparisonResult(final List<Entity> leftFiles, final List<Entity> rightFiles, final String errorMessage, final SuccessResult result) {
        this.leftFiles = leftFiles;
        this.rightFiles = rightFiles;
        this.errorMessage = errorMessage;
        this.result = result;
    }

    @Override
    public Status status() {
        return result.status();
    }

    @Override
    public Error error() {
        return result.error();
    }

    public List<Entity> leftFiles() { return leftFiles; }
    public List<Entity> rightFiles() { return rightFiles; }
    public String errorMessage() { return errorMessage; }
}
