package app.task;

import engine.CompData;
import engine.CompEngine;
import model.entity.ComparableDirEntity;
import model.entity.Entity;
import model.result.ComparisonResult;

import javax.swing.*;
import java.util.List;
import java.util.ResourceBundle;

public class DirsCompareTask extends SwingWorker<ComparisonResult, String> {

    private final ProgressMonitor progressMonitor;
    private final CompData leftData;
    private final CompData rightData;
    private final ResourceBundle bundle;

    public DirsCompareTask(final ProgressMonitor progressMonitor, final CompData leftData, final CompData rightData, final ResourceBundle bundle) {
        this.progressMonitor = progressMonitor;
        this.leftData = leftData;
        this.rightData = rightData;
        this.bundle = bundle;
    }

    @Override
    protected ComparisonResult doInBackground() throws Exception {
        return new CompEngine(leftData, rightData, this::setProgress, this::publish, bundle).compare();
    }

    @Override
    protected void process(List<String> chunks) {
        progressMonitor.setNote(chunks.get(chunks.size() - 1));
    }

    @Override
    protected void done() {
        ComparisonResult result = null;
        try {
            result = get();
        } catch (Exception e) {
            System.out.println("compare crash");
        }
        if (result != null) {
            List<Entity> files = result.leftFiles();
            System.out.println("------------------------------------------");
            System.out.println(result.errorMessage());
            System.out.println("Left files");
            showFiles(files);
            files = result.rightFiles();
            System.out.println("Right files");
            showFiles(files);
            System.out.println("//////////////////////////////////////////");
        }
    }

    private void showFiles(List<Entity> files) {
        for (Entity file : files) {
            System.out.println(file.name());
            if (file.isDirectory()) {
                showFiles(((ComparableDirEntity) file).files());
            }
        }
    }
}
