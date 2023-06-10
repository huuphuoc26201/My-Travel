package com.example.mytravel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.CatogoryAdapter;
import com.example.adapter.TourAdapter;
import com.example.model.Catogory;
import com.example.model.TourData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Tour extends AppCompatActivity {
    RecyclerView  recyclerView;
    TourAdapter tourAdapter;
    private ImageView searchview;
    private EditText searchText;
    ArrayList<TourData> tourDataArrayList;
    private Spinner spnCatogory;
    private CatogoryAdapter catogoryAdapter;

    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        nav = findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.tour);
        recyclerView=findViewById(R.id.recyclerview);
        searchText=(EditText) findViewById(R.id.editText);
        searchview=(ImageView) findViewById(R.id.imageView14);
        spnCatogory=findViewById(R.id.spn_catogory);
        catogoryAdapter=new CatogoryAdapter(this,R.layout.item_select,getListCatelogy());
        spnCatogory.setAdapter(catogoryAdapter);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Tours");
        spnCatogory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            clearAll();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                TourData tourData = snapshot.getValue(TourData.class);
                                if(tourData.getPlaceName().contains(catogoryAdapter.getItem(position).getName()))
                                    tourDataArrayList.add(tourData);
                            }
                            tourAdapter = new TourAdapter(getApplicationContext(), tourDataArrayList);
                            recyclerView.setAdapter(tourAdapter);
                            tourAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Tour.this, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            clearAll();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                TourData tourData = snapshot.getValue(TourData.class);
                                tourDataArrayList.add(tourData);
                            }
                            tourAdapter = new TourAdapter(getApplicationContext(), tourDataArrayList);
                            recyclerView.setAdapter(tourAdapter);
                            tourAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Tour.this, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msearchText=searchText.getText().toString().trim();
                SearchView(msearchText);
            }
        });
        searchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msearchText=searchText.getText().toString().trim();
                SearchView(msearchText);
                hideSoftKeyborard();
            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tourAdapter);
        clearAll();


        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(Tour.this, "Home", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0, 0);
                        return true;


                    case R.id.tour:
                        Toast.makeText(Tour.this, "Tour", Toast.LENGTH_LONG).show();
                        return true;

                    case R.id.booking:
                        Toast.makeText(Tour.this, "Booking", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), booking.class));
                        overridePendingTransition(0, 0);
                        return true;


                    case R.id.news:
                        Toast.makeText(Tour.this, "News", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), News.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profile:
                        Toast.makeText(Tour.this, "Profile", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Account.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });


    }

    private void SearchView(String msearchText) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Tours");
        //Query firebasesearch=databaseReference.orderByChild("placeName").startAt(msearchText).endAt(msearchText+"\uf8ff");
        if(msearchText!=null){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    clearAll();

                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        TourData tourData=snapshot.getValue(TourData.class);
                        if(tourData.getPlaceName().toLowerCase().contains(msearchText.toLowerCase())||tourData.getMaTour().toLowerCase().contains(msearchText.toLowerCase()))
                            tourDataArrayList.add(tourData);
                    }
                    tourAdapter=new TourAdapter(getApplicationContext(),tourDataArrayList);
                    recyclerView.setAdapter(tourAdapter);
                    tourAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Tour.this,"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

                }
            });
        }else {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    clearAll();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TourData tourData = snapshot.getValue(TourData.class);
                        tourDataArrayList.add(tourData);
                    }
                    tourAdapter = new TourAdapter(getApplicationContext(), tourDataArrayList);
                    recyclerView.setAdapter(tourAdapter);
                    tourAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Tour.this, "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

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
    public void hideSoftKeyborard(){
        try{
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }
    private List<Catogory> getListCatelogy(){
        List<Catogory> list=new ArrayList<>();
        list.add(new Catogory("Địa điểm:"));
        list.add(new Catogory("Đà Nẵng"));
        list.add(new Catogory("Phú Quốc"));
        list.add(new Catogory("Hà Nội"));
        list.add(new Catogory("Nha Trang"));
        list.add(new Catogory("Huế"));
        list.add(new Catogory("Cà Mau"));
        list.add(new Catogory("Phan Thiết"));
        list.add(new Catogory("Cần Thơ"));
        list.add(new Catogory("Đà Lạt"));
        return list;
    }
}