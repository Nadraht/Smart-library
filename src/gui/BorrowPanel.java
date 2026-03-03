package gui;

import controller.BorrowController;
import model.LibraryDatabase;

import javax.swing.*;
import java.awt.*;

public class BorrowPanel extends JPanel {

    private JTextField userIdField;
    private JTextField itemIdField;
    private JButton borrowBtn, returnBtn;

    private BorrowController controller;
    private LibraryDatabase database;

    public BorrowPanel(LibraryDatabase database) {
        this.database = database;
        controller = new BorrowController(database);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel userLabel = new JLabel("User ID:");
        JLabel itemLabel = new JLabel("Item ID:");

        userIdField = new JTextField(15);
        itemIdField = new JTextField(15);

        borrowBtn = new JButton("Borrow");
        returnBtn = new JButton("Return");

        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);

        gbc.gridx = 1;
        add(userIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(itemLabel, gbc);

        gbc.gridx = 1;
        add(itemIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(borrowBtn, gbc);

        gbc.gridx = 1;
        add(returnBtn, gbc);

        addListeners();
    }

    private void addListeners() {
        borrowBtn.addActionListener(e -> {
            String uId = userIdField.getText().trim();
            String iId = itemIdField.getText().trim();

            if (uId.isEmpty() || iId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty!");
                return;
            }

            // NEW CHECK: Specifically check if user exists first
            if (database.getUser(uId) == null) {
                JOptionPane.showMessageDialog(this, "User ID not available, register user.");
                return;
            }

            boolean success = controller.borrowItem(uId, iId);
            JOptionPane.showMessageDialog(this, success ? "Borrow successful!" : "Item unavailable, added to queue.");
        });

        returnBtn.addActionListener(e -> {
            String uId = userIdField.getText().trim();
            String iId = itemIdField.getText().trim();

            if (uId.isEmpty() || iId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty!");
                return;
            }

            // Check if user exists before attempting return
            if (database.getUser(uId) == null) {
                JOptionPane.showMessageDialog(this, "User ID not found.");
                return;
            }

            boolean success = controller.returnItem(uId, iId);
            JOptionPane.showMessageDialog(this,
                    success ? "Return successful!" : "Return failed: Check Item ID or history.");
        });
    }
}