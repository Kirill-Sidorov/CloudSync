package app.dialog;

import app.logic.SyncMode;
import app.task.SyncTask;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class SyncTaskDialog extends JDialog {

    private final SyncMode[] syncModes = SyncMode.values();

    public SyncTaskDialog(final JFrame parentFrame, final String leftReadablePath, final String rightReadablePath, final ResourceBundle bundle) {
        super(parentFrame, bundle.getString("ui.dialog.create_sync_task.title"), true);
        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date());

        JLabel infoLabel = new JLabel(bundle.getString("ui.dialog.sync_task.label_info"));

        JTextField leftTextField = new JTextField(leftReadablePath);
        JTextField rightTextFiled = new JTextField(rightReadablePath);

        JButton syncModeButton = new JButton(syncModes[0].image());
        JButton createTaskButton = new JButton(bundle.getString("ui.button.set_timer_for_sync"));

        createTaskButton.addActionListener(event -> {
            Date date = (Date)timeSpinner.getValue();
            LocalTime time = LocalDateTime.ofInstant(date.toInstant(),
                    ZoneId.systemDefault()).toLocalTime();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
            calendar.set(Calendar.MINUTE, time.getMinute());
            calendar.set(Calendar.SECOND, time.getSecond());
            System.out.println(calendar.toString());
        });

        leftTextField.setFocusable(false);
        rightTextFiled.setFocusable(false);

        Container container = getContentPane();
        GroupLayout groupLayout = new GroupLayout(container);
        container.setLayout(groupLayout);

        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(infoLabel)
                .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(leftTextField)
                        .addComponent(syncModeButton)
                        .addComponent(rightTextFiled))
                .addComponent(timeSpinner)
                .addComponent(createTaskButton)
        );

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGap(10)
                .addComponent(infoLabel)
                .addGap(10)
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(leftTextField)
                        .addComponent(syncModeButton)
                        .addComponent(rightTextFiled))
                .addGap(10)
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
