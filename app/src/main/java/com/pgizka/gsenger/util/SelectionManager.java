package com.pgizka.gsenger.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectionManager<T> {

    private List<T> items;
    private Map<Integer, Boolean> selectedItems;

    public SelectionManager(List<T> items) {
        this.items = items;
        selectedItems = new HashMap<>(items.size());
    }

    public void setItemSelected(int position, boolean isSelected) {
        if (isSelected) {
            setItemSelected(position);
        }
    }

    public void setItemSelected(int position) {
        selectedItems.put(position, true);
    }

    public boolean isItemSelected(int position) {
        Boolean selected = selectedItems.get(position);
        if (selected == null) {
            return false;
        } else {
            return selected;
        }
    }

    public void selectAll() {
        for (int i = 0; i < items.size(); i++) {
            selectedItems.put(i, true);
        }
    }

    public void deselectAll() {
        for (int i = 0; i < items.size(); i++) {
            selectedItems.put(i, false);
        }
    }

    public void clearSelection() {
        deselectAll();
    }

    public int getNumberOfSelectedItems() {
        int cnt = 0;
        for (int i : selectedItems.keySet()) {
            if (isItemSelected(i)) {
                cnt++;
            }
        }
        return cnt;
    }

    public List<T> getSelectedItems() {
        List<T> selected = new ArrayList<>();
        for (int i : selectedItems.keySet()) {
            if (isItemSelected(i)) {
                selected.add(items.get(i));
            }
        }
        return selected;
    }

}
