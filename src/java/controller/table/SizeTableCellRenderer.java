package controller.table;

import utility.BundleHolder;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SizeTableCellRenderer extends DefaultTableCellRenderer {
    private final String format = BundleHolder.getBundle().getString("string.format.file_size");

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null) {
            value = String.format(format, value);
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
