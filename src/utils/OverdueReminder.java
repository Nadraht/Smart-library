package utils;

import model.*;

import javax.swing.*;
import java.util.*;

public class OverdueReminder {

    public static void startReminder(LibraryDatabase db) {
        // Specified javax.swing.Timer explicitly to resolve ambiguity
        javax.swing.Timer timer = new javax.swing.Timer(60000, e -> checkOverdue(db));
        timer.start();
    }

    private static void checkOverdue(LibraryDatabase db) {
        Date now = new Date();

        for (UserAccount user : db.getUsers()) {
            for (BorrowRecord record : user.getBorrowHistory()) {
                if (record.getReturnDate() == null &&
                        record.getDueDate().before(now)) {

                    JOptionPane.showMessageDialog(null,
                            "OVERDUE ALERT!\nUser: " + user.getName() +
                                    "\nItem: " + record.getItem().getTitle());
                }
            }
        }
    }
}