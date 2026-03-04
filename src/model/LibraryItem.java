package model;

import java.util.List;
import java.util.ArrayList;

public abstract class LibraryItem {

    protected String id;
    protected String title;
    protected String author;
    protected int year;
    protected boolean isAvailable;
    private List<BorrowRecord> borrowHistory;

    public LibraryItem(String id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.isAvailable = true;
        this.borrowHistory = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public List<BorrowRecord> getBorrowHistory() {
        return borrowHistory;
    }

    public abstract void displayInfo();
}