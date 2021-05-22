package app.job;

import app.dialog.ProcessDialog;
import app.logic.SyncMode;
import engine.comp.CompData;
import engine.comp.CompEngine;
import engine.sync.SyncData;
import engine.sync.SyncEngine;
import model.result.CompResult;
import model.result.Error;
import model.result.SyncResult;

import javax.swing.*;
import java.util.ResourceBundle;
import java.util.TimerTask;

public class CompAndSyncJob extends TimerTask {

    private final ProcessDialog dialog;
    private final CompData leftData;
    private final CompData rightData;
    private final SyncMode syncMode;
    private final ResourceBundle bundle;
    private boolean isCancel;

    public CompAndSyncJob(final JFrame parentFrame, final CompData leftData, final CompData rightData, final SyncMode syncMode, final ResourceBundle bundle) {
        this.dialog = new ProcessDialog(parentFrame, bundle.getString("ui.dialog.sync_task.title"), bundle.getString("message.process.sync"), bundle);
        this.leftData = leftData;
        this.rightData = rightData;
        this.syncMode = syncMode;
        this.bundle = bundle;
        this.isCancel = false;
        dialog.cancelButton().addActionListener(event -> {
            isCancel = cancel();
            dialog.dispose();
        });
    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(() -> dialog.setVisible(true));
        CompResult compResult = new CompEngine(leftData, rightData).compare(this::setProgress, this::setLabelText, () -> isCancel, bundle);
        if (compResult.error() == Error.NO) {
            SyncResult syncResult = new SyncEngine(
                    new SyncData(leftData.disk(), compResult.leftDir()),
                    new SyncData(rightData.disk(), compResult.rightDir()),
                    syncMode
            ).start(this::setProgress, this::setLabelText, () -> isCancel, bundle);
            System.out.println(syncResult.error().toString());
        }
        SwingUtilities.invokeLater(dialog::dispose);
    }

    private void setProgress(int value) {
        SwingUtilities.invokeLater(() -> dialog.progressBar().setValue(value));
    }

    private void setLabelText(String text) {
        SwingUtilities.invokeLater(() -> dialog.infoTextField().setText(text));
    }
}
