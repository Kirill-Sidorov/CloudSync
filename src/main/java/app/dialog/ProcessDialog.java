package app.dialog;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class ProcessDialog extends JDialog {

    private final JTextField infoTextField;
    private final JProgressBar progressBar;
    private final JButton cancelButton;

    public ProcessDialog(final JFrame parentFrame, final String title, final String processMessage, final ResourceBundle bundle) {
        super(parentFrame, title, false);
        JLabel processMessageLabel = new JLabel(processMessage);
        infoTextField = new JTextField();
        infoTextField.setFocusable(false);
        progressBar = new JProgressBar();
        cancelButton = new JButton(bundle.getString("ui.button.cancel"));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(processMessageLabel);
        panel.add(infoTextField);

        Container container = getContentPane();
        GroupLayout groupLayout = new GroupLayout(container);
        container.setLayout(groupLayout);

        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(panel)
                .addComponent(progressBar)
                .addComponent(cancelButton)
                .addGap(200)
        );

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGap(30)
                .addComponent(panel)
                .addGap(20)
                .addComponent(progressBar)
                .addGap(40)
                .addComponent(cancelButton)
                .addGap(10)
        );

        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(400, 180);
        setLocationRelativeTo(getOwner());
    }

    public JTextField infoTextField() { return infoTextField; }
    public JProgressBar progressBar() { return progressBar; }
    public JButton cancelButton() { return cancelButton; }
}
