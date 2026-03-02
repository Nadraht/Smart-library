package gui;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private LibraryDatabase database;

    public ViewPanel(LibraryDatabase database) {
        this.database = database;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Author", "Year", "Type", "Available"}, 0
        );

        table = new JTable(tableModel);
        refreshTable();

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);

        for (LibraryItem item : database.getItems()) {
            tableModel.addRow(new Object[]{
                    item.getId(),
                    item.getTitle(),
                    item.getAuthor(),
                    item.getYear(),
                    item.getClass().getSimpleName(),
                    item.isAvailable()
            });
        }
    }
}