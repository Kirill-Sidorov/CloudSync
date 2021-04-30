package app.task;

import app.dialog.CompProcessDialog;
import engine.CompData;
import engine.CompEngine;
import model.entity.CompDirEntity;
import model.entity.Entity;
import model.result.CompResult;

import javax.swing.*;
import java.util.List;
import java.util.ResourceBundle;

public class DirsCompareTask extends SwingWorker<CompResult, String> {

    private final CompData leftData;
    private final CompData rightData;
    private final CompProcessDialog dialog;
    private final ViewUpdating viewUpdating;
    private final ResourceBundle bundle;

    public DirsCompareTask(final CompData leftData, final CompData rightData, final CompProcessDialog dialog, final ViewUpdating viewUpdating, final ResourceBundle bundle) {
        this.leftData = leftData;
        this.rightData = rightData;
        this.dialog = dialog;
        this.viewUpdating = viewUpdating;
        this.bundle = bundle;

        addPropertyChangeListener(changeEvent -> {
            if ("progress".equals(changeEvent.getPropertyName())) {
                dialog.progressBar().setValue((Integer) changeEvent.getNewValue());
            }
        });

        dialog.cancelButton().addActionListener(event -> cancel(true));
    }

    @Override
    protected CompResult doInBackground() throws Exception {
        return new CompEngine(leftData, rightData).compare(this::setProgress, this::publish, bundle);
    }

    @Override
    protected void process(List<String> chunks) {
        dialog.infoLabel().setText(chunks.get(chunks.size() - 1));
    }

    @Override
    protected void done() {
        dialog.dispose();
        try {
            System.out.println("compare complete");
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
