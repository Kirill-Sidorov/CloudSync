package model.result;

public class SyncResult implements Result {

    private final Status status;
    private final String errorMessage;

    public SyncResult(final Status status, final String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    @Override
    public Status status() { return status; }

    @Override
    public Error error() { return errorMessage.length() == 0 ? Error.NO : Error.UNKNOWN; }
    public String errorMessage() { return errorMessage; }
}
