package com.example.beacon2020;

import java.util.PriorityQueue;

public class FavouriteData {

    private String Title;
    private String Description;
    private String Url;
    private String Location;
    private String Uid;

    public FavouriteData(String title, String description, String url, String location, String uid) {
        Title = title;
        Description = description;
        Url = url;
        Location = location;
        Uid = uid;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void cardClick(String text){
        Title = text;
    }
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
