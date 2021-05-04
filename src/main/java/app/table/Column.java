package app.table;

import model.entity.Entity;

import java.util.ResourceBundle;

public interface Column {
    String getColumnName(final ResourceBundle bundle);
    Class<?> getColumnClass();
    Object getValueAt(final Entity file);
}
