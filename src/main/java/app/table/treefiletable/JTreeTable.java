package app.table.treefiletable;

import app.table.DateTableCellRenderer;
import app.table.SizeTableCellRenderer;
import model.entity.CompDirEntity;
import model.entity.CompFileEntity;
import model.entity.Entity;
import model.entity.FileEntity;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class JTreeTable extends JTable {
    protected TreeTableCellRenderer tree;
    private final ResourceBundle bundle;

    public JTreeTable(final ResourceBundle bundle) {
        super();
        this.bundle = bundle;
        TreeTableModel emptyModel = new FileTreeTableModel(
                new CompDirEntity(
                        new FileEntity("", "", null, 0L, "", true),
                        new ArrayList<>()),
                bundle);
        tree = new TreeTableCellRenderer(emptyModel);
        setTreeTableModel(emptyModel);
        setCellSelectionEnabled(false);
        setFocusable(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int row = rowAtPoint(e.getPoint());
                Entity file = (Entity)tree.getPathForRow(row).getLastPathComponent();
                if (!file.isDirectory()) {
                    CompFileEntity compFileEntity = (CompFileEntity)file;
                    if (!compFileEntity.isNewFile()) {
                        compFileEntity.switchLastModified();
                        compFileEntity.linkedFile().switchLastModified();
                    }
                    repaint();
                }
            }
        });
    }

    public int getEditingRow() {
        return (getColumnClass(editingColumn) == TreeTableModel.class) ? -1 : editingRow;
    }

    public void setTreeTableModel(TreeTableModel treeTableModel) {
        tree = new TreeTableCellRenderer(treeTableModel);
        super.setModel(new TreeTableModelAdapter(treeTableModel, tree));
        tree.setSelectionModel(new DefaultTreeSelectionModel() {
            {
                setSelectionModel(listSelectionModel);
            }
        });
        tree.setRowHeight(getRowHeight());

        setDefaultRenderer(TreeTableModel.class, tree);
        setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());

        getColumnModel().getColumn(FileTreeTableColumn.DATE.ordinal()).setCellRenderer(new DateTableCellRenderer(bundle));
        getColumnModel().getColumn(FileTreeTableColumn.SIZE.ordinal()).setCellRenderer(new SizeTableCellRenderer(bundle));

        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    private class TreeTableCellRenderer extends JTree implements TableCellRenderer {

        protected int visibleRow;

        public TreeTableCellRenderer(TreeModel model) {
            super(model);
        }

        public void setBounds(int x, int y, int w, int h) {
            super.setBounds(x, 0, w, JTreeTable.this.getHeight());
        }

        public void paint(Graphics g) {
            g.translate(0, -visibleRow * getRowHeight());
            super.paint(g);
        }

        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row, int column) {
            visibleRow = row;
            return this;
        }
    }

    private class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int r, int c) {
            return tree;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
