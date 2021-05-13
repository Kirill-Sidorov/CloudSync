package app.task;

import app.dialog.ProcessDialog;
import app.logic.SyncMode;
import engine.sync.SyncData;
import engine.sync.SyncEngine;
import model.result.Error;
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
    protected SyncResult doInBackground() {
        return new SyncEngine(leftData, rightData, syncMode).start(this::setProgress, this::publish, this::isCancelled, bundle);
    }

    @Override
    protected void process(List<String> chunks) { dialog.infoTextField().setText(chunks.get(chunks.size() - 1)); }

    @Override
    protected void done() {
        dialog.dispose();
        try {
            if (!isCancelled()) {
                SyncResult result = get();
                if (result.errorMessage().length() > 0) {
                    JOptionPane.showMessageDialog(dialog.getOwner(), result.errorMessage(), bundle.getString("message.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog.getOwner(), Error.UNKNOWN.getMessage(bundle), bundle.getString("message.title.error"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
