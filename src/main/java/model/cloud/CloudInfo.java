package model.cloud;

public class CloudInfo {

    private final CloudType cloudType;
    private final String tokenPath;

    public CloudInfo(final CloudType cloudType, final String tokenPath) {
        this.cloudType = cloudType;
        this.tokenPath = tokenPath;
    }

    public CloudType cloudType() { return cloudType; }
    public String tokenPath() { return tokenPath; }
}
