package controller.table;

import utility.BundleHolder;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTableCellRenderer extends DefaultTableCellRenderer {
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern(BundleHolder.getBundle().getString("pattern.date"));

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null) {
            value = ((LocalDateTime) value).format(format);
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
