package model.result;

import java.util.ResourceBundle;

public class SyncResult implements Result {

    private final Result result;
    private final String errorMessage;

    public SyncResult(final ErrorResult result, final ResourceBundle bundle) {
        this.result = result;
        errorMessage = result.error().getMessage(bundle);
    }

    public SyncResult(final String errorMessage) {
        this.errorMessage = errorMessage;
        if (errorMessage.length() == 0) {
            result = new SuccessResult(Status.OK);
        } else {
            result = new ErrorResult(Error.UNKNOWN);
        }
    }

    @Override
    public Status status() { return result.status(); }

    @Override
    public Error error() { return result.error(); }
    public String errorMessage() { return errorMessage; }
}
