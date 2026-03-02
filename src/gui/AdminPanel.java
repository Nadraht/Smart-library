package gui;

import model.*;
import utils.IDGenerator;

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
        



        add(addBtn);
        add(delBtn);
        add(undoBtn);

        addBtn.addActionListener(e -> addItem());
        delBtn.addActionListener(e -> deleteItem());
        undoBtn.addActionListener(e -> undoLastAction());
    }

    private void addItem() {
        String title = JOptionPane.showInputDialog("Enter title:");
        String author = JOptionPane.showInputDialog("Enter author:");

        if (title == null || author == null) return;

        LibraryItem item = new Book(
                IDGenerator.generateID(),
                title,
                author,
                2024,
                "N/A",
                1
        );

        database.addItem(item);
        JOptionPane.showMessageDialog(this, "Item added.");
    }

    private void deleteItem() {
        String id = JOptionPane.showInputDialog("Enter item ID:");

        for (LibraryItem item : database.getItems()) {
            if (item.getId().equals(id)) {
                undoStack.push(item);
                database.removeItem(item);
                JOptionPane.showMessageDialog(this, "Item deleted.");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Item not found.");
    }

    private void undoLastAction() {
        if (!undoStack.isEmpty()) {
            database.addItem(undoStack.pop());
            JOptionPane.showMessageDialog(this, "Undo successful.");
        } else {
            JOptionPane.showMessageDialog(this, "Nothing to undo.");
        }
    }
}