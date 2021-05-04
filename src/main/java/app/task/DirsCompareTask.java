package app.task;

import app.dialog.ProcessDialog;
import engine.comp.CompData;
import engine.comp.CompEngine;
import model.result.CompResult;

import javax.swing.*;
import java.util.List;
import java.util.ResourceBundle;

public class DirsCompareTask extends SwingWorker<CompResult, String> {

    private final CompData leftData;
    private final CompData rightData;
    private final ProcessDialog dialog;
    private final ViewUpdating viewUpdating;
    private final ResourceBundle bundle;

    public DirsCompareTask(final CompData leftData, final CompData rightData, final ProcessDialog dialog, final ViewUpdating viewUpdating, final ResourceBundle bundle) {
        this.leftData = leftData;
        this.rightData = rightData;
        this.dialog = dialog;
        this.viewUpdating = viewUpdating;
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
    protected CompResult doInBackground() {
        return new CompEngine(leftData, rightData).compare(this::setProgress, this::publish, this::isCancelled, bundle);
    }

    @Override
    protected void process(List<String> chunks) {
        dialog.infoLabel().setText(chunks.get(chunks.size() - 1));
    }

    @Override
    protected void done() {
        dialog.dispose();
        try {
            if (!isCancelled()) {
                viewUpdating.result(get());
            }
        } catch (Exception e) {
            System.out.println("compare crash");
        }
        /*
        if (result != null) {
            List<Entity> files = result.leftDir().files();
            System.out.println("------------------------------------------");
            System.out.println(result.errorMessage());
            System.out.println("Left files");
            showFiles(files);
            files = result.rightDir().files();
            System.out.println("Right files");
            showFiles(files);
            System.out.println("//////////////////////////////////////////");
        }
        */

    }
/*
    private void showFiles(List<Entity> files) {
        for (Entity file : files) {
            System.out.println(file.name());
            if (file.isDirectory()) {
                showFiles(((CompDirEntity) file).files());
            }
        }
    }
 */
}
