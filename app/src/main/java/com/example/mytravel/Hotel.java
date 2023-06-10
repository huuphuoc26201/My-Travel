package com.example.mytravel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.adapter.hoTelAdapter;
import com.example.model.Catogory;
import com.example.model.hoTelData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Hotel extends AppCompatActivity {
    RecyclerView recyclerView;
    hoTelAdapter hotelAdapter;
    private ImageView searchview,back;
    private EditText searchText;
    private Spinner spnCatogory;
    private CatogoryAdapter catogoryAdapter;

    ArrayList<hoTelData> hotelDataArrayList;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Hotel");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        recyclerView=findViewById(R.id.recyclerview);
        searchText=(EditText) findViewById(R.id.editText);
        searchview=(ImageView) findViewById(R.id.imageView14);
        back=(ImageView) findViewById(R.id.back);
        spnCatogory=findViewById(R.id.spn_catogory);
        catogoryAdapter=new CatogoryAdapter(this,R.layout.item_select,getListCatelogy());
        spnCatogory.setAdapter(catogoryAdapter);

        spnCatogory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            clearAll();

                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                hoTelData hoTeldata =snapshot.getValue(hoTelData.class);
                                if(hoTeldata.getDiachi().contains(catogoryAdapter.getItem(position).getName()))
                                    hotelDataArrayList.add(hoTeldata);
                            }
                            hotelAdapter=new hoTelAdapter(getApplicationContext(),hotelDataArrayList);
                            recyclerView.setAdapter(hotelAdapter);
                            hotelAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Hotel.this,"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    });
                }else {
                    allhotel();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Hotel.this,Home.class);
                startActivity(intent);
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
        recyclerView.setAdapter(hotelAdapter);

        clearAll();



    }
    private void allhotel(){
        databaseReference.addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(Hotel.this,"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
    private void SearchView(String msearchText) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Hotel");
        if(msearchText!=null){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    clearAll();

                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        hoTelData hoTeldata =snapshot.getValue(hoTelData.class);
                        if(hoTeldata.getName().toLowerCase().contains(msearchText.toLowerCase())||hoTeldata.getDiachi().toLowerCase().contains(msearchText.toLowerCase()))
                            hotelDataArrayList.add(hoTeldata);
                    }
                    hotelAdapter=new hoTelAdapter(getApplicationContext(),hotelDataArrayList);
                    recyclerView.setAdapter(hotelAdapter);
                    hotelAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Hotel.this,"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

                }
            });
        }else {
           allhotel();
        }

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
        list.add(new Catogory("Nha Trang"));
        list.add(new Catogory("Vũng Tàu"));
        return list;
    }
}