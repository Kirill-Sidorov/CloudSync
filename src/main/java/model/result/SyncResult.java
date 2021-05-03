package model.result;

public class SyncResult implements Result {

    private final String errorMessage;

    public SyncResult(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public Status status() { return errorMessage.length() == 0 ? Status.OK : Status.ERROR; }

    @Override
    public Error error() { return errorMessage.length() == 0 ? Error.NO : Error.UNKNOWN; }
    public String errorMessage() { return errorMessage; }
}
