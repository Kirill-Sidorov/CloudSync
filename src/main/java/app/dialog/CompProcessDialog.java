package app.dialog;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class CompProcessDialog extends JDialog {

    private final JLabel infoLabel;
    private final JProgressBar progressBar;
    private final JButton cancelButton;

    public CompProcessDialog(final JFrame parentFrame, final ResourceBundle bundle) {
        super(parentFrame, bundle.getString("ui.dialog"), false);
        infoLabel = new JLabel();
        progressBar = new JProgressBar();
        cancelButton = new JButton(bundle.getString("ui.button.cancel"));

        Container container = getContentPane();
        GroupLayout groupLayout = new GroupLayout(container);
        container.setLayout(groupLayout);

        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(infoLabel)
                .addComponent(progressBar)
                .addComponent(cancelButton)
                .addGap(200)
        );

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGap(30)
                .addComponent(infoLabel)
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

    public JLabel infoLabel() { return infoLabel; }
    public JProgressBar progressBar() { return progressBar; }
    public JButton cancelButton() { return cancelButton; }
}
