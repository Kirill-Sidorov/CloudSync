package app.filetable;

import model.file.FileEntity;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FileTableModel extends AbstractTableModel {

    private List<FileEntity> files;
    private final ResourceBundle bundle;
    private FileTableColumn[] columns = FileTableColumn.values();

    public FileTableModel(final ResourceBundle bundle) {
        files = new ArrayList<>();
        this.bundle = bundle;
    }

    @Override
    public int getRowCount() { return files.size(); }

    @Override
    public int getColumnCount() { return columns.length; }

    @Override
    public Class<?> getColumnClass(int column) { return columns[column].getColumnClass(); }

    @Override
    public Object getValueAt(int row, int column) { return columns[column].getValueAt(files.get(row)); }

    @Override
    public String getColumnName(int column) { return columns[column].getColumnName(bundle); }

    public FileEntity getFile(int row) { return files.get(row); }

    public void setFiles(List<FileEntity> files) {
        this.files = files;
        fireTableDataChanged();
    }
}
