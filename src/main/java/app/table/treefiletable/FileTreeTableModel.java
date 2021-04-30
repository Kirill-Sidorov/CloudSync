package app.table.treefiletable;

import model.entity.CompDirEntity;
import model.entity.Entity;

import java.util.ResourceBundle;

public class FileTreeTableModel extends AbstractTreeTableModel {

    private final ResourceBundle bundle;
    private final FileTreeTableColumn[] columns = FileTreeTableColumn.values();

    public FileTreeTableModel(Object root, final ResourceBundle bundle) {
        super(root);
        this.bundle = bundle;
    }

    @Override
    public boolean isLeaf(Object node) { return !((Entity)node).isDirectory(); }

    @Override
    public int getColumnCount() {return columns.length; }

    @Override
    public String getColumnName(int column) { return columns[column].getColumnName(bundle); }

    @Override
    public Class<?> getColumnClass(int column) { return columns[column].getColumnClass(); }

    @Override
    public Object getValueAt(Object node, int column) { return columns[column].getValueAt((Entity)node); }

    @Override
    public Object getChild(Object parent, int index) {
        Entity file = (Entity) parent;
        if (file.isDirectory()) {
            return ((CompDirEntity)file).files().get(index);
        } else {
            return null;
        }
    }

    @Override
    public int getChildCount(Object parent) {
        Entity file = (Entity) parent;
        if (file.isDirectory()) {
            return ((CompDirEntity)file).files().size();
        } else {
            return 0;
        }
    }
}
