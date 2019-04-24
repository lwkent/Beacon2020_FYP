package com.example.beacon2020;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import static android.content.Context.MODE_PRIVATE;

public class SettingData {

    private boolean notificationAlert;
    private boolean vibration;
    SharedPreferences preferences;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String NOTIFICATION_ALERT = "notalert";
    private static final String VIBRATE = "vibrate";



    public SettingData() {
//        SharedPreferences  sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

//        this.notificationAlert = sharedPreferences.getBoolean(NOTIFICATION_ALERT, true);
//        this.vibration = sharedPreferences.getBoolean(VIBRATE, true);
    }

    public boolean isNotificationAlert() {

        return notificationAlert;
    }

    public void setNotificationAlert(boolean notificationAlert) {
        this.notificationAlert = notificationAlert;
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }
}
