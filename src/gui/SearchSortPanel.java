package gui;

import controller.SearchEngine;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SearchSortPanel extends JPanel {

    private LibraryDatabase database;
    private SearchEngine engine;
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> sortCombo;
    private JComboBox<String> fieldCombo; // Added for Title/Author/Year
    private String lastSortedField = ""; // Tracks sorting state for Binary Search

    public SearchSortPanel(LibraryDatabase database) {
        this.database = database;
        engine = new SearchEngine();

        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");

        // UI for selecting what to sort/search by
        fieldCombo = new JComboBox<>(new String[] { "Title", "Author", "Year" });
        sortCombo = new JComboBox<>(new String[] { "Selection Sort", "Merge Sort" });
        JButton sortBtn = new JButton("Sort");

        top.add(new JLabel("Search:"));
        top.add(searchField);
        top.add(new JLabel("By:"));
        top.add(fieldCombo);
        top.add(new JLabel("Algorithm:"));
        top.add(sortCombo);
        top.add(sortBtn);
        top.add(searchBtn);

        model = new DefaultTableModel(
                new String[] { "ID", "Title", "Author", "Year" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // This makes the whole table read-only
            }
        };
        table = new JTable(model);
        table.setCellSelectionEnabled(true);

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
            }
        });

        popupMenu.add(copyItem);
        table.setComponentPopupMenu(popupMenu);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> search());
        sortBtn.addActionListener(e -> sort());

        refreshTable(); // Show items on startup
    }

    private void search() {
        String query = searchField.getText().trim();
        String field = (String) fieldCombo.getSelectedItem(); // e.g., "Author"
        model.setRowCount(0);

        if (query.isEmpty()) {
            refreshTable();
            return;
        }

        // Binary Search: Best for exact matches on sorted data
        // Always use Linear Search to allow for keyword/partial matches.
        if (lastSortedField.equals(field) && query.length() > 10) {
            LibraryItem item = engine.binarySearch(database.getItems(), query, field);
            if (item != null) {
                addRowToTable(item);
                return; // Exit early if exact match found
            }
        }

        // Fallback to Linear Search for keyword matches (e.g., "Gatsby")
        List<LibraryItem> results = engine.linearSearch(database.getItems(), query, field);
        for (LibraryItem item : results) {
            addRowToTable(item);
        }

    }

    private void sort() {
        String field = (String) fieldCombo.getSelectedItem();

        if (sortCombo.getSelectedIndex() == 0)
            engine.selectionSort(database.getItems(), field);
        else
            engine.mergeSort(database.getItems(), field);

        lastSortedField = field; // Update state so search() knows list is sorted
        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (LibraryItem item : database.getItems()) {
            addRowToTable(item);
        }
    }

    private void addRowToTable(LibraryItem item) {
        model.addRow(new Object[] {
                item.getId(),
                item.getTitle(),
                item.getAuthor(),
                item.getYear()
        });
    }
}