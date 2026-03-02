package model;

public class Magazine extends LibraryItem implements Borrowable {
    private int issueNumber;

    public Magazine(String id, String title, String author, int year, int issueNumber) {
        super(id, title, author, year);
        this.issueNumber = issueNumber;
    }

    public int getIssueNumber() {
        return issueNumber;
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
        System.out.println("Magazine: " + title + " | Issue: " + issueNumber);
    }

}
