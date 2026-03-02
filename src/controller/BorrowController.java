package controller;

import model.*;
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
            cal.add(Calendar.DAY_OF_MONTH, 14); // 2 weeks
            Date dueDate = cal.getTime();

            BorrowRecord record = new BorrowRecord(item, borrowDate, dueDate);
            user.addBorrowRecord(record);

            return true;
        } else {
            database.getReservationQueue().offer(item);
            return false;
        }
    }

    public boolean returnItem(String userId, String itemId) {
        UserAccount user = findUser(userId);
        LibraryItem item = findItem(itemId);

        if (user == null || item == null)
            return false;

        for (BorrowRecord record : user.getBorrowHistory()) {
            if (record.getItem().getId().equals(itemId) && record.getReturnDate() == null) {
                record.setReturnDate(new Date());
                item.setAvailable(true);

                processReservationQueue();
                return true;
            }
        }
        return false;
    }

    private void processReservationQueue() {
        if (!database.getReservationQueue().isEmpty()) {
            LibraryItem next = database.getReservationQueue().poll();
            next.setAvailable(false);
        }
    }

    private UserAccount findUser(String userId) {
        for (UserAccount u : database.getUsers()) {
            if (u.getUserId().equals(userId))
                return u;
        }
        return null;
    }

    private LibraryItem findItem(String itemId) {
        for (LibraryItem i : database.getItems()) {
            if (i.getId().equals(itemId))
                return i;
        }
        return null;
    }
}
