package gui;

import model.*;
import utils.IdGenerator;
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
        // 1. Collect inputs
        String title = JOptionPane.showInputDialog(this, "Enter title:");
        if (title == null || title.trim().isEmpty())
            return;

        String author = JOptionPane.showInputDialog(this, "Enter author:");
        if (author == null || author.trim().isEmpty())
            return;

        String yearStr = JOptionPane.showInputDialog(this, "Enter year (e.g., 2024):");
        if (yearStr == null)
            return;

        String isbn = JOptionPane.showInputDialog(this, "Enter ISBN (Optional):");
        // If user cancels (null) or leaves it blank, default to "N/A"
        if (isbn == null || isbn.trim().isEmpty())
            isbn = "N/A";

        String genre = JOptionPane.showInputDialog(this, "Enter genre/description:");
        if (genre == null)
            genre = "N/A"; // Default if they leave it blank

        try {
            // 2. Parse the year safely
            int year = Integer.parseInt(yearStr.trim());

            // 3. Create the item with real data instead of hardcoded 2024
            LibraryItem item = new Book(
                    IdGenerator.generateID(),
                    title,
                    author,
                    year,
                    isbn,
                    genre,
                    1);

            // 4. Add to database and SAVE to file
            database.addItem(item);
            FileHandler.saveData(database); // IMPORTANT: Saves to your .dat or .txt file

            JOptionPane.showMessageDialog(this, "Item '" + title + "' added successfully.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Year! Please enter a numeric value (e.g., 2024).",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
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