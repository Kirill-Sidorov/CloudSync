package app.task;

import model.cloud.CloudInfo;
import model.cloud.CloudType;
import model.disk.Disk;
import model.result.CloudsConnectResult;
import model.result.Status;
import model.result.SuccessResult;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CloudDrivesConnectTask extends SwingWorker<CloudsConnectResult, Void> {

    private final Updating updating;

    public CloudDrivesConnectTask(final Updating updating) {
        this.updating = updating;
    }

    @Override
    protected CloudsConnectResult doInBackground() throws Exception {
        Map<String, Disk> cloudDrives =  new HashMap<>();
        Map<String, CloudInfo> cloudsInfo =  new HashMap<>();
        CloudType[] types = CloudType.values();
        for (CloudType type : CloudType.values()) {
            String[] tokens = new File(type.tokensDir()).list();
            if (tokens != null) {
                for (String path : tokens) {
                    Disk disk = type.auth(path).authorize();
                    cloudDrives.put(disk.name(), disk);
                    cloudsInfo.put(disk.name(), new CloudInfo(type, path));
                }
            }
        }
        return new CloudsConnectResult(cloudDrives, cloudsInfo, new SuccessResult(Status.OK));
    }

    @Override
    protected void done() {
        try {
            updating.result(get());
        } catch (Exception e) {
            System.out.println("connecting thread crash");
        }
        super.done();
    }
}
