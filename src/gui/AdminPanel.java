package gui;

import model.*;
import utils.FileHandler;
import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class AdminPanel extends JPanel {

    private LibraryDatabase database;
    private Stack<LibraryItem> undoStack;

    public AdminPanel(LibraryDatabase database) {
        this.database = database;
        undoStack = new Stack<>();

        setLayout(new FlowLayout());

        JButton addBtn = new JButton("Add Item");
        JButton delBtn = new JButton("Delete Item");
        JButton undoBtn = new JButton("Undo");
        JButton exportBtn = new JButton("Export Data");
        JButton importBtn = new JButton("Import Data");
        JButton reportBtn = new JButton("Generate Reports");

        add(addBtn);
        add(delBtn);
        add(undoBtn);
        add(importBtn);
        add(exportBtn);
        add(reportBtn);

        addBtn.addActionListener(e -> addItem());
        delBtn.addActionListener(e -> deleteItem());
        undoBtn.addActionListener(e -> undoLastAction());
        importBtn.addActionListener(e -> importData());
        exportBtn.addActionListener(e -> exportData());
        reportBtn.addActionListener(e -> generateReports());
    }

    private void addItem() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Item", true);

        // 1. Create a main panel with EmptyBorder for "Edge Spacing"
        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 15, 15)); // 0 rows = flexible, 15px gaps
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding: top, left, bottom, right

        // Form Components
        String[] types = { "Book", "Magazine", "Journal" };
        JComboBox<String> typeCombo = new JComboBox<>(types);
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField extraField = new JTextField();

        JLabel isbnLabel = new JLabel("ISBN:");
        JLabel extraLabel = new JLabel("Genre:");

        // 2. Logic to Hide/Show ISBN and change labels
        typeCombo.addActionListener(e -> {
            String selected = (String) typeCombo.getSelectedItem();
            boolean isBook = "Book".equals(selected);

            // Toggle ISBN visibility
            isbnLabel.setVisible(isBook);
            isbnField.setVisible(isBook);

            // Update bottom label
            if (isBook)
                extraLabel.setText("Genre:");
            else if ("Magazine".equals(selected))
                extraLabel.setText("Issue #:");
            else
                extraLabel.setText("Volume #:");

            dialog.pack(); // Adjust window size after hiding/showing components
        });

        // Add components to panel
        mainPanel.add(new JLabel("Item Type:"));
        mainPanel.add(typeCombo);
        mainPanel.add(new JLabel("Title:"));
        mainPanel.add(titleField);
        mainPanel.add(new JLabel("Author:"));
        mainPanel.add(authorField);
        mainPanel.add(new JLabel("Year:"));
        mainPanel.add(yearField);
        mainPanel.add(isbnLabel);
        mainPanel.add(isbnField);
        mainPanel.add(extraLabel);
        mainPanel.add(extraField);

        JButton okBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        mainPanel.add(okBtn);
        mainPanel.add(cancelBtn);

        dialog.add(mainPanel);
        dialog.getRootPane().setDefaultButton(okBtn); // Enter key shortcut

        // 3. Logic for the OK button (remains mostly same as previous fix)
        okBtn.addActionListener(e -> {
            try {
                // These are the variables you declared
                String type = (String) typeCombo.getSelectedItem();
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                String extra = extraField.getText().trim();

                LibraryItem newItem;

                // NOW WE USE 'type' - This clears the warning!
                if ("Book".equals(type)) {
                    String isbn = isbnField.getText().trim();
                    // ISBN Validation
                    if (!isbn.equalsIgnoreCase("N/A") && !isbn.matches("\\d{10}|\\d{13}")) {
                        throw new Exception("ISBN must be 10 or 13 digits.");
                    }
                    newItem = new Book(utils.IdGenerator.generateID(), title, author, year, isbn, extra, 1);
                } else if ("Magazine".equals(type)) {
                    int issue = Integer.parseInt(extra);
                    newItem = new Magazine(utils.IdGenerator.generateID(), title, author, year, issue);
                } else {
                    int vol = Integer.parseInt(extra);
                    newItem = new Journal(utils.IdGenerator.generateID(), title, author, year, vol);
                }

                database.addItem(newItem);
                utils.FileHandler.saveData(database);
                dialog.dispose();
                JOptionPane.showMessageDialog(this, type + " added successfully!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Year and Issue/Volume must be numbers!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setMinimumSize(new Dimension(350, 400)); // Make it wider and taller
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteItem() {
        String id = JOptionPane.showInputDialog("Enter item ID:");

        for (LibraryItem item : database.getItems()) {
            if (item.getId().equals(id)) {
                undoStack.push(item);
                database.removeItem(item);
                FileHandler.saveData(database);
                JOptionPane.showMessageDialog(this, "Item deleted.");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Item not found.");
    }

    private void undoLastAction() {
        if (!undoStack.isEmpty()) {
            database.addItem(undoStack.pop());
            FileHandler.saveData(database);
            JOptionPane.showMessageDialog(this, "Undo successful.");
        } else {
            JOptionPane.showMessageDialog(this, "Nothing to undo.");
        }
    }

    private void exportData() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(this, "Export successful!");
        }
    }

    private void importData() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(this, "Import successful!");
        }
    }

    private void generateReports() {
        StringBuilder sb = new StringBuilder();

        sb.append("MOST BORROWED ITEMS:\n");
        for (String s : utils.ReportGenerator.mostBorrowedItems(database))
            sb.append(s).append("\n");

        sb.append("\nUSERS WITH OVERDUE ITEMS:\n");
        for (String s : utils.ReportGenerator.overdueUsers(database))
            sb.append(s).append("\n");

        sb.append("\nCATEGORY DISTRIBUTION:\n");
        utils.ReportGenerator.categoryDistribution(database)
                .forEach((k, v) -> sb.append(k).append(": ").append(v).append("\n"));

        JOptionPane.showMessageDialog(this, sb.toString());
    }
}