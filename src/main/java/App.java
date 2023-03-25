import app.MainFrameControl;
import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;

public class App {
    private App() {}
    public static void main(String[] args) {
        FlatIntelliJLaf.install();
        SwingUtilities.invokeLater(MainFrameControl::new);
    }
}
