package com.example.vluadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminBillManage extends AppCompatActivity {

    private RecyclerView billRecycleView;
    private RecyclerView.Adapter billAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Billitems> billitems;


    private DatabaseReference billReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bill_manage);
        billReference = FirebaseDatabase.getInstance().getReference();
        getData();
    }

    private void getData() {
        billitems = new ArrayList<>();
        Query query = billReference.child("order");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                billitems.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    String date;
                    String _user = item.getKey();
                    for (DataSnapshot sp : item.getChildren()){
                        String spkey = sp.getKey();
                        date = item.child(spkey).child("time").getValue().toString();
                        billitems.add(new Billitems(_user,date));
                    }

                }
                setBillRecycleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setBillRecycleView() {
        billRecycleView = findViewById(R.id.bill_recycler_view);
        billRecycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        billAdapter = new BillRecyclerAdapter(billitems);

        billRecycleView.setLayoutManager(layoutManager);
        billRecycleView.setAdapter(billAdapter);
    }
}