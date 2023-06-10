package com.example.mytravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.TourAdapter;
import com.example.model.TourData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class listTour extends AppCompatActivity {
    RecyclerView recyclerView;
    TourAdapter tourAdapter;
    ArrayList<TourData> tourDataArrayList;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listtour);

        recyclerView=findViewById(R.id.recyclerview);
        back=(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(listTour.this,Home.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tourAdapter);
        clearAll();
        String name = getIntent().getStringExtra("name");
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Tours");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAll();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    TourData tourData=snapshot.getValue(TourData.class);
                    if(tourData.getPlaceName().toLowerCase().contains(name.toLowerCase()))
                        tourDataArrayList.add(tourData);
                }
                tourAdapter=new TourAdapter(getApplicationContext(),tourDataArrayList);
                recyclerView.setAdapter(tourAdapter);
                tourAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(listTour.this,"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
    private void clearAll() {
        if(tourDataArrayList!=null){
            tourDataArrayList.clear();
            if(tourAdapter!=null){
                tourAdapter.notifyDataSetChanged();
            }
        }
        tourDataArrayList=new ArrayList<>();
    }
}