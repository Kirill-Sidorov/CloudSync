package app;

import javax.swing.*;
import java.util.ResourceBundle;

public class CloudManagerDialog {

    private final ResourceBundle bundle;

    public CloudManagerDialog(final JFrame parentFrame, final ResourceBundle bundle) {
        this.bundle = bundle;
        JDialog dialog = new JDialog(parentFrame, bundle.getString("ui.menu_bar.menu_item.cloud_manager"), true);
        dialog.getContentPane().add(new JLabel("label"));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setSize(600, 300);
        dialog.setVisible(true);
    }
}
