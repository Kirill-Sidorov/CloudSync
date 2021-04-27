package model.result;

import model.entity.Entity;

public class FileNotExistResult implements Result {

    private final Entity file;

    private final String errorMessage;

    public FileNotExistResult(final Entity file) {
        this.file = file;
        this.errorMessage = "";
    }

    public FileNotExistResult(final Entity file, final String errorMessage) {
        this.file = file;
        this.errorMessage = errorMessage;
    }

    @Override
    public Status status() { return errorMessage.length() == 0 ? Status.OK : Status.ERROR; }

    @Override
    public Error error() { return errorMessage.length() == 0 ? Error.NO : Error.UNKNOWN; }

    public String errorMessage() { return errorMessage; }
    public Entity file() { return file; }
}
