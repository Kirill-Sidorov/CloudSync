package app.table.treefiletable;

import app.table.Column;
import model.entity.CompFileEntity;
import model.entity.Entity;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public enum FileTreeTableColumn implements Column {
    ACTION {
        @Override
        public String getColumnName(ResourceBundle bundle) { return "Version"; }

        @Override
        public Class<?> getColumnClass() { return ImageIcon.class; }

        @Override
        public Object getValueAt(Entity file) {
            ImageIcon result = null;
            if (!file.isDirectory()) {
                CompFileEntity compFileEntity = (CompFileEntity)file;
                if (!compFileEntity.isNewFile()) {
                    if (compFileEntity.isLastModified()) {
                        result = new ImageIcon(getClass().getResource("/img/add.png"));
                    } else {
                        result = new ImageIcon(getClass().getResource("/img/cancel.png"));
                    }
                }
            }
            return result;
        }
    },
    NAME {
        @Override
        public String getColumnName(ResourceBundle bundle) { return bundle.getString("ui.table.files.column.name"); }

        @Override
        public Class<?> getColumnClass() { return TreeTableModel.class; }

        @Override
        public Object getValueAt(Entity file) { return file.name(); }
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
