package app.dialog;

import app.table.cloudtable.CloudTableModel;
import drive.google.GoogleAuth;
import model.cloud.CloudInfo;
import model.cloud.CloudType;
import model.disk.Disk;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class CloudManagerDialog extends JDialog {

    public CloudManagerDialog(final JFrame parentFrame, final ResourceBundle bundle, Map<String, Disk> drives, Map<String, CloudInfo> cloudsInfo) {
        super(parentFrame, bundle.getString("ui.menu_bar.menu_item.cloud_manager"), true);

        CloudTableModel cloudTableModel = new CloudTableModel(bundle, new ArrayList<>(cloudsInfo.values()));
        JTable cloudTable = new JTable(cloudTableModel);

        cloudTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cloudTable.setAutoCreateRowSorter(false);
        JScrollPane scrollPane = new JScrollPane(cloudTable);

        JButton addGoogleButton = new JButton(CloudType.GOOGLE.image());
        JButton addDropboxButton = new JButton(CloudType.DROPBOX.image());

        addGoogleButton.setText(bundle.getString("ui.button.add_drive"));
        addDropboxButton.setText(bundle.getString("ui.button.add_drive"));

        addGoogleButton.setToolTipText(bundle.getString("ui.button.tool_tip.add_google_drive"));
        addDropboxButton.setToolTipText(bundle.getString("ui.button.tool_tip.add_dropbox_drive"));

        addGoogleButton.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                cloudTable.clearSelection();
            }
        });

        addGoogleButton.addActionListener(event -> {
            Disk googleDisk = new GoogleAuth(CloudType.GOOGLE.tokensDir()).authorize();
            drives.put(googleDisk.getName(), googleDisk);
            cloudsInfo.put(googleDisk.getName(), new CloudInfo(CloudType.GOOGLE, googleDisk.getName(), CloudType.GOOGLE.tokensDir()));
        });

        addDropboxButton.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                cloudTable.clearSelection();
            }
        });

        JButton removeButton = new JButton(bundle.getString("ui.button.delete"));

        removeButton.addActionListener(event -> {
            int row = cloudTable.getSelectedRow();
            if (row != -1) {
                CloudInfo cloudInfo = cloudTableModel.removeCloud(row);
                cloudsInfo.remove(cloudInfo.name());
                drives.remove(cloudInfo.name());
            }
        });

        JPanel mainPanel = new JPanel();
        GroupLayout layout = new GroupLayout(mainPanel);
        mainPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        hGroup.addComponent(scrollPane)
                .addGroup(layout.createParallelGroup()
                        .addComponent(addGoogleButton)
                        .addComponent(addDropboxButton)
                        .addComponent(removeButton));
        layout.setHorizontalGroup(hGroup);

        layout.linkSize(SwingConstants.HORIZONTAL, addGoogleButton, removeButton);

        GroupLayout.ParallelGroup vGroup = layout.createParallelGroup();
        vGroup.addComponent(scrollPane)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(addGoogleButton)
                        .addComponent(addDropboxButton)
                        .addComponent(removeButton));
        layout.setVerticalGroup(vGroup);

        getContentPane().add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setSize(600, 300);
    }
}
