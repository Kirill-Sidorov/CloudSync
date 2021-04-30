package app;

import app.dialog.CloudManagerDialog;
import app.dialog.CompProcessDialog;
import app.table.treefiletable.JTreeTable;
import app.task.DirsCompareTask;
import drive.local.LocalFS;
import model.cloud.CloudInfo;
import model.disk.Disk;
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

    private JSplitPane splitPane;
    private JPanel syncControlPanel;
    private FilePanelControl leftPanel;
    private FilePanelControl rightPanel;

    private ImageIcon[] syncModeImages = new ImageIcon[] {new ImageIcon(getClass().getResource("/img/all-sync.png")),
                                                          new ImageIcon(getClass().getResource("/img/right-sync.png"))};

    private ResourceBundle bundle;
    private Map<String, Disk> drives;
    private Map<String, CloudInfo> cloudsInfo;

    private boolean temp = false;

    public MainFrameControl() {
        bundle = ResourceBundle.getBundle("bundle.strings", Locale.getDefault());
        initDrives();
        initView();
        initListeners();
    }

    private void initDrives() {
        drives = new LocalFS().drives();
        cloudsInfo = new HashMap<>();
        /*
        new CloudDrivesConnectTask(result -> {
            CloudsConnectResult cloudsConnectResult = (CloudsConnectResult) result;
            drives.putAll(cloudsConnectResult.cloudDrives());
            cloudsInfo.putAll(cloudsConnectResult.cloudsInfo());
            updateComboBoxes();
        }).execute();
         */
    }

    private void initView() {
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
        compareDirsButton = new JButton("Compare dirs");
        syncModeButton = new JButton(syncModeImages[0]);
        cancelCompModeButton = new JButton("cancelCompModeButton");

        syncModeButton.addActionListener(e -> {
            if (temp) {
                temp = false;
                syncModeButton.setIcon(syncModeImages[0]);
            } else {
                temp = true;
                syncModeButton.setIcon(syncModeImages[1]);
            }
        });
        cancelCompModeButton.addActionListener(event -> viewFileTables());

        syncModeButton.setVisible(false);
        cancelCompModeButton.setVisible(false);

        syncControlPanel.add(compareDirsButton);
        syncControlPanel.add(syncModeButton);
        syncControlPanel.add(cancelCompModeButton);

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

    private void initListeners() {
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
            CompProcessDialog dialog = new CompProcessDialog(mainFrame, bundle);
            DirsCompareTask task = new DirsCompareTask(leftPanel.compData(), rightPanel.compData(), dialog, this::viewComparableDirs, bundle);
            dialog.setVisible(true);
            task.execute();
        });
    }

    private void viewFileTables() {
        compareDirsButton.setVisible(true);
        syncModeButton.setVisible(false);
        cancelCompModeButton.setVisible(false);
        leftPanel.viewFileTable();
        rightPanel.viewFileTable();
    }

    private void viewComparableDirs(Result result) {
        CompResult compResult = (CompResult) result;
        compareDirsButton.setVisible(false);
        syncModeButton.setVisible(true);
        cancelCompModeButton.setVisible(true);
        leftPanel.viewComparableDir(compResult.leftDir());
        rightPanel.viewComparableDir(compResult.rightDir());
    }

    private void updateComboBoxes() {
        leftPanel.updateDiskComboBox();
        rightPanel.updateDiskComboBox();
    }
}