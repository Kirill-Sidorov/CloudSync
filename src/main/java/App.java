import com.formdev.flatlaf.FlatLightLaf;
import app.MainFrameControl;

import javax.swing.*;

public class App {
    private App() {}
    public static void main(String[] args) {
        FlatLightLaf.install();
        SwingUtilities.invokeLater(MainFrameControl::new);
    }
}
