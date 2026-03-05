package gui;

import model.LibraryDatabase;
import utils.FileHandler;
import utils.OverdueReminder;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private LibraryDatabase database;
    private JTabbedPane tabbedPane;
    private JLabel statusLabel; // New field for the Status Bar

    public MainWindow() {
        database = new LibraryDatabase();
        try {
            FileHandler.loadData(database);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading items.txt! Check file formatting.");
            e.printStackTrace();
        }
        OverdueReminder.startReminder(database);

        setTitle("Smart Library Circulation & Automation System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        tabbedPane = new JTabbedPane();
        statusLabel = new JLabel(" System Ready"); // Initialize status bar
        statusLabel.setBorder(BorderFactory.createEtchedBorder());

        ViewPanel viewPanel = new ViewPanel(database);

        tabbedPane.addTab("View Items", viewPanel);
        tabbedPane.addTab("Borrow / Return", new BorrowPanel(database));
        tabbedPane.addTab("Admin", new AdminPanel(database));
        tabbedPane.addTab("Search & Sort", new SearchSortPanel(database));

        tabbedPane.addChangeListener(e -> {
            // Update the status bar text based on the selected tab
            int index = tabbedPane.getSelectedIndex();
            String tabTitle = tabbedPane.getTitleAt(index);
            statusLabel.setText(" Mode: " + tabTitle);

            if (tabbedPane.getSelectedComponent() instanceof ViewPanel) {
                ((ViewPanel) tabbedPane.getSelectedComponent()).refreshTable();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH); // Add the status bar to the bottom
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}