package app.task;

import app.dialog.ProcessDialog;
import app.logic.SyncMode;
import engine.sync.SyncData;
import engine.sync.SyncEngine;
import model.result.SyncResult;

import javax.swing.*;
import java.util.List;
import java.util.ResourceBundle;

public class SyncTask extends SwingWorker<SyncResult, String> {

    private final SyncData leftData;
    private final SyncData rightData;
    private final SyncMode syncMode;
    private final ProcessDialog dialog;
    private final ResourceBundle bundle;

    public SyncTask(final SyncData leftData, final SyncData rightData, final SyncMode syncMode, final ProcessDialog dialog, final ResourceBundle bundle) {
        this.leftData = leftData;
        this.rightData = rightData;
        this.syncMode = syncMode;
        this.dialog = dialog;
        this.bundle = bundle;
        addPropertyChangeListener(event -> {
            if ("progress".equals(event.getPropertyName())) {
                dialog.progressBar().setValue((Integer) event.getNewValue());
            }
        });
        dialog.cancelButton().addActionListener(event -> cancel(true));
        dialog.setVisible(true);
    }

    @Override
    protected SyncResult doInBackground() throws Exception {
        return new SyncEngine(leftData, rightData, syncMode).start(this::setProgress, this::publish, bundle);
    }

    @Override
    protected void process(List<String> chunks) { dialog.infoLabel().setText(chunks.get(chunks.size() - 1)); }

    @Override
    protected void done() {
        dialog.dispose();
        try {
            if (!isCancelled()) {
                SyncResult result = get();
                System.out.println(result.errorMessage());
            }
        } catch (Exception e) {
            System.out.println("compare crash");
        }
    }
}
