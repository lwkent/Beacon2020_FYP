package com.example.beacon2020;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {


    protected static final String TAG = "FavouriteActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private int item;

    private String Uid;
    private RecyclerView mRecycleView;
    private FavouriteAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<FavouriteData> prodlist1;
    List<FavouriteData> prodlistremove;
    private com.like.LikeButton likeButton;


    boolean isDelete = false;
    Dialog FavDialog;
    private TextView BtndialogClose;
    private Button dialogbtnShare;

    private ProgressDialog progressDialog;

    private int n=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        prodlist1 = new ArrayList<>();
        prodlistremove = new ArrayList<>();

        FavDialog = new Dialog(this);
        FavDialog.setContentView(R.layout.custompopup);
        likeButton = FavDialog.findViewById(R.id.DialogFavourite);
        likeButton.setEnabled(false);
        BtndialogClose = FavDialog.findViewById(R.id.textViewClose);
        BtndialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavDialog.cancel();
            }
        });

        dialogbtnShare = FavDialog.findViewById(R.id.dialogbtnShare);
        dialogbtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Your Friend has something share with you !!\n http://lwkbeacon2020fyp.surge.sh/#/socialshare?="+Uid;
                String shareHub = "Beacon2020";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using :"));
            }
        });
        Button directionbtn = FavDialog.findViewById(R.id.BtnDirection);


        directionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent directionIntent = new Intent(Intent.ACTION_VIEW);
                directionIntent.setData(Uri.parse("geo:1.46497 ,110.426846"));
                startActivity(directionIntent);
            }
        });
        mRecycleView = findViewById(R.id.Favouriterecycleview);
        mRecycleView.setHasFixedSize(true);

        GetFirebase();



//                Toast.makeText(FavouriteActivity.this, "hi" + n, Toast.LENGTH_SHORT).show();


//    prodlist1.add(new FavouriteData("title", "des", "https://firebasestorage.googleapis.com/v0/b/beacon2020-d281d.appspot.com/o/BeaconUUID%2F124154144.jpg?alt=media&token=8af8fa31-6dbf-423d-9641-0e1077d364c0","is","sad"));
//    prodlist1.add(new FavouriteData("title", "des", "https://firebasestorage.googleapis.com/v0/b/beacon2020-d281d.appspot.com/o/BeaconUUID%2F124154144.jpg?alt=media&token=8af8fa31-6dbf-423d-9641-0e1077d364c0","is","sad"));
//    prodlist1.add(new FavouriteData("title", "des", "https://firebasestorage.googleapis.com/v0/b/beacon2020-d281d.appspot.com/o/BeaconUUID%2F124154144.jpg?alt=media&token=8af8fa31-6dbf-423d-9641-0e1077d364c0","is","sad"));
//    prodlist1.add(new FavouriteData("title", "des", "https://firebasestorage.googleapis.com/v0/b/beacon2020-d281d.appspot.com/o/BeaconUUID%2F124154144.jpg?alt=media&token=8af8fa31-6dbf-423d-9641-0e1077d364c0","is","sad"));
    }
    public void insertItem(int position){
//        prodlist1.add(position, new FavouriteData("sdf", "url","url","location","id"));
    }
    public void GetFirebase(){
        progressDialog.setMessage("Loading");
        progressDialog.show();

        firebaseAuth = firebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+firebaseAuth.getUid()+"/Favourite");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                prodlist1.clear();
//                if(isDelete == false) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        FavouriteData favouriteData = new FavouriteData(postSnapshot.child("title").getValue().toString(),
                                postSnapshot.child("description").getValue().toString(),
                                postSnapshot.child("url").getValue().toString(),
                                postSnapshot.child("location").getValue().toString(),
                                postSnapshot.getKey().toString());
                        if(n ==0) {
                            prodlist1.add(favouriteData);

                        }
                    }
                n++;
//                     prodlist1=prodlistremove;
//                    prodlistremove.clear();

                      sethistoryview();

//                }
//                Toast.makeText(FavouriteActivity.this, "i got result", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FavouriteActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    };

    public void sethistoryview(){
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new FavouriteAdapter(prodlist1);

        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new FavouriteAdapter.OnItemCLickListener() {
            @Override
            public void onItemClick(int position) {
                getDialogContent(position);
                FavDialog.show();
            }

            @Override
            public void onItemDeleteCLick(int position) {
                removeItem(position);
            }
        });
    }

    public void getDialogContent(int position){

        TextView title= FavDialog.findViewById(R.id.dialogtitle);
        TextView description = FavDialog.findViewById(R.id.description);
        ImageView image= FavDialog.findViewById(R.id.dialogimageview);

        Picasso.with(FavouriteActivity.this).load(prodlist1.get(position).getUrl()).resize(720,500).into(image);
        title.setText(prodlist1.get(position).getTitle());
        description.setText(prodlist1.get(position).getDescription());
        Uid = prodlist1.get(position).getUid();

    }

    public void removeItem(int position ){
        String uuid = prodlist1.get(position).getUid();
//        Toast.makeText(this, uuid, Toast.LENGTH_SHORT).show();
        removeFavouriteFirebase(uuid, position);
    }
    public void changeItem(int position, String text){
//        prodlist1.get(position).cardClick(text);

//        mAdapter.notifyItemChanged(position);

    }
    public void removeFavouriteFirebase(String id, final int position){
        item = position;
        firebaseAuth = firebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+firebaseAuth.getUid()+"/Favourite/"+id);
        databaseReference.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                prodlist1.clear();



//                mAdapter.notifyItemRemoved(item);
//                mAdapter.notifyItemChanged(item);
                isDelete = true;

                prodlist1.remove(item);
//                mAdapter.notifyDataSetChanged();
                mAdapter.notifyItemRemoved(item);
                Log.d(TAG, "onComplete: "+prodlist1.toString());
                mAdapter.notifyItemRangeChanged(item, prodlist1.size());
                Toast.makeText(FavouriteActivity.this, "Removed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                FavDialog.cancel();
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public void BuildRecycleView(){

    }
}
