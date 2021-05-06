package app.table.cloudtable;

import model.cloud.CloudInfo;

import javax.swing.*;
import java.util.ResourceBundle;

public enum CloudTableColumn {
    IMAGE {
        @Override
        public String getColumnName(ResourceBundle bundle) { return ""; }

        @Override
        public Class<?> getColumnClass() { return ImageIcon.class; }

        @Override
        public Object getValueAt(CloudInfo cloud) { return cloud.cloudType().image(); }
    },
    CLOUD_TYPE {
        @Override
        public String getColumnName(ResourceBundle bundle) { return bundle.getString("ui.table.clouds.column.cloud_type"); }

        @Override
        public Class<?> getColumnClass() { return String.class; }

        @Override
        public Object getValueAt(CloudInfo cloud) { return cloud.cloudType(); }
    },
    NAME {
        @Override
        public String getColumnName(ResourceBundle bundle) { return bundle.getString("ui.table.clouds.column.cloud_user_name"); }

        @Override
        public Class<?> getColumnClass() { return String.class; }

        @Override
        public Object getValueAt(CloudInfo cloud) { return cloud.name(); }
    };

    public abstract String getColumnName(ResourceBundle bundle);
    public abstract Class<?> getColumnClass();
    public abstract Object getValueAt(CloudInfo cloud);
}
