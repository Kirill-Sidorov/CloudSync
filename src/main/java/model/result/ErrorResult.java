package model.result;

public class ErrorResult implements Result {

    private final Error error;

    public ErrorResult(final Error error) {
        this.error = error;
    }

    @Override
    public Status status() {
        return Status.ERROR;
    }

    @Override
    public Error error() {
        return error;
    }
}
