package gui;

import model.*;
import utils.FileHandler;
import javax.swing.*;
import java.awt.*;
import java.util.Random;
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
        JButton reportBtn = new JButton("Generate Reports");
        JButton addUserBtn = new JButton("Register User"); // New button

        add(addBtn);
        add(delBtn);
        add(undoBtn);
        add(reportBtn);
        add(addUserBtn);

        addBtn.addActionListener(e -> addItem());
        delBtn.addActionListener(e -> deleteItem());
        undoBtn.addActionListener(e -> undoLastAction());
        reportBtn.addActionListener(e -> generateReports());
        addUserBtn.addActionListener(e -> registerUser());
    }

    private void addItem() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Item", true);

        // Create a main panel with EmptyBorder for "Edge Spacing"
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

        // Logic to Hide/Show ISBN and change labels
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

        // Logic for the OK button (remains mostly same as previous fix)
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

    private void registerUser() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Register New User", true);
        // Use a vertical BoxLayout for better spacing
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing between rows
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(15);
        JTextField idField = new JTextField(15);
        JLabel suggestionLabel = new JLabel(" "); // For ID suggestions
        suggestionLabel.setForeground(Color.BLUE);

        // Form Rows
        // Row 0: Name Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Full Name:"), gbc);

        // Row 1: Name Field
        gbc.gridy = 1;
        mainPanel.add(nameField, gbc);

        // Row 2: ID Label
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Desired User ID:"), gbc);

        // Row 3: ID Field
        gbc.gridy = 3;
        mainPanel.add(idField, gbc);

        // Row 4: Suggestions
        gbc.gridy = 4;
        mainPanel.add(suggestionLabel, gbc);

        // Row 5: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveBtn = new JButton("Register");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridy = 5;
        mainPanel.add(buttonPanel, gbc);

        // Logic
        cancelBtn.addActionListener(e -> dialog.dispose());

        saveBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();

            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields required!");
                return;
            }

            // Check if ID is taken
            if (database.getUser(id) != null) {
                String suggestion = name.toLowerCase().replaceAll("\\s+", "") + (new Random().nextInt(900) + 100);
                suggestionLabel.setText("ID taken! Try: " + suggestion);
                dialog.pack(); // Adjust size to show suggestion
                return;
            }

            database.addUser(new UserAccount(id, name));
            utils.FileHandler.saveData(database);
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Registration Successful!");
        });

        dialog.add(mainPanel);
        dialog.pack(); // Automatically sizes window perfectly based on components
        dialog.setMinimumSize(new Dimension(300, 250)); // Sets a better default size
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}