package com.go.hungrynaki.groupmentioneditbox;

public class SelectedItem {
    private String name;
    private int start, end;

    public String getName() {
        return name;
    }

    public SelectedItem setName(String name) {
        this.name = name;
        return this;
    }

    public int getStart() {
        return start;
    }

    public SelectedItem setStart(int start) {
        this.start = start;
        return this;
    }

    public int getEnd() {
        return end;
    }

    public SelectedItem setEnd(int end) {
        this.end = end;
        return this;
    }
}
