package model;

public class Book extends LibraryItem implements Borrowable {
    private String isbn;
    private String genre;
    private int copies;

    public Book(String id, String title, String author, int year, String isbn, String genre, int copies) {
        super(id, title, author, year);
        this.isbn = isbn;
        this.genre = genre;
        this.copies = copies;
        // If copies is 0 (for some reason), set available to false immediately
        this.isAvailable = (copies > 0);
    }

    public String getIsbn() {
        return isbn;
    }

    public String getGenre() {
        return genre;
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
