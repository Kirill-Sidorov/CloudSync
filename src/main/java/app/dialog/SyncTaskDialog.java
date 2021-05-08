package app.dialog;

import app.logic.SyncMode;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.ResourceBundle;

public class SyncTaskDialog extends JDialog {

    private final SyncMode[] syncModes = SyncMode.values();

    public SyncTaskDialog(final JFrame parentFrame, final ResourceBundle bundle) {
        super(parentFrame, bundle.getString("ui.dialog.create_sync_task.title"), true);
        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date());

        JTextField leftTextField = new JTextField();
        JTextField rightTextFiled = new JTextField();

        JButton syncModeButton = new JButton(syncModes[0].image());
        JButton createTaskButton = new JButton(bundle.getString("ui.button.set_timer_for_sync"));

        leftTextField.setFocusable(false);
        rightTextFiled.setFocusable(false);

        Container container = getContentPane();
        GroupLayout groupLayout = new GroupLayout(container);
        container.setLayout(groupLayout);

        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(leftTextField)
                        .addComponent(syncModeButton)
                        .addComponent(rightTextFiled))
                .addComponent(timeSpinner)
                .addComponent(createTaskButton)
        );

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGap(30)
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(leftTextField)
                        .addComponent(syncModeButton)
                        .addComponent(rightTextFiled))
                .addGap(20)
                .addComponent(timeSpinner)
                .addGap(10)
                .addComponent(createTaskButton)
                .addGap(10)
        );

        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(400, 180);
        setLocationRelativeTo(getOwner());
    }
}
