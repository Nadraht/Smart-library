package controller;

import model.*;
import java.util.*;

public class SearchEngine {

    // 1. KEYWORD LINEAR SEARCH: Fixed to find partial matches (keywords)
    // UPDATED LINEAR SEARCH: Matches if the chosen field contains the keyword
    public List<LibraryItem> linearSearch(List<LibraryItem> items, String keyword, String field) {
        List<LibraryItem> results = new ArrayList<>();
        for (LibraryItem item : items) {
            // Get the value of the field we are searching in (Title, Author, or Year)
            String fieldValue = getFieldValue(item, field);

            // Case-insensitive partial match
            if (fieldValue.toLowerCase().contains(keyword.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }

    // 2. BINARY SEARCH: Works on Title, Author, or Year (if sorted)
    public LibraryItem binarySearch(List<LibraryItem> items, String target, String field) {
        int left = 0, right = items.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String midValue = getFieldValue(items.get(mid), field);
            int cmp = midValue.compareToIgnoreCase(target);

            if (cmp == 0)
                return items.get(mid);
            if (cmp < 0)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return null;
    }

    // 3. SELECTION SORT: Updated for dynamic fields
    public void selectionSort(List<LibraryItem> items, String field) {
        for (int i = 0; i < items.size() - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < items.size(); j++) {
                if (getFieldValue(items.get(j), field)
                        .compareToIgnoreCase(getFieldValue(items.get(minIdx), field)) < 0) {
                    minIdx = j;
                }
            }
            Collections.swap(items, i, minIdx);
        }
    }

    // 4. MERGE SORT: Added back and updated for dynamic fields
    public void mergeSort(List<LibraryItem> items, String field) {
        if (items.size() > 1) {
            int mid = items.size() / 2;
            List<LibraryItem> left = new ArrayList<>(items.subList(0, mid));
            List<LibraryItem> right = new ArrayList<>(items.subList(mid, items.size()));

            mergeSort(left, field);
            mergeSort(right, field);

            merge(items, left, right, field);
        }
    }

    private void merge(List<LibraryItem> items, List<LibraryItem> left, List<LibraryItem> right, String field) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (getFieldValue(left.get(i), field).compareToIgnoreCase(getFieldValue(right.get(j), field)) <= 0) {
                items.set(k++, left.get(i++));
            } else {
                items.set(k++, right.get(j++));
            }
        }
        while (i < left.size())
            items.set(k++, left.get(i++));
        while (j < right.size())
            items.set(k++, right.get(j++));
    }

    // Helper: Allows the algorithms to look at Title, Author, or Year
    private String getFieldValue(LibraryItem item, String field) {
        switch (field) {
            case "Author":
                return item.getAuthor();
            case "Year":
                return String.valueOf(item.getYear());
            default:
                return item.getTitle();
        }
    }
}