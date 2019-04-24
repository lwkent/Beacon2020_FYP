package com.example.beacon2020;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class permissionAlert  {

    String name,line1,line2;

    public permissionAlert() {
    }

    public permissionAlert(String name, String line1, String line2) {
        this.name = name;
        this.line1 = line1;
        this.line2 = line2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }
}
