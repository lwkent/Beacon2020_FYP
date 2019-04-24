package com.example.beacon2020;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.TimingLogger;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import static com.example.beacon2020.BeaconListViewAdapter.inflater;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BeaconConsumer, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    protected static final String TAG = "RangingActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private com.like.LikeButton likeButton;
    private Button dialogbtnShare;
    private static final String SHARED_PREFS = "sharedPrefs",  NOTIFICATION_ALERT = "notalert", VIBRATE = "vibrate";

    private String FTItle, FDescription, FUrl, FLocation, Fuid;

    private  String uid;
    private BeaconManager beaconManager;
    private BluetoothAdapter BA;
    //notification
    private  NotificationManagerCompat notificationManager;
//    private SettingData settingData1;
    // define database
    public TextView textView;
    //define tab
    private PagerAdapter pageadapter;
    private ViewPager mViewPager;

    private Button SearchBeaconBtn;
    private boolean startSearch = false, notificationALert;
    private boolean checktime = false;

    public SettingScreen settingactivitydata;
    //dialog
    boolean showdialog = true;
    Dialog myDialog1;
    FragmentManager fm = getSupportFragmentManager();

    // define the listview
    ListView lv;
    String number[] = {"sdf", "sdf","sdf"};
    public List<String> listbeacon = new ArrayList<String>();
    public  List<String> listbeaconid = new ArrayList<String>();
    BeaconListViewAdapter beaconAdapter = new BeaconListViewAdapter(MainActivity.this, listbeacon);

//    private TimingLogger timingLogger;
        private long millis_startTime, millis_endTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //request enable bluetooth
        BA = BluetoothAdapter.getDefaultAdapter();
        Intent turnOn = new Intent( (BluetoothAdapter.ACTION_REQUEST_ENABLE));
        startActivityForResult(turnOn,0);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nav_view);
        mDrawerLayout.addDrawerListener(mToggle);
        navigationView.setNavigationItemSelectedListener(this);
//        settingData1 = new SettingData();
        settingactivitydata = new SettingScreen();
        FacebookSdk.sdkInitialize(getApplicationContext());
        loadSetting();

        printKeyHash();
        notificationManager = NotificationManagerCompat.from(this);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        // beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

        //get beacon id
        listbeaconid.add("fc67e849-3e2c-4005-8f24-a9c0198f86e4");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Android M permission checj
            if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);

                    }
                });
                builder.show();
            }
        }

        myDialog1 = new Dialog(this);
        myDialog1.setContentView(R.layout.custompopup);
        myDialog1.setCanceledOnTouchOutside(false);
        TextView dialogclosebtn = myDialog1.findViewById(R.id.textViewClose);
        Button directionbtn = myDialog1.findViewById(R.id.BtnDirection);
        dialogclosebtn.setOnClickListener((View.OnClickListener) this);


        TextView linkingtext = findViewById(R.id.linkText);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        anim.setStartOffset(200);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        linkingtext.startAnimation(anim);

//       mViewPager = findViewById(R.id.ViewPagercontainer);
//       setupViewPager(mViewPager);
//       TabLayout tabLayout = findViewById(R.id.tab_layout);
//       tabLayout.setupWithViewPager(mViewPager);
        dialogbtnShare = myDialog1.findViewById(R.id.dialogbtnShare);
        likeButton = myDialog1.findViewById(R.id.DialogFavourite);
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addFavourite();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                DeleteFavoutite();
            }
        });

        CallbackManager callbackManager;

        //sharing Button
        dialogbtnShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                 String url  = FUrl;
                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        SharePhoto photo = new SharePhoto.Builder()
                                .setBitmap(bitmap)
                                .build();
                        SharePhotoContent content = new SharePhotoContent.Builder()
