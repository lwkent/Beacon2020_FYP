package com.example.beacon2020;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    protected static final String TAG = "HistoryActivity";
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private Button clear;

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<HistoryItem> prodlist;
    List<HistoryItem> prodlistFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clear = findViewById( R.id.btnClearHistory);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);




        prodlist = new ArrayList<>();
        mRecycleView = findViewById(R.id.historyrecycleview);
        mRecycleView.setHasFixedSize(true);

        GetFirebase();

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHistorycontent();
            }
        });



    }

    public void  clearHistorycontent(){
        prodlist.clear();
        firebaseAuth = firebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+firebaseAuth.getUid()+"/History");
        databaseReference.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(HistoryActivity.this, "Cleared Record", Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.notifyDataSetChanged();

    }

    public void GetFirebase(){

        progressDialog.setMessage("Loading");
        progressDialog.show();
        firebaseAuth = firebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users/"+firebaseAuth.getUid()+"/History");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "History date: " + dataSnapshot);
                Log.d(TAG, "History date count: " + dataSnapshot.getChildrenCount());

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                        <YourClass > post = postSnapshot.getValue( < YourClass >.class);
                    Log.d(TAG, "History date: " +postSnapshot.child("location").getValue());
//                    HistoryItem historyItem = dataSnapshot.getValue(postSnapshot.child("location").getValue(), postSnapshot.child("title").getValue());
                    HistoryItem historyItem1 = new HistoryItem(postSnapshot.child("title").getValue().toString()
                            , postSnapshot.child("location").getValue().toString());
                    prodlist.add(historyItem1);

                    sethistoryview();
                }
                progressDialog.dismiss();
//                Toast.makeText(HistoryActivity.this, "i got result", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();

                Toast.makeText(HistoryActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void sethistoryview(){

        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HistoryAdapter(this, prodlist);
        mRecycleView.setAdapter(mAdapter);

        SearchView searchtest = findViewById(R.id.textSearchHistory);
        searchtest.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.example_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView seachView = (SearchView) searchItem.getActionView();
//
//        seachView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                mAdapter.getFilter().filter(newText);
////                return false;
//            }
//        });
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
