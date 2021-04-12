package app.filetable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ResourceBundle;

public class SizeTableCellRenderer extends DefaultTableCellRenderer {
    private String format;

    public SizeTableCellRenderer(ResourceBundle bundle) {
        this.format = bundle.getString("string.format.file_size");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null) {
            value = String.format(format, value);
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