//                                .addPhoto(url)
                                .build();

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "http://lwkbeacon2020fyp.surge.sh/#/socialshare?="+uid;
                String shareHub = "Beacon2020";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                PackageManager pm = v.getContext().getPackageManager();
                List<ResolveInfo> activityList = pm.queryIntentActivities(myIntent, 0);
                for (final ResolveInfo app : activityList) {
                    if ((app.activityInfo.name).contains("facebook")) {
                        setupFacebookShareIntent();
//                        final ActivityInfo activity = app.activityInfo;
//                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
//                        myIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                        myIntent.setComponent(name);
//                        v.getContext().startActivity(myIntent);

                        break;
                    }
                }

                startActivity(Intent.createChooser(myIntent, "Share using :"));
            }
        });

        directionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent directionIntent = new Intent(Intent.ACTION_VIEW);
                directionIntent.setData(Uri.parse("geo:1.46497 ,110.426846"));
                startActivity(directionIntent);
            }
        });
        SearchBeaconBtn = findViewById(R.id.btnDetectBeacon);
        SearchBeaconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch = !startSearch;
                checktime = true;
                Toast.makeText(MainActivity.this, "Searching Beacon Mode :" +startSearch, Toast.LENGTH_SHORT).show();


            }
        });


    }
    public void setupFacebookShareIntent() {
        ShareDialog shareDialog;
        FacebookSdk.sdkInitialize(getApplicationContext());
        shareDialog = new ShareDialog(this);

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("Title")
                .setContentDescription(
                        "\"Body Of Test Post\"")
                .setContentUrl(Uri.parse("http://lwkbeacon2020fyp.surge.sh/#/socialshare?="+uid))
                .build();

        shareDialog.show(linkContent);
    }
    private void printKeyHash() {
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.example.beacon2020",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Hashing", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void loadSetting() {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPreferences.edit();
        Switch notAlertSwitch = findViewById(R.id.NotAlertswitch);

        notificationALert = mPreferences.getBoolean(NOTIFICATION_ALERT, true);

    }

    public void addFavourite(){

        FavouriteData favouriteData = new FavouriteData(FTItle, FDescription, FUrl, FLocation, Fuid);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase;

        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+firebaseAuth.getUid()+"/Favourite/"+uid);

        databaseReference.setValue(favouriteData);
    }

    public void DeleteFavoutite(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase;

        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+firebaseAuth.getUid()+"/Favourite/");
        databaseReference.child(uid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(MainActivity.this, "Removed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View v) {
        myDialog1.cancel();

        checktime = false;
        delayreset(5000);

    }

    public void delayreset(int milisec){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RestartDialog();
            }
        },milisec);
    }

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

    public void addStatistic(String Id){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();

        System.out.println("format 1 " + sdf.format(date));
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("Statistic/Advertiseent/"+Id+"/" +firebaseAuth.getUid()+ sdf.format(date).toString());
        advertisementData ad = new advertisementData("sdfsdfsfdsdf");
        myRef.setValue(date);
    }


    public void dataTrigger(final String UUId){
        likeButton.setLiked(false);
        millis_startTime = System.currentTimeMillis();
        uid = UUId;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("BeaconUUID/"+UUId);
        DatabaseReference FavRef = database.getReference("Users/"+firebaseAuth.getUid()+"/Favourite");

        FavRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            boolean exist = false;
                for(DataSnapshot data: dataSnapshot.getChildren()){

                        if (data.getKey().equals(uid) && exist == false) {
                            //do ur stuff
                            Log.d(TAG, "liked");
                            likeButton.setLiked(true);
                            exist = true;
                        }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String description = dataSnapshot.child("/description").getValue().toString();
                String imageurl = dataSnapshot.child("/imageUrl").getValue().toString();
                String title = dataSnapshot.child("/title").getValue().toString();
                String location = dataSnapshot.child("/location").getValue().toString();

                FDescription = description;
                FUrl = imageurl;
                FTItle = title;
                FLocation = location;
                Fuid = uid;
//
                TextView tName = myDialog1.findViewById(R.id.dialogtitle);
                TextView tLine1 = myDialog1.findViewById(R.id.description);
                ImageView image= myDialog1.findViewById(R.id.dialogimageview);

                Picasso.with(MainActivity.this).load(imageurl).resize(720,500).into(image);
                tName.setText(title);
                tLine1.setText(description);
//
                addUserHistoryItem uploadHistoryItem = new addUserHistoryItem(title, location);
                addUserHistory(uploadHistoryItem, uid);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        timingLoggertimingLogger.dumpToLog();
        millis_endTime = System.currentTimeMillis();
        Log.d(TAG, "Time taken in milli seconds: " + + (millis_endTime - millis_startTime));
    }


    public void addUserHistory(Object his, String Id){
         FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
         FirebaseDatabase firebaseDatabase;

        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+firebaseAuth.getUid()+"/History/"+Id);

        databaseReference.setValue(his);

        addStatistic(Id);

    };

    public void RestartDialog(){
        checktime = true;
    }
    public void changedialog()
    {
        showdialog = false;
    }
    private void setupViewPager(ViewPager viewPager){
        PagerAdapter tabadapter = new PagerAdapter(getSupportFragmentManager());
        tabadapter.addFragment(new TabFragment1(), "Tab1");
        tabadapter.addFragment(new TabFragment2(), "Tab2");
        tabadapter.addFragment(new TabFragment3(), "Tab3");
        viewPager.setAdapter(tabadapter);

    }


    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (mToggle.onOptionsItemSelected(item)){
              return  true;
        }
        switch (item.getItemId()) {
            case R.id.setting:
                // your logic here.
                           Toast.makeText(this,"this is setting" + item, Toast.LENGTH_SHORT).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onRequestPermissionResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    public void sendOnChannel1(){
        Log.i(TAG, "notification!" + settingactivitydata.isNotificationALert());
        if(settingactivitydata.isNotificationALert()) {
            Notification notification = new NotificationCompat.Builder(this, App.CHANNERL_1_ID)
                    .setSmallIcon(R.drawable.beaconicon)
                    .setContentTitle("New Beacon found")
                    .setContentText("Browse the beacon with app")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_PROMO)
                    .build();
            notificationManager.notify(1, notification);
            Log.i(TAG, "notify!");
        }
    }

