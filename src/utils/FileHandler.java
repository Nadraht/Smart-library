package utils;

import model.*;
import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String ITEMS_FILE = "data/items.txt";
    private static final String USERS_FILE = "data/users.txt";
    private static final String BORROW_FILE = "data/borrow_records.txt";

    // SAVE DATA
    public static void saveData(LibraryDatabase db) {
        saveItems(db.getItems());
        saveUsers(db.getUsers());
        saveBorrowRecords(db.getUsers());
    }

    // LOAD DATA
    public static void loadData(LibraryDatabase db) {
        loadItems(db);
        loadUsers(db);
        loadBorrowRecords(db);
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

    private static void saveBorrowRecords(List<UserAccount> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BORROW_FILE))) {
            for (UserAccount user : users) {
                for (BorrowRecord record : user.getBorrowHistory()) {
                    writer.println(record.toFileString());
                }
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
                if (line.trim().isEmpty())
                    continue; // Skip blank lines
                String[] parts = line.split("\\|");

                try {
                    switch (parts[0]) {
                        case "BOOK":
                            db.addItem(new Book(parts[1], parts[2], parts[3],
                                    Integer.parseInt(parts[4]), parts[5], parts[6],
                                    Integer.parseInt(parts[7])));
                            break;
                        case "MAGAZINE":
                            // If your Magazine constructor requires an INT, use this:
                            int issue = parts[5].replaceAll("[^0-9]", "").isEmpty() ? 0
                                    : Integer.parseInt(parts[5].replaceAll("[^0-9]", ""));
                            db.addItem(new Magazine(parts[1], parts[2], parts[3], Integer.parseInt(parts[4]), issue));
                            break;
                        case "JOURNAL":
                            // If your Journal constructor requires an INT, use this:
                            int vol = parts[5].replaceAll("[^0-9]", "").isEmpty() ? 0
                                    : Integer.parseInt(parts[5].replaceAll("[^0-9]", ""));
                            db.addItem(new Journal(parts[1], parts[2], parts[3], Integer.parseInt(parts[4]), vol));
                            break;
                    }
                } catch (Exception e) {
                    System.err.println("Skipping bad line: " + line);
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

    private static void loadBorrowRecords(LibraryDatabase db) {
        File file = new File(BORROW_FILE);
        if (!file.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                BorrowRecord record = BorrowRecord.fromFileString(line, db);
                String userId = BorrowRecord.getUserIdFromLine(line);

                UserAccount user = db.findUser(userId);
                if (record != null && user != null) {
                    // Add to User history
                    user.addBorrowRecord(record);
                    // Add to Item history
                    record.getItem().getBorrowHistory().add(record);

                    // Update availability: if no return date, it's still out
                    boolean isReturned = record.getReturnDate() != null;
                    record.getItem().setAvailable(isReturned);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
