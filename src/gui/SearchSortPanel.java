package gui;

import controller.SearchEngine;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SearchSortPanel extends JPanel {

    private LibraryDatabase database;
    private SearchEngine engine;
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> sortCombo;

    public SearchSortPanel(LibraryDatabase database) {
        this.database = database;
        engine = new SearchEngine();

        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");

        sortCombo = new JComboBox<>(new String[]{"Selection Sort", "Merge Sort"});
        JButton sortBtn = new JButton("Sort");

        top.add(new JLabel("Search Title:"));
        top.add(searchField);
        top.add(searchBtn);
        top.add(sortCombo);
        top.add(sortBtn);

        model = new DefaultTableModel(
                new String[]{"ID", "Title", "Author", "Year"}, 0
        );
        table = new JTable(model);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> search());
        sortBtn.addActionListener(e -> sort());
    }

    private void search() {
        LibraryItem item = engine.linearSearch(
                database.getItems(),
                searchField.getText()
        );

        model.setRowCount(0);

        if (item != null) {
            model.addRow(new Object[]{
                    item.getId(),
                    item.getTitle(),
                    item.getAuthor(),
                    item.getYear()
            });
        }
    }

    private void sort() {
        if (sortCombo.getSelectedIndex() == 0)
            engine.selectionSort(database.getItems());
        else
            engine.mergeSort(database.getItems());

        model.setRowCount(0);
        for (LibraryItem item : database.getItems()) {
            model.addRow(new Object[]{
                    item.getId(),
                    item.getTitle(),
                    item.getAuthor(),
                    item.getYear()
            });
        }
    }
}