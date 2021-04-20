package app;

import app.logic.DiskSize;
import app.logic.FileClickLogic;
import app.filetable.FileTableColumn;
import app.filetable.DateTableCellRenderer;
import app.filetable.FileTableModel;
import app.filetable.SizeTableCellRenderer;
import app.task.TableUpdateTask;
import model.disk.Disk;
import model.file.FileEntity;
import model.result.DirResult;
import model.result.Error;
import model.result.PathResult;
import model.result.Result;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.ResourceBundle;

public class FilePanel {
    private JPanel filePanel;

    private JComboBox diskComboBox;
    private JProgressBar progressBarUpdateTable;
    private JLabel diskInfoLabel;
    private JTextField pathTextField;
    private JScrollPane scrollPane;
    private JTable fileTable;
    private JButton backButton;
    private JButton updateButton;

    private FileTableModel fileTableModel;
    private Disk currentDisk;
    private String currentPath;
    private String humanReadablePath;
    private TableUpdateTask updateTask;

    private final ResourceBundle bundle;
    private final Map<String, Disk> drives;

    public FilePanel(final ResourceBundle bundle, final Map<String, Disk> drives) {
        this.bundle = bundle;
        this.drives = drives;
        initView();
        initListeners();
        currentDisk = drives.get((String) diskComboBox.getSelectedItem());
        currentPath = currentDisk.rootPath();
        humanReadablePath = currentDisk.name();
        updateTable();
    }

    private void initView() {
        filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));

        diskComboBox = new JComboBox();
        diskComboBox.setMaximumSize(new Dimension(30, 20));
        drives.keySet().forEach(diskComboBox::addItem);
        progressBarUpdateTable = new JProgressBar();
        progressBarUpdateTable.setVisible(false);
        diskInfoLabel = new JLabel();
        diskInfoLabel.setVerticalAlignment(SwingConstants.CENTER);
        backButton = new JButton(new ImageIcon(getClass().getResource("/img/back-button.png")));
        backButton.setEnabled(false);
        updateButton = new JButton(new ImageIcon(getClass().getResource("/img/refresh-button.png")));
        pathTextField = new JTextField();
        fileTableModel = new FileTableModel(bundle);
        fileTable = new JTable(fileTableModel);
        fileTable.getColumnModel().getColumn(FileTableColumn.DATE.ordinal()).setCellRenderer(new DateTableCellRenderer(bundle));
        fileTable.getColumnModel().getColumn(FileTableColumn.SIZE.ordinal()).setCellRenderer(new SizeTableCellRenderer(bundle));
        fileTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        fileTable.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                fileTable.clearSelection();
            }
        });

        fileTable.setAutoCreateRowSorter(true);
        scrollPane = new JScrollPane(fileTable);

        JPanel controlPanel = new JPanel();
        GroupLayout layout = new GroupLayout(controlPanel);
        controlPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        hGroup.addGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(diskComboBox)
                        .addComponent(updateButton)
                        .addComponent(diskInfoLabel))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(backButton)
                        .addComponent(pathTextField)));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(diskComboBox)
                .addComponent(updateButton)
                .addComponent(diskInfoLabel));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(backButton)
                .addComponent(pathTextField));
        layout.setVerticalGroup(vGroup);

        filePanel.add(controlPanel);
        filePanel.add(progressBarUpdateTable);
        filePanel.add(scrollPane);
    }

    private void initListeners() {
        updateButton.addActionListener(event -> updateTable());

        diskComboBox.addActionListener(event -> {
            Object drive = diskComboBox.getSelectedItem();
            if (drive != null) {
                currentDisk = drives.get((String) diskComboBox.getSelectedItem());
                humanReadablePath = currentDisk.name();
                currentPath = currentDisk.rootPath();
                updateTable();
            }
        });

        backButton.addActionListener(event -> processResult(currentDisk.previousDirPath(currentPath, humanReadablePath)));

        fileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = fileTable.rowAtPoint(e.getPoint());
                    FileEntity file = fileTableModel.getFile(row);
                    processResult(new FileClickLogic(file, currentDisk, humanReadablePath).perform());
                }
            }
        });
    }

    private void processResult(Result result) {
        switch (result.status()) {
            case NEED_UPDATE_TABLE:
                currentPath = ((PathResult) result).path();
                humanReadablePath = ((PathResult) result).humanReadablePath();
                updateTable();
                break;
            case NEED_UPDATE_VIEW_PANEL:
                processUpdateViewPanel((DirResult) result);
                break;
            case ERROR:
                processError(result.error());
                break;
        }
    }

    private void processUpdateViewPanel(DirResult result) {
        fileTableModel.setFiles(result.files());
        diskInfoLabel.setText(String.format(bundle.getString("string.format.drive_size_info"),
                new DiskSize(result.totalSpace()).convert(),
                new DiskSize(result.unallocatedSpace()).convert()));
        if (!currentPath.equals(currentDisk.rootPath())) {
            backButton.setEnabled(true);
        }
        if (result.error() != Error.NO) {
            processError(result.error());
        }
    }

    private void processError(Error error) {
        System.out.println(error.getMessage(bundle));
        //JOptionPane.showMessageDialog(component, text, BundleHolder.getBundle().getString("message.title.error"), JOptionPane.ERROR_MESSAGE);
    }

    public void updateTable() {
        if (updateTask == null || updateTask.isDone()) {
            backButton.setEnabled(false);
            pathTextField.setText(humanReadablePath);
            updateTask = new TableUpdateTask(currentDisk,
                    currentPath,
                    progressBarUpdateTable,
                    this::processResult);
            updateTask.execute();
        }
    }

    public void updateDiskComboBox() {
        int index = diskComboBox.getSelectedIndex();
        diskComboBox.removeAllItems();
        drives.keySet().forEach(diskComboBox::addItem);
        if (diskComboBox.getItemCount() > index) {
            diskComboBox.setSelectedIndex(index);
        } else {
            diskComboBox.setSelectedIndex(diskComboBox.getItemCount() - 1);
        }
    }

    public JPanel getMainJPanel() {
        return filePanel;
    }

    public String getCurrentPath() { return currentPath; }
}
