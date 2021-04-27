package model.result;

import model.entity.Entity;

import java.util.List;

public class CompResult implements Result {

    private final List<Entity> leftFiles;
    private final List<Entity> rightFiles;
    private final String errorMessage;
    private final Status status;

    public CompResult(final List<Entity> leftFiles, final List<Entity> rightFiles, final String errorMessage, final Status status) {
        this.leftFiles = leftFiles;
        this.rightFiles = rightFiles;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public Error error() {
        return errorMessage.length() == 0 ? Error.NO : Error.UNKNOWN;
    }

    public String errorMessage() { return errorMessage; }
    public List<Entity> leftFiles() { return leftFiles; }
    public List<Entity> rightFiles() { return rightFiles; }
}
