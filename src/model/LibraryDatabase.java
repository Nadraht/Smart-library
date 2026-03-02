package model;

import java.util.*;

public class LibraryDatabase {
    private List<LibraryItem> items;
    private List<UserAccount> users;
    private Queue<LibraryItem> reservationQueue;

    public LibraryDatabase() {
        items = new ArrayList<>();
        users = new ArrayList<>();
        reservationQueue = new LinkedList<>();
    }

    public void addItem(LibraryItem item) {
        items.add(item);
    }

    public void removeItem(LibraryItem item) {
        items.remove(item);
    }

    public List<LibraryItem> getItems() {
        return items;
    }

    public void addUser(UserAccount user) {
        users.add(user);
    }

    public List<UserAccount> getUsers() {
        return users;
    }

    public Queue<LibraryItem> getReservationQueue() {
        return reservationQueue;
    }
}
