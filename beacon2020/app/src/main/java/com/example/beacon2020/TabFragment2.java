package com.example.beacon2020;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.beacon2020.BeaconListViewAdapter.inflater;

public class TabFragment2 extends Fragment {
    protected static final String TAG = "TabFragment2";
    ListView lv;
    Context temp;
    String number[] = {"sdf", "sdf", "sdf"};
    List<String> listbeacon = new ArrayList<String>();
    BeaconListViewAdapter listViewAdapterfinal;
            ;
    //    BeaconListViewAdapter beaconAdapter = new BeaconListViewAdapter(get, listbeacon);
    private BeaconManager beaconManager;

//    @SuppressLint("ValidFragment")
//    public TabFragment2(BeaconManager beaconManager) {
//        this.beaconManager = beaconManager;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        listbeacon.add("1");
        listbeacon.add("2");
        listbeacon.add("3");

        //construct beacon


        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);

        temp = view.getContext();
//        beaconManager = BeaconManager.getInstanceForApplication(temp);
//        beaconManager.bind(temp);

        //viewList
        lv = view.findViewById(R.id.beaconDistanceList2);
        BeaconListViewAdapter listViewAdapter = new BeaconListViewAdapter(view.getContext(), listbeacon);
        listViewAdapterfinal = listViewAdapter;
        lv.setAdapter(listViewAdapterfinal);

//        Log.i(TAG, "callllllllllllllllllllll   ::  " + listbeacon);
        return view;
    }
    public void addList( List<String> x){

//        listbeacon = x;
//        Log.i(TAG, "111listbeacon   ::  " + listbeacon);

//        listViewAdapter.notifyDataSetChanged();
//                    lv.setAdapter(beaconAdapte
//
//
//
// r);
//                    beaconAdapter.notifyAll();
    }

    public void displayListVIew(){

    }


}