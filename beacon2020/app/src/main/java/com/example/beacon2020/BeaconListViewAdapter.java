package com.example.beacon2020;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BeaconListViewAdapter extends BaseAdapter {
    private static final String TAG = "beaconlist";
    Context context;
    static LayoutInflater inflater = null;
    int icons[];
    //String number[];
    List<String> beacond;


    public BeaconListViewAdapter(Context context,  List<String> number){
        this.context = context;
        this.beacond = number;
    }

    @Override
    public int getCount() {
        return beacond.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(row == null)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.listview_content, null);

            TextView tv = row.findViewById(R.id.detectedDistance);
            tv.setText(beacond.get(position));
        }
//        Log.i(TAG, "beacon in adapter4: " + beacond );
        return row;
    }
}
