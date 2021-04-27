package model.result;
import model.entity.Entity;

public class FileExistResult implements Result {

    private final Entity leftFile;
    private final Entity rightFile;

    private final String errorMessage;
    private final Status status;

    public FileExistResult(final Entity leftFile, final Entity rightFile, final Status status) {
        this.leftFile = leftFile;
        this.rightFile = rightFile;
        this.errorMessage = "";
        this.status = status;
    }

    public FileExistResult(final Entity leftFile, final Entity rightFile, final String errorMessage, final Status status) {
        this.leftFile = leftFile;
        this.rightFile = rightFile;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    @Override
    public Status status() { return status; }

    @Override
    public Error error() { return errorMessage.length() == 0 ? Error.NO : Error.UNKNOWN; }

    public String errorMessage() { return errorMessage; }
    public Entity leftFile() { return leftFile; }
    public Entity rightFile() { return rightFile; }
}
