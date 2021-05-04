package app.table.filetable;

import app.table.Column;
import model.entity.Entity;

import java.time.LocalDateTime;
import java.util.ResourceBundle;

public enum FileTableColumn implements Column {
    NAME {
        @Override
        public String getColumnName(ResourceBundle bundle) { return bundle.getString("ui.table.files.column.name"); }

        @Override
        public Class<?> getColumnClass() { return String.class; }

        @Override
        public Object getValueAt(Entity file) { return file.name(); }
    },
    TYPE {
        @Override
        public String getColumnName(ResourceBundle bundle) { return bundle.getString("ui.table.files.column.type"); }

        @Override
        public Class<?> getColumnClass() { return String.class; }

        @Override
        public Object getValueAt(Entity file) { return file.typeName(); }
    },
    DATE {
        @Override
        public String getColumnName(ResourceBundle bundle) { return bundle.getString("ui.table.files.column.date"); }

        @Override
        public Class<?> getColumnClass() { return LocalDateTime.class; }

        @Override
        public Object getValueAt(Entity file) { return file.modifiedDate(); }
    },
    SIZE {
        @Override
        public String getColumnName(ResourceBundle bundle) { return bundle.getString("ui.table.files.column.size"); }

        @Override
        public Class<?> getColumnClass() { return Long.class; }

        @Override
        public Object getValueAt(Entity file) { return file.size(); }
    }
}

