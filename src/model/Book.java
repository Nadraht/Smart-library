package model;

public class Book extends LibraryItem implements Borrowable {
    private String isbn;
    private int copies;

    public Book(String id, String title, String author, int year, String isbn, int copies) {
        super(id, title, author, year);
        this.isbn = isbn;
        this.copies = copies;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getCopies() {
        return copies;
    }

    @Override
    public void borrowItem() {
        if (copies > 0) {
            copies--;
            if (copies == 0) {
                isAvailable = false;
            }
        }
    }

    @Override
    public void returnItem() {
        copies++;
        isAvailable = true;
    }

    @Override
    public void displayInfo() {
        System.out.println("Book: " + title + " | Author: " + author + " | Copies: " + copies);
    }
}
