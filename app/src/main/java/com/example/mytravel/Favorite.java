package com.example.mytravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.TourAdapter;
import com.example.adapter.hoTelAdapter;
import com.example.model.TourData;
import com.example.model.hoTelData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Favorite extends AppCompatActivity {
    Button btnHotel,btnTour;
    RecyclerView recyclerView;
    hoTelAdapter hotelAdapter;
    RecyclerView  recyclerViewTour;
    TourAdapter tourAdapter;
    ArrayList<hoTelData> hotelDataArrayList;
    ArrayList<TourData> tourDataArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        btnHotel=findViewById(R.id.btnhotel);
        btnTour=findViewById(R.id.btntour);
        recyclerView=findViewById(R.id.recyclerviewHotel);
        recyclerViewTour=findViewById(R.id.recyclerviewTour);
        recyclerViewTour.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(hotelAdapter);
        clearAll();
        hotel();

        LinearLayoutManager linearLayoutmanager=new LinearLayoutManager(this);
        recyclerViewTour.setLayoutManager(linearLayoutmanager);
        recyclerViewTour.setAdapter(tourAdapter);
        clearAllTour();
        tour();

    }

    private void tour() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String eemail = user.getEmail();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tài Khoản");
        ref.orderByChild("email").equalTo(eemail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        com.example.model.user User = ds.getValue(com.example.model.user.class);
                        if (User != null) {
                            String key = User.getKey();
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Favorite").child(key).child("Tour");
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    clearAllTour();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        TourData tourData = snapshot.getValue(TourData.class);
                                        tourDataArrayList.add(tourData);
                                    }
                                    tourAdapter = new TourAdapter(getApplicationContext(), tourDataArrayList);
                                    recyclerViewTour.setAdapter(tourAdapter);
                                    tourAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(Favorite.this, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xảy ra lỗi trong quá trình đọc dữ liệu
            }
        });
    }

    private void clearAllTour() {
        if(tourDataArrayList!=null){
            tourDataArrayList.clear();
            if(tourAdapter!=null){
                tourAdapter.notifyDataSetChanged();
            }
        }
        tourDataArrayList=new ArrayList<>();
    }


    private void hotel() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String eemail = user.getEmail();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tài Khoản");
        ref.orderByChild("email").equalTo(eemail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        com.example.model.user User = ds.getValue(com.example.model.user.class);
                        if (User != null) {
                            String key = User.getKey();
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Favorite").child(key).child("Hotel and Resort");
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    clearAll();
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        hoTelData hoTeldata =snapshot.getValue(hoTelData.class);
                                        hotelDataArrayList.add(hoTeldata);
                                    }
                                    hotelAdapter=new hoTelAdapter(getApplicationContext(),hotelDataArrayList);
                                    recyclerView.setAdapter(hotelAdapter);
                                    hotelAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(Favorite.this,"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xảy ra lỗi trong quá trình đọc dữ liệu
            }
        });
    }

    private void clearAll() {
        if(hotelDataArrayList!=null){
            hotelDataArrayList.clear();
            if(hotelAdapter!=null){
                hotelAdapter.notifyDataSetChanged();
            }
        }
        hotelDataArrayList=new ArrayList<>();
    }
    public void prev(View view){
        Intent intent=new Intent(Favorite.this,Account.class);
        startActivity(intent);
    }
    public void btnTour(View view){
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerViewTour.setVisibility(View.VISIBLE);
        btnTour.setBackgroundResource(R.drawable.custom_button_tour);
        btnHotel.setBackgroundResource(R.drawable.custom_button_hotel);
    }
    public void btnHotel(View view){
        recyclerViewTour.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        btnHotel.setBackgroundResource(R.drawable.custom_button_tour);
        btnTour.setBackgroundResource(R.drawable.custom_button_hotel);
    }
}