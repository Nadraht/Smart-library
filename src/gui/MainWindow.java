package gui;

import model.LibraryDatabase;
import utils.FileHandler;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private LibraryDatabase database;
    private JTabbedPane tabbedPane;

    public MainWindow() {
        database = new LibraryDatabase();
        FileHandler.loadData(database);

        setTitle("Smart Library Circulation & Automation System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("View Items", new ViewPanel(database));
        tabbedPane.addTab("Borrow / Return", new BorrowPanel(database));
        tabbedPane.addTab("Admin", new AdminPanel(database));
        tabbedPane.addTab("Search & Sort", new SearchSortPanel(database));

        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}