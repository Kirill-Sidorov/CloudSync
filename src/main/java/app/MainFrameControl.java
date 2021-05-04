package app;

import app.dialog.CloudManagerDialog;
import app.dialog.ProcessDialog;
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
import java.util.*;

public class MainFrameControl {

    private JFrame mainFrame;
    private JMenuBar menuBar;
    private JMenu programMenu;
    private JMenuItem cloudManagerMenu;

    private JButton compareDirsButton;
    private JButton syncModeButton;
    private JButton cancelCompModeButton;
    private JButton syncDirsButton;

    private JSplitPane splitPane;
    private JPanel syncControlPanel;
    private FilePanelControl leftPanel;
    private FilePanelControl rightPanel;

    private final SyncMode[] syncModes = SyncMode.values();
    private final ResourceBundle bundle;
    private Map<String, Disk> drives;
    private Map<String, CloudInfo> cloudsInfo;

    private int currentSyncMode;

    public MainFrameControl() {
        bundle = ResourceBundle.getBundle("bundle.strings", Locale.getDefault());
        currentSyncMode = 0;

        drives = new LocalFS().drives();
        cloudsInfo = new HashMap<>();

        new CloudDrivesConnectTask(result -> {
            CloudsConnectResult cloudsConnectResult = (CloudsConnectResult) result;
            drives.putAll(cloudsConnectResult.cloudDrives());
            cloudsInfo.putAll(cloudsConnectResult.cloudsInfo());
            updateComboBoxes();
        }).execute();


        menuBar = new JMenuBar();
        programMenu = new JMenu(bundle.getString("ui.menu_bar.menu.app"));
        cloudManagerMenu = new JMenuItem(bundle.getString("ui.menu_bar.menu_item.cloud_manager"));
        programMenu.add(cloudManagerMenu);
        menuBar.add(programMenu);

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

        leftPanel = new FilePanelControl(bundle, drives, leftTreeTable);
        rightPanel = new FilePanelControl(bundle, drives, rightTreeTable);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel.mainJPanel(), rightPanel.mainJPanel());
        splitPane.setResizeWeight(0.5);

        syncControlPanel = new JPanel();
        compareDirsButton = new JButton(bundle.getString("ui.button.comp_current_dirs"));
        syncModeButton = new JButton(syncModes[0].image());
        cancelCompModeButton = new JButton(bundle.getString("ui.button.cancel_comp_mode"));
        syncDirsButton = new JButton(bundle.getString("ui.button.sync_dirs"));

        cancelCompModeButton.addActionListener(event -> viewFileTables(false));

        syncModeButton.setVisible(false);
        cancelCompModeButton.setVisible(false);
        syncDirsButton.setVisible(false);

        syncControlPanel.add(compareDirsButton);
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

        compareDirsButton.addActionListener(event -> {
            ProcessDialog dialog = new ProcessDialog(mainFrame, bundle.getString("ui.dialog"), bundle);
            DirsCompareTask task = new DirsCompareTask(leftPanel.compData(), rightPanel.compData(), dialog, this::viewComparableDirs, bundle);
            task.execute();
        });

        syncDirsButton.addActionListener(event -> {
            viewFileTables(true);
            ProcessDialog dialog = new ProcessDialog(mainFrame, "Sync", bundle);
            SyncTask task = new SyncTask(leftPanel.syncData(), rightPanel.syncData(), syncModes[currentSyncMode], dialog, bundle);
            task.execute();
        });

        mainFrame = new JFrame();
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
        syncModeButton.setVisible(false);
        cancelCompModeButton.setVisible(false);
        syncDirsButton.setVisible(false);
        leftPanel.viewFileTable(isViewRootDir);
        rightPanel.viewFileTable(isViewRootDir);
    }

    private void viewComparableDirs(Result result) {
        CompResult compResult = (CompResult) result;
        compareDirsButton.setVisible(false);
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