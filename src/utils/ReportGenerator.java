package utils;

import model.*;

import java.util.*;

public class ReportGenerator {

    public static List<String> mostBorrowedItems(LibraryDatabase db) {
        Map<String, Integer> countMap = new HashMap<>();

        for (UserAccount user : db.getUsers()) {
            for (BorrowRecord record : user.getBorrowHistory()) {
                String title = record.getItem().getTitle();
                countMap.put(title, countMap.getOrDefault(title, 0) + 1);
            }
        }

        List<String> result = new ArrayList<>();
        countMap.forEach((k, v) -> result.add(k + " → " + v + " times"));

        return result;
    }

    public static List<String> overdueUsers(LibraryDatabase db) {
        List<String> result = new ArrayList<>();
        Date now = new Date();

        for (UserAccount user : db.getUsers()) {
            for (BorrowRecord record : user.getBorrowHistory()) {
                if (record.getReturnDate() == null &&
                        record.getDueDate().before(now)) {
                    result.add(user.getName());
                    break;
                }
            }
        }
        return result;
    }

    public static Map<String, Integer> categoryDistribution(LibraryDatabase db) {
        Map<String, Integer> map = new HashMap<>();

        for (LibraryItem item : db.getItems()) {
            String type = item.getClass().getSimpleName();
            map.put(type, map.getOrDefault(type, 0) + 1);
        }
        return map;
    }
}