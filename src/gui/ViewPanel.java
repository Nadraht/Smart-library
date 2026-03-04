package gui;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private LibraryDatabase database;
    private JButton btnViewHistory;

    public ViewPanel(LibraryDatabase database) {
        this.database = database;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new String[] { "ID", "Title", "Author", "Year", "Type", "Available" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // This makes the whole table read-only
            }
        };
        table = new JTable(tableModel);
        table.setCellSelectionEnabled(true);

        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnViewHistory = new JButton("View Item History");
        btnViewHistory.addActionListener(e -> showHistoryWindow());

        southPanel.add(btnViewHistory);
        add(southPanel, BorderLayout.SOUTH);

        // Create a popup menu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem copyItem = new JMenuItem("Copy");

        copyItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                // Get ID from the first column (index 0)
                String id = table.getValueAt(row, 0).toString();
                java.awt.datatransfer.StringSelection selection = new java.awt.datatransfer.StringSelection(id);
                java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
                JOptionPane.showMessageDialog(this, "Copied to clipboard: ");
            }
        });

        popupMenu.add(copyItem);
        table.setComponentPopupMenu(popupMenu);

        refreshTable();
    }

    private void showHistoryWindow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item from the table first!");
            return;
        }

        // Get ID from the table and find the actual Item object
        String itemId = (String) tableModel.getValueAt(selectedRow, 0);
        LibraryItem item = database.findItem(itemId);

        if (item != null) {
            // Create a small popup window for history
            JFrame historyFrame = new JFrame("Borrow History: " + item.getTitle());
            historyFrame.setSize(500, 300);
            historyFrame.setLocationRelativeTo(this);

            String[] columns = { "User ID", "Date Borrowed", "Due Date", "Date Returned" };

            // Create the model and override the 'isCellEditable' behavior
            DefaultTableModel historyModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // This tells Java that NO cells in this table can be edited
                }
            };

            // Populate from the item's history list
            for (BorrowRecord record : item.getBorrowHistory()) {
                historyModel.addRow(new Object[] {
                        // This takes the record, turns it into the file string, then extracts the ID
                        BorrowRecord.getUserIdFromLine(record.toFileString()),
                        record.getBorrowDate(),
                        record.getDueDate(),
                        record.getReturnDate() == null ? "STILL OUT" : record.getReturnDate()
                });
            }

            historyFrame.add(new JScrollPane(new JTable(historyModel)));
            historyFrame.setVisible(true);
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);

        for (LibraryItem item : database.getItems()) {
            tableModel.addRow(new Object[] {
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