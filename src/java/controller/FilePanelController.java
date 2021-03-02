package controller;

import controller.table.Column;
import controller.table.DateTableCellRenderer;
import controller.table.FileTableModel;
import controller.table.SizeTableCellRenderer;
import drive.local.LocalDriveManager;
import model.entity.DriveEntity;
import model.result.DataResult;
import utility.BundleHolder;

import javax.swing.*;
import java.awt.*;

public class FilePanelController extends JPanel {
    private JComboBox diskComboBox;
    private JProgressBar progressBarUpdateTable;
    private JLabel diskInfoLabel;
    private JTextField pathTextField;
    private JScrollPane scrollPane;
    private JTable fileTable;
    private JButton backButton;
    private JButton updateButton;

    private FileTableModel fileTableModel;
    private DriveEntity currentDrive;
    private String rootDirectory;

    public FilePanelController() {
        initView();
        getFiles();
    }

    private void initView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        diskComboBox = new JComboBox();
        diskComboBox.setMaximumSize(new Dimension(30, 20));
        progressBarUpdateTable = new JProgressBar();
        progressBarUpdateTable.setVisible(false);
        diskInfoLabel = new JLabel();
        diskInfoLabel.setVerticalAlignment(SwingConstants.CENTER);
        backButton = new JButton(new ImageIcon("src/resources/img/back-button.png"));
        updateButton = new JButton(new ImageIcon("src/resources/img/refresh-button.png"));
        backButton.setBorderPainted(false);
        updateButton.setBorderPainted(false);
        pathTextField = new JTextField();
        pathTextField.setEnabled(false);
        fileTableModel = new FileTableModel();
        fileTable = new JTable(fileTableModel);
        fileTable.getColumnModel().getColumn(Column.DATE.ordinal()).setCellRenderer(new DateTableCellRenderer());
        fileTable.getColumnModel().getColumn(Column.SIZE.ordinal()).setCellRenderer(new SizeTableCellRenderer());
        fileTable.setAutoCreateRowSorter(true);
        scrollPane = new JScrollPane(fileTable);

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        hGroup.addGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addComponent(diskComboBox)
                    .addComponent(diskInfoLabel))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(backButton)
                        .addComponent(pathTextField)
                        .addComponent(updateButton)));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(diskComboBox)
                .addComponent(diskInfoLabel));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(backButton)
                .addComponent(pathTextField)
                .addComponent(updateButton));
        layout.setVerticalGroup(vGroup);

        add(panel);
        add(scrollPane);
        add(progressBarUpdateTable);
    }

    private void getFiles() {
        DataResult files = LocalDriveManager.getListDirectoryFiles("C:\\", (a, b) -> {});
        fileTableModel.setFiles(files.getFiles());
        diskInfoLabel.setText(String.format(BundleHolder.getBundle().getString("string.format.drive_size_info"), files.getTotalSpace(), files.getUnallocatedSpace()));
    }
}
