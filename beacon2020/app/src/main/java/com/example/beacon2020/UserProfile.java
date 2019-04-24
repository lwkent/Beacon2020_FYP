package com.example.beacon2020;

public class UserProfile {
    public String userPhone;
    public String userEmail;
    public String userName;
    public String userType;

    public UserProfile(){

    }

    public UserProfile(String userPhone, String userEmail, String userName, String userType) {
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userType = userType;
    }
    public UserProfile(String userEmail, String userName, String userType) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userType = userType;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