//    this methif is invoked when the user clicks the GPS A Plant Menu option
    public void settingCLick(MenuItem menuItem){
        Log.d("main tab", "start 222tab");

    }
    private void showNotification(String message){

        Log.d("main tab", "start 222tab" +notificationALert);
//        if(notificationALert== true) {
            Intent resultIntent = new Intent(this, MainActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "default")
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Notification1")
                    .setContentText(message)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent);
            Log.d("Hay11", "DCM11");


            NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(MainActivity.this.NOTIFICATION_SERVICE);
            Log.d("Hay12", "DCM12");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId("com.myApp");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        "com.myApp",
                        "My App",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }
            notificationManager.notify(2, builder.build());

//        }
    }

    public List getbeaconID(){
        return listbeaconid;
    }

    @Override
    public void onBeaconServiceConnect() {
            beaconManager.removeAllMonitorNotifiers();
            beaconManager.addMonitorNotifier(new MonitorNotifier() {
                @Override
                public void didEnterRegion(Region region) {
                    Log.i(TAG, "I just saw an beacon for the first time!");
                }

                @Override
                public void didExitRegion(Region region) {
                    Log.i(TAG, "I no longer see an beacon");
                }

                @Override
                public void didDetermineStateForRegion(int state, Region region) {
                    Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
                }
            });
            beaconManager.setRangeNotifier(new RangeNotifier() {
                @Override
                public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                    TabFragment2 tabfragment22 = new TabFragment2();
                    TabFragment1 tabfragment11 = new TabFragment1();

                    if (beacons.size() > 0) {
                        Log.i(TAG, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.");//
                        Log.i(TAG, "The first ID " + beacons.iterator().next().getId1() + " meters away.");
//                    Log.i(TAG, "The first beacon I see is about " + listbeacon + " meters away.");
                        double distance = beacons.iterator().next().getDistance();
                        double compareddistance = 0.08;
                        Log.i(TAG, "The distance1 : " + distance);

                        String x = beacons.iterator().next().getId1().toString();
                        String y = "fc67e849-3e2c-4005-8f24-a9c0198f86e4";
//
                        if ( startSearch == true && distance < compareddistance && checktime ==true) {// { x.equals(y) &&
                            Log.i(TAG, "the UUID :" + x);


//                            timingLogger = new TimingLogger("TimerCheck", "StartCount");
//                            timingLogger.addSplit("work A");
                            dataTrigger(x);
                            showNotification("New beacon found");
                            myDialog1.show();
                            showdialog = false;
//                            startSearch= !startSearch;

                        }

//
                        if (listbeacon.size() > 5) {
                            listbeacon.remove(0);

                        }
                        tabfragment22.addList(listbeacon);
                        Log.i(TAG, "beacon size : " + listbeacon.size());
                    }
                }

            });

            try {
                beaconManager.startRangingBeaconsInRegion(new Region("com.beacon.demo", null, null, null));
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
                Log.i(TAG, "start agian");
            } catch (RemoteException e) {
            }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.setting: {
                Intent  getNameScreenIntent = new Intent(this, SettingScreen.class);
                final int result = 1;
                startActivity(getNameScreenIntent);
                break;
            }
            case R.id.menuLogout: {
                logout();
                break;
            }
            case R.id.menuProfile: {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            }
            case R.id.historyview: {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                break;
            }
            case R.id.favouriteView: {
                startActivity(new Intent(MainActivity.this, FavouriteActivity.class));
                break;
            }



        }
        //close navigation drawer
//        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout(){
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        Toast.makeText(this,"Sign out Successful" , Toast.LENGTH_SHORT).show();
    }
}