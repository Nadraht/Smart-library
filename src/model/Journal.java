package model;

public class Journal extends LibraryItem implements Borrowable {
    private int volume;

    public Journal(String id, String title, String author, int year, int volume) {
        super(id, title, author, year);
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    @Override
    public void borrowItem() {
        isAvailable = false;
    }

    @Override
    public void returnItem() {
        isAvailable = true;
    }

    @Override
    public void displayInfo() {
        System.out.println("Journal: " + title + " | Volume: " + volume);
    }

}
