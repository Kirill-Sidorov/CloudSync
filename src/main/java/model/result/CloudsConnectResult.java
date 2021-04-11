package model.result;

import model.cloud.CloudInfo;
import model.disk.Disk;

import java.util.Map;

public class CloudsConnectResult implements Result {

    private final Map<String, Disk> cloudDrives;
    private final Map<String, CloudInfo> cloudsInfo;
    private final Result result;

    public CloudsConnectResult(final Map<String, Disk> cloudDrives, final Map<String, CloudInfo> cloudsInfo, final Result result) {
        this.cloudDrives = cloudDrives;
        this.cloudsInfo = cloudsInfo;
        this.result = result;
    }

    @Override
    public Status status() { return result.status(); }

    @Override
    public Error error() { return result.error(); }

    public Map<String, Disk> cloudDrives() { return cloudDrives; }
    public Map<String, CloudInfo> cloudsInfo() { return cloudsInfo; }
}
