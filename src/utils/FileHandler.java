package utils;

import model.*;
import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String ITEMS_FILE = "data/items.txt";
    private static final String USERS_FILE = "data/users.txt";

    // SAVE DATA
    public static void saveData(LibraryDatabase db) {
        saveItems(db.getItems());
        saveUsers(db.getUsers());
    }

    // LOAD DATA
    public static void loadData(LibraryDatabase db) {
        loadItems(db);
        loadUsers(db);
    }

    // ---------------- SAVE METHODS ----------------

    private static void saveItems(List<LibraryItem> items) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ITEMS_FILE))) {
            for (LibraryItem item : items) {

                if (item instanceof Book) {
                    Book b = (Book) item;
                    writer.println("BOOK|" + b.getId() + "|" + b.getTitle() + "|" +
                            b.getAuthor() + "|" + b.getYear() + "|" +
                            b.getIsbn() + "|" + b.getGenre() + "|" + b.getCopies());
                }

                else if (item instanceof Magazine) {
                    Magazine m = (Magazine) item;
                    writer.println("MAGAZINE|" + m.getId() + "|" + m.getTitle() + "|" +
                            m.getAuthor() + "|" + m.getYear() + "|" +
                            m.getIssueNumber());
                }

                else if (item instanceof Journal) {
                    Journal j = (Journal) item;
                    writer.println("JOURNAL|" + j.getId() + "|" + j.getTitle() + "|" +
                            j.getAuthor() + "|" + j.getYear() + "|" +
                            j.getVolume());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveUsers(List<UserAccount> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (UserAccount user : users) {
                writer.println(user.getUserId() + "|" + user.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------- LOAD METHODS ----------------

    private static void loadItems(LibraryDatabase db) {
        File file = new File(ITEMS_FILE);
        if (!file.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");

                switch (parts[0]) {
                    case "BOOK":
                        db.addItem(new Book(
                                parts[1], parts[2], parts[3],
                                Integer.parseInt(parts[4]),
                                parts[5], parts[6],
                                Integer.parseInt(parts[7])));
                        break;

                    case "MAGAZINE":
                        db.addItem(new Magazine(
                                parts[1], parts[2], parts[3],
                                Integer.parseInt(parts[4]),
                                Integer.parseInt(parts[5])));
                        break;

                    case "JOURNAL":
                        db.addItem(new Journal(
                                parts[1], parts[2], parts[3],
                                Integer.parseInt(parts[4]),
                                Integer.parseInt(parts[5])));
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadUsers(LibraryDatabase db) {
        File file = new File(USERS_FILE);
        if (!file.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                db.addUser(new UserAccount(parts[0], parts[1]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
