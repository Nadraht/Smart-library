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

    public BorrowPanel(LibraryDatabase database) {
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

        gbc.gridx = 0; gbc.gridy = 0;
        add(userLabel, gbc);

        gbc.gridx = 1;
        add(userIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(itemLabel, gbc);

        gbc.gridx = 1;
        add(itemIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(borrowBtn, gbc);

        gbc.gridx = 1;
        add(returnBtn, gbc);

        addListeners();
    }

    private void addListeners() {
        borrowBtn.addActionListener(e -> {
            boolean success = controller.borrowItem(
                    userIdField.getText(),
                    itemIdField.getText()
            );

            JOptionPane.showMessageDialog(this,
                    success ? "Borrow successful!" : "Item unavailable, added to queue.");
        });

        returnBtn.addActionListener(e -> {
            boolean success = controller.returnItem(
                    userIdField.getText(),
                    itemIdField.getText()
            );

            JOptionPane.showMessageDialog(this,
                    success ? "Return successful!" : "Return failed.");
        });
    }
}