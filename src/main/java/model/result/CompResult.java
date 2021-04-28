package model.result;

import model.entity.CompDirEntity;

public class CompResult implements Result {

    private final CompDirEntity leftDir;
    private final CompDirEntity rightDir;
    private final String errorMessage;
    private final Status status;

    public CompResult(final CompDirEntity leftDir, final CompDirEntity rightDir, final String errorMessage, final Status status) {
        this.leftDir = leftDir;
        this.rightDir = rightDir;
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
    public CompDirEntity leftDir() { return leftDir; }
    public CompDirEntity rightDir() { return rightDir; }
}
