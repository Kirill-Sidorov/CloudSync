package app.table.cloudtable;

import model.cloud.CloudInfo;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ResourceBundle;

public class CloudTableModel extends AbstractTableModel {

    private final ResourceBundle bundle;
    private List<CloudInfo> clouds;
    private CloudTableColumn[] columns = CloudTableColumn.values();

    public CloudTableModel(final ResourceBundle bundle, List<CloudInfo> clouds) {
        this.bundle = bundle;
        this.clouds = clouds;
    }

    @Override
    public int getRowCount() { return clouds.size(); }

    @Override
    public int getColumnCount() { return columns.length; }

    @Override
    public Object getValueAt(int row, int column) { return columns[column].getValueAt(clouds.get(row)); }

    @Override
    public String getColumnName(int column) { return columns[column].getColumnName(bundle); }

    @Override
    public Class<?> getColumnClass(int column) { return columns[column].getColumnClass(); }

    public CloudInfo removeCloud(int row) {
        CloudInfo removedCloud = clouds.remove(row);
        fireTableDataChanged();
        return removedCloud;
    }
}
