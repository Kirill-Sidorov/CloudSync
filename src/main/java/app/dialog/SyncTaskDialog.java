package app.dialog;

import app.job.CompAndSyncJob;
import app.logic.SyncMode;
import engine.comp.CompData;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class SyncTaskDialog extends JDialog {

    private final SyncMode[] syncModes = SyncMode.values();
    private int currentSyncMode;

    public SyncTaskDialog(final JFrame parentFrame, final CompData leftData, final CompData rightData, final ResourceBundle bundle) {
        super(parentFrame, bundle.getString("ui.dialog.create_sync_task.title"), true);
        currentSyncMode = 0;
        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date());

        JLabel infoLabel = new JLabel(bundle.getString("ui.dialog.sync_task.label_info"));
        JLabel startTimeLabel = new JLabel(bundle.getString("ui.dialog.sync_task.label_task_time"));

        JTextField leftTextField = new JTextField(leftData.fileEntity().name());
        JTextField rightTextFiled = new JTextField(rightData.fileEntity().name());

        JButton syncModeButton = new JButton(syncModes[0].image());
        syncModeButton.addActionListener(e -> {
            if (syncModes.length - 1 > currentSyncMode) {
                syncModeButton.setIcon(syncModes[++currentSyncMode].image());
            } else {
                currentSyncMode = 0;
                syncModeButton.setIcon(syncModes[0].image());
            }
        });

        JButton createTaskButton = new JButton(bundle.getString("ui.button.set_timer_for_sync"));

        createTaskButton.addActionListener(event -> {
            Date date = (Date)timeSpinner.getValue();
            LocalTime time = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
            calendar.set(Calendar.MINUTE, time.getMinute());
            calendar.set(Calendar.SECOND, time.getSecond());
            if (time.isBefore(LocalTime.now())) {
                calendar.add(Calendar.DATE, 1);
            }
            java.util.Timer timer = new Timer();
            timer.schedule(new CompAndSyncJob(parentFrame, leftData, rightData, syncModes[currentSyncMode], bundle),
                    calendar.getTime(),
                    TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
            this.dispose();
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
                .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(startTimeLabel)
                        .addComponent(timeSpinner))
                .addComponent(createTaskButton)
        );

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGap(10)
                .addComponent(infoLabel)
                .addGap(10)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(leftTextField)
                        .addComponent(syncModeButton)
                        .addComponent(rightTextFiled))
                .addGap(10)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(startTimeLabel)
                        .addComponent(timeSpinner))
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
