import com.formdev.flatlaf.FlatLightLaf;
import app.MainFrame;

import javax.swing.*;

public class App {
    private App() {}
    public static void main(String[] args) {
        FlatLightLaf.install();
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
