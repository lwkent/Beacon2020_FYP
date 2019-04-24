package com.example.beacon2020;

public class HistoryItem {
    private int mImageResource;
    private String title;
    private String description;
    private String Location;

    public   HistoryItem(){

    }
    public HistoryItem(String title, String location) {
        this.title = title;
        this.Location = location;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
