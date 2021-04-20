package app;

import app.task.CloudDrivesConnectTask;
import drive.local.LocalFS;
import model.cloud.CloudInfo;
import model.disk.Disk;
import model.result.CloudsConnectResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class MainFrame {

    private JFrame mainFrame;
    private JMenuBar menuBar;
    private JMenu programMenu;
    private JMenuItem cloudManagerMenu;

    private JButton compareDirsButton;

    private JSplitPane splitPane;
    private JPanel syncControlPanel;
    private FilePanel leftPanel;
    private FilePanel rightPanel;

    private ResourceBundle bundle;
    private Map<String, Disk> drives;
    private Map<String, CloudInfo> cloudsInfo;

    public MainFrame() {
        bundle = ResourceBundle.getBundle("bundle.strings", Locale.getDefault());
        initDrives();
        initView();
        initListeners();
    }

    private void initDrives() {
        drives = new LocalFS().drives();
        cloudsInfo = new HashMap<>();
        new CloudDrivesConnectTask(result -> {
            CloudsConnectResult cloudsConnectResult = (CloudsConnectResult) result;
            drives.putAll(cloudsConnectResult.cloudDrives());
            cloudsInfo.putAll(cloudsConnectResult.cloudsInfo());
            updateComboBoxes();
        }).execute();
    }

    private void initView() {
        menuBar = new JMenuBar();
        programMenu = new JMenu(bundle.getString("ui.menu_bar.menu.app"));
        cloudManagerMenu = new JMenuItem(bundle.getString("ui.menu_bar.menu_item.cloud_manager"));
        programMenu.add(cloudManagerMenu);
        menuBar.add(programMenu);

        leftPanel = new FilePanel(bundle, drives);
        rightPanel = new FilePanel(bundle, drives);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel.getMainJPanel(), rightPanel.getMainJPanel());
        splitPane.setResizeWeight(0.5);

        syncControlPanel = new JPanel();
        compareDirsButton = new JButton("Compare dirs");

        syncControlPanel.add(compareDirsButton);

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
    }

    private void updateComboBoxes() {
        leftPanel.updateDiskComboBox();
        rightPanel.updateDiskComboBox();
    }
}