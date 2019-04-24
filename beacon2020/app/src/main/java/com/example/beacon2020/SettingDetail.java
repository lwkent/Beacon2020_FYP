package com.example.beacon2020;

import android.os.Vibrator;

public class SettingDetail {

    public boolean notificationalert;
    public boolean vibration;
    public boolean soundAlert;
    public boolean bluetoothenable;

    public SettingDetail() {
    }

    public void vibrate(){

    }
    public boolean isNotificationalert() {
        return notificationalert;
    }

    public void setNotificationalert(boolean notificationalert) {
        this.notificationalert = notificationalert;
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public boolean isSoundAlert() {
        return soundAlert;
    }

    public void setSoundAlert(boolean soundAlert) {
        this.soundAlert = soundAlert;
    }

    public boolean isBluetoothenable() {
        return bluetoothenable;
    }

    public void setBluetoothenable(boolean bluetoothenable) {
        this.bluetoothenable = bluetoothenable;
    }
}
