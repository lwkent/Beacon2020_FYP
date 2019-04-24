package com.example.beacon2020;

public class addUserHistoryItem {

    private String Title;
    private  String location;

    public addUserHistoryItem(String title, String location) {
        Title = title;
        this.location = location;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
