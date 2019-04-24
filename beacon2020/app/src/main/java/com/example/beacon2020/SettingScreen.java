package com.example.beacon2020;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.util.Set;

public class SettingScreen extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String NOTIFICATION_ALERT = "notalert";
    private static final String VIBRATE = "vibrate";

    private boolean notificationALert,Vibrate;

    private BluetoothAdapter BA;

    private Set<BluetoothDevice> pairedDevices;
    private SettingData settingData;

    SwitchCompat bluetoothswitch;
    Switch notAlertSwitch, vibrateSwitch;
    boolean statebluetoothswitch;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.settinglayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//

        BA = BluetoothAdapter.getDefaultAdapter();
        Intent turnOn = new Intent( (BluetoothAdapter.ACTION_REQUEST_ENABLE));
//        startActivityForResult(turnOn,0);
        bluetoothswitch = findViewById(R.id.bluetoothswitch);
        notAlertSwitch = findViewById(R.id.NotAlertswitch);
        vibrateSwitch = findViewById(R.id.vibrateSwitch);
        loadData();

        settingData = new SettingData();
        notAlertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            SharedPreferences  sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                settingData.setNotificationAlert(isChecked);
                editor.putBoolean(NOTIFICATION_ALERT, isChecked);
                editor.apply();
                editor.commit();
                Toast.makeText(getApplicationContext(), "this: "+isChecked ,Toast.LENGTH_SHORT).show();

            }
        });
        vibrateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            SharedPreferences  sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                settingData.setVibration(isChecked);
                editor.putBoolean(VIBRATE, isChecked);
                editor.apply();
                editor.commit();
                Toast.makeText(getApplicationContext(), "this: "+isChecked ,Toast.LENGTH_SHORT).show();

            }
        });




        preferences = getSharedPreferences("PREPS", 0);
        statebluetoothswitch = preferences.getBoolean("bluetoothswitch", checkBluetoothStatus());

        bluetoothswitch.setChecked(statebluetoothswitch);

        bluetoothswitch.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                BA = BluetoothAdapter.getDefaultAdapter();
                if (!BA.isEnabled()) {
                    BA.enable();
                    Toast.makeText(getApplicationContext(), "Bluetooth enabled",Toast.LENGTH_SHORT).show();
                } else {
                    BA.disable();
                    Toast.makeText(getApplicationContext(), "Bluetooth disabled",Toast.LENGTH_SHORT).show();
                }
                statebluetoothswitch = !statebluetoothswitch;
                bluetoothswitch.setChecked(statebluetoothswitch);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("statebluetoothswitch", statebluetoothswitch);
                editor.apply();

            }
        });



    }
    public boolean checkBluetoothStatus(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                return false;
            }
            else return true;
        }
        return false;
    };
    public void on(View v){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }
    public void off(View v){
        BA.disable();
        Toast.makeText(getApplicationContext(), "Turned off" ,Toast.LENGTH_LONG).show();
    }
    public  void visible(View v){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveData(){
        SharedPreferences  sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(NOTIFICATION_ALERT, notAlertSwitch.isChecked());
        editor.putBoolean(VIBRATE, vibrateSwitch.isChecked());
        editor.apply();

    }

    public void loadData(){
        SharedPreferences  sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        notificationALert = sharedPreferences.getBoolean(NOTIFICATION_ALERT, true);
        Vibrate = sharedPreferences.getBoolean(VIBRATE, true);
        updateVIews();
    }

    public void updateVIews(){
        notAlertSwitch.setChecked(notificationALert);
        vibrateSwitch.setChecked(Vibrate);
    }

    public boolean isNotificationALert() {
        return notificationALert;
    }

    public boolean isVibrate() {
        return Vibrate;
    }
}
