package model;

import java.util.Date;

public class BorrowRecord {
    private LibraryItem item;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;

    public BorrowRecord(LibraryItem item, Date borrowDate, Date dueDate) {
        this.item = item;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
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
