package controller;

import model.*;
import java.util.*;

public class SearchEngine {
    // LINEAR SEARCH
    public LibraryItem linearSearch(List<LibraryItem> items, String title) {
        for (LibraryItem item : items) {
            if (item.getTitle().equalsIgnoreCase(title))
                return item;
        }
        return null;
    }

    // BINARY SEARCH (requires sorted list)
    public LibraryItem binarySearch(List<LibraryItem> items, String title) {
        int left = 0, right = items.size() - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = items.get(mid).getTitle().compareToIgnoreCase(title);

            if (cmp == 0)
                return items.get(mid);
            else if (cmp < 0)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return null;
    }

    // RECURSIVE SEARCH
    public LibraryItem recursiveSearch(List<LibraryItem> items, String title, int index) {
        if (index >= items.size())
            return null;
        if (items.get(index).getTitle().equalsIgnoreCase(title))
            return items.get(index);
        return recursiveSearch(items, title, index + 1);
    }

    // SELECTION SORT
    public void selectionSort(List<LibraryItem> items) {
        for (int i = 0; i < items.size() - 1; i++) {
            int min = i;
            for (int j = i + 1; j < items.size(); j++) {
                if (items.get(j).getTitle()
                        .compareToIgnoreCase(items.get(min).getTitle()) < 0) {
                    min = j;
                }
            }
            Collections.swap(items, i, min);
        }
    }

    // MERGE SORT
    public void mergeSort(List<LibraryItem> items) {
        if (items.size() > 1) {
            int mid = items.size() / 2;
            List<LibraryItem> left = new ArrayList<>(items.subList(0, mid));
            List<LibraryItem> right = new ArrayList<>(items.subList(mid, items.size()));

            mergeSort(left);
            mergeSort(right);

            merge(items, left, right);
        }
    }

    private void merge(List<LibraryItem> items, List<LibraryItem> left, List<LibraryItem> right) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getTitle()
                    .compareToIgnoreCase(right.get(j).getTitle()) <= 0) {
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
}
