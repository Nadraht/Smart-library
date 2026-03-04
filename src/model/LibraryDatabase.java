package model;

import java.util.*;

public class LibraryDatabase {
    private List<LibraryItem> items;
    private List<UserAccount> users;
    private Queue<Reservation> reservationQueue;
    private LibraryItem[] recentCache = new LibraryItem[5];

    public LibraryDatabase() {
        items = new ArrayList<>();
        users = new ArrayList<>();
        reservationQueue = new LinkedList<>();
    }

    public void addItem(LibraryItem item) {
        items.add(item);
        updateRecentCache(item);
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

    public UserAccount getUser(String id) {
        for (UserAccount user : users) {
            if (user.getUserId().equals(id)) {
                return user;
            }
        }
        return null; // Returns null if the User ID isn't found
    }

    public LibraryItem findItem(String itemId) {
        for (LibraryItem item : items) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null; // Return null if the book/item isn't found
    }

    public UserAccount findUser(String userId) {
        for (UserAccount user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null; // Return null if the user isn't found
    }

    public List<UserAccount> getUsers() {
        return users;
    }

    public Queue<Reservation> getReservationQueue() {
        return reservationQueue;
    }

    private void updateRecentCache(LibraryItem item) {
        for (int i = recentCache.length - 1; i > 0; i--) {
            recentCache[i] = recentCache[i - 1];
        }
        recentCache[0] = item;
    }

}
