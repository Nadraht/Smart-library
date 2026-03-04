package model;

import java.util.Date;

public class BorrowRecord {
    private String userId;
    private LibraryItem item;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;

    public BorrowRecord(String userId, LibraryItem item, Date borrowDate, Date dueDate) {
        this.userId = userId;
        this.item = item;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public String toFileString() { // Removed parameter, use the field
        return userId + "," + item.getId() + "," +
                borrowDate.getTime() + "," +
                dueDate.getTime() + "," +
                (returnDate != null ? returnDate.getTime() : -1);
    }

    public static BorrowRecord fromFileString(String line, LibraryDatabase db) {
        try {
            String[] parts = line.split(",");
            String userId = parts[0];
            String itemId = parts[1];
            LibraryItem item = db.findItem(itemId); // You need a public findItem()
            Date borrowDate = new Date(Long.parseLong(parts[2]));
            Date dueDate = new Date(Long.parseLong(parts[3]));
            Date returnDate = Long.parseLong(parts[4]) != -1 ? new Date(Long.parseLong(parts[4])) : null;

            BorrowRecord record = new BorrowRecord(userId, item, borrowDate, dueDate);
            record.setReturnDate(returnDate);
            return record;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUserId() {
        return userId;
    }

    public static String getUserIdFromLine(String line) {
        try {
            String[] parts = line.split(",");
            return parts[0]; // Ensure it's index 0, which is the User ID
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public LibraryItem getItem() {
        return item;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
