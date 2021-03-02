package controller.table;

import model.entity.FileEntity;
import utility.BundleHolder;

import java.time.LocalDateTime;

public enum Column {
    NAME {
        @Override
        public String getColumnName() { return BundleHolder.getBundle().getString("ui.table.files.column.name"); }

        @Override
        public Class<?> getColumnClass() { return String.class; }

        @Override
        public Object getValueAt(FileEntity file) { return file.getName(); }
    },
    TYPE {
        @Override
        public String getColumnName() { return BundleHolder.getBundle().getString("ui.table.files.column.type"); }

        @Override
        public Class<?> getColumnClass() { return String.class; }

        @Override
        public Object getValueAt(FileEntity file) { return file.getTypeName(); }
    },
    DATE {
        @Override
        public String getColumnName() { return BundleHolder.getBundle().getString("ui.table.files.column.date"); }

        @Override
        public Class<?> getColumnClass() { return LocalDateTime.class; }

        @Override
        public Object getValueAt(FileEntity file) { return file.getModifiedDate(); }
    },
    SIZE {
        @Override
        public String getColumnName() { return BundleHolder.getBundle().getString("ui.table.files.column.size"); }

        @Override
        public Class<?> getColumnClass() { return Long.class; }

        @Override
        public Object getValueAt(FileEntity file) { return file.getSize(); }
    };

    public abstract String getColumnName();
    public abstract Class<?> getColumnClass();
    public abstract Object getValueAt(FileEntity file);
}
