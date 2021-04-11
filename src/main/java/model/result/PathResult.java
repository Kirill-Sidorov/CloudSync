package model.result;

public class PathResult implements Result {
    private String pathId;
    private String humanReadablePath;
    private Result result;

    public PathResult(final ErrorResult result) { this.result = result; }

    public PathResult(final String pathId, final String humanReadablePath) {
        this.pathId = pathId;
        this.humanReadablePath = humanReadablePath;
        this.result = new SuccessResult(Status.NEED_UPDATE_TABLE);
    }

    @Override
    public Status status() { return result.status(); }

    @Override
    public Error error() { return result.error(); }

    public String path() { return pathId; }
    public String humanReadablePath() { return humanReadablePath; }
}
