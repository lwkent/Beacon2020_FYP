package com.example.beacon2020;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class TabFragment1 extends Fragment {
    private final int FIVE_SECONDS = 5000;
    final Handler handler = new Handler();
    private boolean started = false;
    public View view;


    Dialog myDialog ;
    List<String> listbeaconid = new ArrayList<String>();
    MainActivity main;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//        row = inflater.inflate(R.layout.listview_content, null);
        main = new MainActivity();
       view = inflater.inflate(R.layout.tab_fragment_1,container,false);
        Button showp = view.findViewById(R.id.popupbutton);


        listbeaconid.add("fc67e849-3e2c-4005-8f24-a9c0198f86e4");
        Log.i("testing", "testing initial the tabFragment" + main.getbeaconID() );
        myDialog = new Dialog(getContext());
        Log.i("testing", "testing initial the tabFragment" + getContext() );
        myDialog.setContentView(R.layout.custompopup);

        permissionAlert a = new permissionAlert();
        TextView tvName = (TextView)myDialog.findViewById(R.id.dialogtitle);
        tvName.setText(a.getName());
        Log.i("testing", "testingttt" + a.getName());
        TextView dialogline1 = myDialog.findViewById(R.id.line1);
        dialogline1.setText(a.getLine1());
//        TextView dialogline2 = myDialog.findViewById(R.id.line2);
//        dialogline2.setText(a.getLine2());

        showp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              myDialog.show();
            }
        });



        TextView txtclose;
        txtclose = myDialog.findViewById(R.id.textViewClose);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity m = new MainActivity();
                m.changedialog();
                myDialog.dismiss();
                Log.i("testing", "Hiii" );
            }


        });
        return  view;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if(started) {
                getbeaconid(main.getbeaconID());
            }
        }
    };

    public void changeDialogContent(){
//        TextView tvName = (TextView)myDialog.findViewById(R.id.nametitle);
//        tvName.setText("sdfs");
//        TextView dialogline1 = myDialog.findViewById(R.id.nametitle);
//        dialogline1.setText("line1");
//        TextView dialogline2 = myDialog.findViewById(R.id.nametitle);
//        dialogline2.setText("line2");
    };
    public void opendialog() {


        Log.i("testing", "ttiersfsfsdfsdfsdfsf" + getContext());
    };
    public  void getbeaconid(List list){
        List id = null;
        if (id.get(0) == listbeaconid.get(0) ){
//            myDialog.show();

        }  Log.i("testing", "testing Runnable");
        handler.postDelayed(runnable, FIVE_SECONDS);
    };

//    @RequiresApi(api = Build.VERSION_CODES.M)
    public void ShowPopup(View v){
        TextView txtclose;
        txtclose = myDialog.findViewById(R.id.textViewClose);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }


        });
//        myDialog.show();
    }
}
