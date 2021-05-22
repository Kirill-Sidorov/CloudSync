package app.task;

import model.cloud.CloudInfo;
import model.cloud.CloudType;
import model.disk.Disk;
import model.result.CloudsConnectResult;
import model.result.Error;
import model.result.Status;
import model.result.SuccessResult;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CloudDrivesConnectTask extends SwingWorker<CloudsConnectResult, Void> {

    private final ViewUpdating viewUpdating;
    private final JFrame mainFrame;
    private final ResourceBundle bundle;
    private boolean isError;

    public CloudDrivesConnectTask(final ViewUpdating viewUpdating, final JFrame mainFrame, final ResourceBundle bundle) {
        this.viewUpdating = viewUpdating;
        this.mainFrame = mainFrame;
        this.bundle = bundle;
        isError = false;
    }

    @Override
    protected CloudsConnectResult doInBackground() {
        Map<String, Disk> cloudDrives =  new HashMap<>();
        Map<String, CloudInfo> cloudsInfo =  new HashMap<>();
        for (CloudType type : CloudType.values()) {
            String[] tokens = new File(type.tokensDir()).list();
            if (tokens != null) {
                for (String path : tokens) {
                    Disk disk = type.auth(path).authorize();
                    if (disk.getName().length() > 0) {
                        cloudDrives.put(disk.getName(), disk);
                        cloudsInfo.put(disk.getName(), new CloudInfo(type, disk.getName(), path));
                    } else {
                        isError = true;
                    }
                }
            }
        }
        return new CloudsConnectResult(cloudDrives, cloudsInfo, new SuccessResult(Status.OK));
    }

    @Override
    protected void done() {
        if (isError) {
            JOptionPane.showMessageDialog(mainFrame, Error.CLOUD_DRIVE_NOT_CONNECTED.getMessage(bundle), bundle.getString("message.title.error"), JOptionPane.ERROR_MESSAGE);
        }
        try {
            viewUpdating.result(get());
        } catch (Exception e) {
            System.out.println("connecting thread crash");
        }
    }
}
