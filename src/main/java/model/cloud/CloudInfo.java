package model.cloud;

public class CloudInfo {

    private final CloudType cloudType;
    private final String name;
    private final String tokenPath;

    public CloudInfo(final CloudType cloudType, final String name, final String tokenPath) {
        this.cloudType = cloudType;
        this.name = name;
        this.tokenPath = tokenPath;
    }

    public CloudType cloudType() { return cloudType; }
    public String name() { return name; }
    public String tokenPath() { return tokenPath; }
}
