package controller.table;

import model.entity.FileEntity;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class FileTableModel extends AbstractTableModel {

    private List<FileEntity> files;
    private Column[] columns = Column.values();

    public FileTableModel() { files = new ArrayList<>(); }

    @Override
    public int getRowCount() { return files.size(); }

    @Override
    public int getColumnCount() { return columns.length; }

    @Override
    public Class<?> getColumnClass(int column) { return columns[column].getColumnClass(); }

    @Override
    public Object getValueAt(int row, int column) { return columns[column].getValueAt(files.get(row)); }

    @Override
    public String getColumnName(int column) { return columns[column].getColumnName(); }

    public void setFiles(List<FileEntity> files) {
        this.files = files;
        fireTableDataChanged();
    }
}
