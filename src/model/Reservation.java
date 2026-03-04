package model;

public class Reservation {

    private String userId;
    private String itemId;

    public Reservation(String userId, String itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public String getItemId() {
        return itemId;
    }
}