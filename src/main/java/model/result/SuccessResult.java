package model.result;

public class SuccessResult implements Result {

    private final Status status;

    public SuccessResult(final Status status) {
        this.status = status;
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public Error error() {
        return Error.NO;
    }
}
