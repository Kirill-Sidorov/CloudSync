import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import controller.MainFrameController;
import utility.BundleHolder;

import javax.swing.*;
import java.util.Locale;

public class Runner {
    private Runner() {}
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatLightLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        BundleHolder.setLocale(Locale.getDefault());
        SwingUtilities.invokeLater(() -> new MainFrameController());
    }
}
