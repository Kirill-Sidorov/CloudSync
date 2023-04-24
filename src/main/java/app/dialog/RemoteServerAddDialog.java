package app.dialog;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class RemoteServerAddDialog extends JDialog {

    public RemoteServerAddDialog(final Frame parentFrame, final String title, final ResourceBundle bundle) {
        super(parentFrame, bundle.getString("ui.dialog.remote_server_add.title"), true);

        JLabel infoLabel = new JLabel(bundle.getString("ui.dialog.remote_server_add.label_info"));
        JTextField hostNameField = new JTextField(20);
        JTextField portNameField = new JTextField(20);
        JLabel delimiterLabel = new JLabel(":");
        JButton addRemoteServerButton = new JButton(bundle.getString("ui.dialog.remote_server_add.button_add"));
        JButton cancelButton = new JButton(bundle.getString("ui.dialog.remote_server_add.button_cancel"));
        JLabel errorLabel = new JLabel(bundle.getString("ui.dialog.remote_server_add.label_error_connect"));
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);

        addRemoteServerButton.addActionListener(event -> {
            errorLabel.setVisible(true);
        });

        cancelButton.addActionListener(event -> {
            this.dispose();
        });

        Container container = getContentPane();
        GroupLayout groupLayout = new GroupLayout(container);
        container.setLayout(groupLayout);

        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(infoLabel)
                .addComponent(errorLabel)
                .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(hostNameField)
                        .addComponent(delimiterLabel)
                        .addComponent(portNameField))
                .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(addRemoteServerButton)
                        .addComponent(cancelButton))
        );

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGap(10)
                .addComponent(infoLabel)
                .addComponent(errorLabel)
                .addGap(10)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(hostNameField)
                        .addComponent(delimiterLabel)
                        .addComponent(portNameField))
                .addGap(10)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addRemoteServerButton)
                        .addComponent(cancelButton))
                .addGap(10)
        );

        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(400, 160);
        setLocationRelativeTo(getOwner());
    }


}
