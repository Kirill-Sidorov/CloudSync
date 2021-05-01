package app;

import app.logic.DiskSize;
import app.table.filetable.FileTableColumn;
import app.table.DateTableCellRenderer;
import app.table.filetable.FileTableModel;
import app.table.SizeTableCellRenderer;
import app.table.treefiletable.FileTreeTableModel;
import app.table.treefiletable.JTreeTable;
import app.task.TableUpdateTask;
import engine.CompData;
import model.disk.Disk;
import model.entity.CompDirEntity;
import model.entity.Entity;
import model.result.DirResult;
import model.result.Error;
import model.result.Result;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Stack;

public class FilePanelControl {

    private final String FILE_TABLE = "file_table";
    private final String TREE_FILE_TABLE = "tree_file_table";

    private JPanel filePanel;

    private JComboBox diskComboBox;
    private JProgressBar progressBarUpdateTable;
    private JLabel diskInfoLabel;
    private JTextField pathTextField;
    private JPanel cardsPanel;
    private JTable fileTable;
    private JTreeTable treeFileTable;
    private JButton backButton;
    private JButton updateButton;

    private FileTableModel fileTableModel;
    private Disk currentDisk;
    private String humanReadablePath;
    private TableUpdateTask updateTask;

    private final ResourceBundle bundle;
    private final Map<String, Disk> drives;
    private final Stack<Entity> dirs = new Stack<>();;

    public FilePanelControl(final ResourceBundle bundle, final Map<String, Disk> drives, final JTreeTable treeFileTable) {
        this.bundle = bundle;
        this.drives = drives;
        this.treeFileTable = treeFileTable;

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
        pathTextField.setFocusable(false);
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
        JScrollPane scrollFileTable = new JScrollPane(fileTable);

        JScrollPane scrollTreeFileTable = new JScrollPane(treeFileTable);

        cardsPanel = new JPanel(new CardLayout());
        cardsPanel.add(scrollFileTable, FILE_TABLE);
        cardsPanel.add(scrollTreeFileTable, TREE_FILE_TABLE);

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
        filePanel.add(cardsPanel);

        updateButton.addActionListener(event -> updateFileTable());

        diskComboBox.addActionListener(event -> {
            Object drive = diskComboBox.getSelectedItem();
            if (drive != null) {
                currentDisk = drives.get((String) diskComboBox.getSelectedItem());
                humanReadablePath = currentDisk.name();
                dirs.clear();
                dirs.push(currentDisk.rootFile());
                updateFileTable();
            }
        });

        backButton.addActionListener(event -> {
            dirs.pop();
            int index = humanReadablePath.lastIndexOf("\\");
            humanReadablePath = (index == -1) ? humanReadablePath : humanReadablePath.substring(0, index);
            updateFileTable();
        });

        // not clicked when comparison mode
        fileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = fileTable.getSelectedRow();
                    if (row != -1) {
                        row = fileTable.convertRowIndexToModel(row);
                        Entity file = fileTableModel.getFile(row);
                        if (file.isDirectory()) {
                            dirs.push(file);
                            humanReadablePath = humanReadablePath + "\\" + file.name();
                            updateFileTable();
                        } else {
                            processResult(currentDisk.execute(file));
                        }
                    }
                }
            }
        });

        currentDisk = drives.get((String) diskComboBox.getSelectedItem());
        dirs.push(currentDisk.rootFile());
        humanReadablePath = currentDisk.name();
        updateFileTable();
    }

    private void processResult(Result result) {
        progressBarUpdateTable.setVisible(false);
        progressBarUpdateTable.setValue(0);
        switch (result.status()) {
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
        if (dirs.size() > 1) {
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

    private void updateFileTable() {
        if (updateTask == null || updateTask.isDone()) {
            progressBarUpdateTable.setVisible(true);
            backButton.setEnabled(false);
            pathTextField.setText(humanReadablePath);
            updateTask = new TableUpdateTask(currentDisk, dirs.peek(), this::processResult);
            updateTask.addPropertyChangeListener(event -> {
                if ("progress".equals(event.getPropertyName())) {
                    progressBarUpdateTable.setValue((Integer)event.getNewValue());
                }
            });
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

    public void viewComparableDir(CompDirEntity dir) {
        diskComboBox.setEnabled(false);
        updateButton.setEnabled(false);
        backButton.setEnabled(false);
        treeFileTable.setTreeTableModel(new FileTreeTableModel(dir, bundle));
        CardLayout cardLayout = (CardLayout) cardsPanel.getLayout();
        cardLayout.show(cardsPanel, TREE_FILE_TABLE);
    }

    public void viewFileTable() {
        diskComboBox.setEnabled(true);
        updateButton.setEnabled(true);
        backButton.setEnabled(true);
        CardLayout cardLayout = (CardLayout) cardsPanel.getLayout();
        cardLayout.show(cardsPanel, FILE_TABLE);
    }

    public JPanel mainJPanel() { return filePanel; }

    public CompData compData() { return new CompData(currentDisk, dirs.peek()); }
}
