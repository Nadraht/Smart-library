package model;

import java.util.ArrayList;
import java.util.List;

public class UserAccount {
    private String userId;
    private String name;
    private List<BorrowRecord> borrowHistory;

    public UserAccount(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.borrowHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public List<BorrowRecord> getBorrowHistory() {
        return borrowHistory;
    }

    public void addBorrowRecord(BorrowRecord record) {
        borrowHistory.add(record);
    }
}
