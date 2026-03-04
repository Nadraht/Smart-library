package controller;

import model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class BorrowController {
    private LibraryDatabase database;

    public BorrowController(LibraryDatabase database) {
        this.database = database;
    }

    public boolean borrowItem(String userId, String itemId) {
        UserAccount user = findUser(userId);
        LibraryItem item = findItem(itemId);

        if (user == null || item == null)
            return false;

        if (item.isAvailable()) {
            item.setAvailable(false);

            Calendar cal = Calendar.getInstance();
            Date borrowDate = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 14);
            Date dueDate = cal.getTime();

            // FIX: Pass userId to the constructor (Ensure you updated BorrowRecord model
            // too)
            BorrowRecord record = new BorrowRecord(userId, item, borrowDate, dueDate);

            user.addBorrowRecord(record);
            item.getBorrowHistory().add(record); // Adds to item history
            saveAllRecords();

            return true;
        } else {
            database.getReservationQueue().offer(new Reservation(userId, itemId));
            return false;
        }
    }

    public boolean returnItem(String userId, String itemId) {

        UserAccount user = findUser(userId);
        LibraryItem item = findItem(itemId);

        if (user == null || item == null)
            return false;

        for (BorrowRecord record : user.getBorrowHistory()) {

            if (record.getItem().getId().equals(itemId)
                    && record.getReturnDate() == null) {

                record.setReturnDate(new Date());

                processReservationQueue(item);
                saveAllRecords();
                return true;
            }
        }

        return false;
    }

    private void processReservationQueue(LibraryItem returnedItem) {
        Iterator<Reservation> iterator = database.getReservationQueue().iterator();

        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();

            if (reservation.getItemId().equals(returnedItem.getId())) {
                UserAccount nextUser = findUser(reservation.getUserId());

                if (nextUser != null) {
                    returnedItem.setAvailable(false);

                    Calendar cal = Calendar.getInstance();
                    Date borrowDate = cal.getTime();
                    cal.add(Calendar.DAY_OF_MONTH, 14);
                    Date dueDate = cal.getTime();

                    // FIX 1: Include the User ID from the reservation
                    BorrowRecord newRecord = new BorrowRecord(reservation.getUserId(), returnedItem, borrowDate,
                            dueDate);

                    // FIX 2: Register the record in BOTH places
                    nextUser.addBorrowRecord(newRecord);
                    returnedItem.getBorrowHistory().add(newRecord); // This makes it show in item history!
                }
                saveAllRecords();
                iterator.remove();
                return;
            }
        }
        returnedItem.setAvailable(true);
    }

    private UserAccount findUser(String userId) {
        for (UserAccount u : database.getUsers()) {
            if (u.getUserId().equals(userId))
                return u;
        }
        return null;
    }

    private LibraryItem findItem(String itemId) {
        return recursiveFindItem(database.getItems(), itemId, 0);
    }

    private LibraryItem recursiveFindItem(List<LibraryItem> items, String id, int index) {
        if (index >= items.size())
            return null; // Base case: not found
        if (items.get(index).getId().equals(id))
            return items.get(index); // Base case: found
        return recursiveFindItem(items, id, index + 1); // Recursive call
    }

    public void saveAllRecords() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("data/borrow_records.txt"))) {
            for (UserAccount user : database.getUsers()) {
                for (BorrowRecord record : user.getBorrowHistory()) {
                    // This uses the method you wrote in your model!
                    writer.println(record.toFileString());
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving history: " + e.getMessage());
        }
    }

}
