package app;

import app.dialog.CloudManagerDialog;
import app.dialog.ProcessDialog;
import app.dialog.SyncTaskDialog;
import app.logic.SyncMode;
import app.table.treefiletable.JTreeTable;
import app.task.CloudDrivesConnectTask;
import app.task.DirsCompareTask;
import app.task.SyncTask;
import drive.local.LocalFS;
import model.cloud.CloudInfo;
import model.disk.Disk;
import model.result.CloudsConnectResult;
import model.result.CompResult;
import model.result.Result;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class MainFrameControl {

    private JFrame mainFrame;

    private final JButton compareDirsButton;
    private final JButton setSyncTaskButton;
    private final JButton syncModeButton;
    private final JButton cancelCompModeButton;
    private final JButton syncDirsButton;

    private final FilePanelControl leftPanel;
    private final FilePanelControl rightPanel;

    private final SyncMode[] syncModes = SyncMode.values();
    private final ResourceBundle bundle;
    private final Map<String, Disk> drives;
    private final Map<String, CloudInfo> cloudsInfo;

    private int currentSyncMode;

    public MainFrameControl() {
        bundle = ResourceBundle.getBundle("bundle.strings", Locale.getDefault());
        currentSyncMode = 0;

        drives = new LocalFS().drives();
        cloudsInfo = new HashMap<>();
        mainFrame = new JFrame();

        new CloudDrivesConnectTask(result -> {
                    CloudsConnectResult cloudsConnectResult = (CloudsConnectResult) result;
                    drives.putAll(cloudsConnectResult.cloudDrives());
                    cloudsInfo.putAll(cloudsConnectResult.cloudsInfo());
                    updateComboBoxes();
                },
                mainFrame,
                bundle).
                execute();


        JMenuBar menuBar = new JMenuBar();
        JMenu programMenu = new JMenu(bundle.getString("ui.menu_bar.menu.app"));
        JMenu helpMenu = new JMenu(bundle.getString("ui.menu_bar.menu.help"));

        helpMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                super.mouseClicked(event);
                try {
                    Desktop.getDesktop().open(new File(getClass().getResource("/help/help.html").toURI()));
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

        JMenuItem cloudManagerMenu = new JMenuItem(bundle.getString("ui.menu_bar.menu_item.cloud_manager"));
        programMenu.add(cloudManagerMenu);
        menuBar.add(programMenu);
        menuBar.add(helpMenu);

        JTreeTable rightTreeTable = new JTreeTable(bundle);
        JTreeTable leftTreeTable = new JTreeTable(bundle);

        rightTreeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                leftTreeTable.repaint();
            }
        });

        leftTreeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                rightTreeTable.repaint();
            }
        });

        JPanel left = new JPanel();
        JPanel right = new JPanel();

        leftPanel = new FilePanelControl(bundle, drives, left, leftTreeTable);
        rightPanel = new FilePanelControl(bundle, drives, right, rightTreeTable);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        splitPane.setResizeWeight(0.5);

        JPanel syncControlPanel = new JPanel();
        compareDirsButton = new JButton(bundle.getString("ui.button.comp_current_dirs"));
        compareDirsButton.setToolTipText(bundle.getString("ui.button.tool_tip.comp_current_dirs"));
        setSyncTaskButton = new JButton(bundle.getString("ui.button.set_timer_for_sync"));
        setSyncTaskButton.setToolTipText(bundle.getString("ui.button.tool_tip.set_timer_for_sync"));
        syncModeButton = new JButton(syncModes[0].image());
        syncModeButton.setToolTipText(bundle.getString("ui.button.tool_tip.sync_mode"));
        cancelCompModeButton = new JButton(bundle.getString("ui.button.cancel"));
        cancelCompModeButton.setToolTipText(bundle.getString("ui.button.cancel_comp_mode"));
        syncDirsButton = new JButton(bundle.getString("ui.button.sync_dirs"));

        cancelCompModeButton.addActionListener(event -> viewFileTables(false));

        syncModeButton.setVisible(false);
        cancelCompModeButton.setVisible(false);
        syncDirsButton.setVisible(false);

        syncControlPanel.add(compareDirsButton);
        syncControlPanel.add(setSyncTaskButton);
        syncControlPanel.add(syncModeButton);
        syncControlPanel.add(syncDirsButton);
        syncControlPanel.add(cancelCompModeButton);

        syncModeButton.addActionListener(e -> {
            if (syncModes.length - 1 > currentSyncMode) {
                syncModeButton.setIcon(syncModes[++currentSyncMode].image());
            } else {
                currentSyncMode = 0;
                syncModeButton.setIcon(syncModes[0].image());
            }
        });

        cloudManagerMenu.addActionListener(event -> {
            CloudManagerDialog dialog = new CloudManagerDialog(mainFrame, bundle, drives, cloudsInfo);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    updateComboBoxes();
                }
            });
            dialog.setVisible(true);
        });

        setSyncTaskButton.addActionListener(event -> {
            SyncTaskDialog dialog = new SyncTaskDialog(mainFrame, leftPanel.compData(), rightPanel.compData(), bundle);
            dialog.setVisible(true);
        });

        compareDirsButton.addActionListener(event -> {
            ProcessDialog dialog = new ProcessDialog(mainFrame, bundle.getString("ui.dialog.comp.title"), bundle.getString("message.process.comp"), bundle);
            DirsCompareTask task = new DirsCompareTask(leftPanel.compData(), rightPanel.compData(), dialog, this::viewComparableDirs, bundle);
            task.execute();
        });

        syncDirsButton.addActionListener(event -> {
            viewFileTables(true);
            ProcessDialog dialog = new ProcessDialog(mainFrame, bundle.getString("ui.dialog.sync.title"), bundle.getString("message.process.sync"), bundle);
            SyncTask task = new SyncTask(leftPanel.syncData(), rightPanel.syncData(), syncModes[currentSyncMode], dialog, bundle);
            task.execute();
        });

        mainFrame.setJMenuBar(menuBar);
        mainFrame.add(splitPane);
        mainFrame.add(syncControlPanel, BorderLayout.SOUTH);
        mainFrame.setIconImage(new ImageIcon(getClass().getResource("/img/app-icon.png")).getImage());
        mainFrame.setTitle("CloudSync");
        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setSize(1200, 600);

        mainFrame.setVisible(true);
    }

    private void viewFileTables(boolean isViewRootDir) {
        compareDirsButton.setVisible(true);
        setSyncTaskButton.setVisible(true);
        syncModeButton.setVisible(false);
        cancelCompModeButton.setVisible(false);
        syncDirsButton.setVisible(false);
        leftPanel.viewFileTable(isViewRootDir);
        rightPanel.viewFileTable(isViewRootDir);
    }

    private void viewComparableDirs(Result result) {
        CompResult compResult = (CompResult) result;
        compareDirsButton.setVisible(false);
        setSyncTaskButton.setVisible(false);
        syncModeButton.setVisible(true);
        cancelCompModeButton.setVisible(true);
        syncDirsButton.setVisible(true);
        leftPanel.viewComparableDir(compResult.leftDir());
        rightPanel.viewComparableDir(compResult.rightDir());
    }

    private void updateComboBoxes() {
        leftPanel.updateDiskComboBox();
        rightPanel.updateDiskComboBox();
    }
}