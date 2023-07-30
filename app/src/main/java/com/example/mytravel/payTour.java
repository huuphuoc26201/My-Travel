package com.example.mytravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.PayTourAdapter;
import com.example.model.payTourData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class payTour extends AppCompatActivity {

    RecyclerView recyclerView;
    PayTourAdapter tourAdapter;
    ArrayList<payTourData> tourDataArrayList;
    TextView deleteAll,thongbao,chontour;
    BottomNavigationView nav;
    TextView countCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytour);
        deleteAll=findViewById(R.id.deleteAll);
        thongbao=findViewById(R.id.textView30);
        chontour=findViewById(R.id.chontour);
        recyclerView=findViewById(R.id.recyclerview);
        nav = findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.booking);

        countCart=findViewById(R.id.count);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tourAdapter);
        clearAll();
        setDataPayTour();
        deleteTour();
        //numberCart();
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(payTour.this, "Home", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0, 0);
                        return true;


                    case R.id.tour:
                        Toast.makeText(payTour.this, "Tour", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Tour.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.booking:
                        Toast.makeText(payTour.this, "Booking", Toast.LENGTH_LONG).show();

                        return true;


                    case R.id.news:
                        Toast.makeText(payTour.this, "News", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), News.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profile:
                        Toast.makeText(payTour.this, "Profile", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(getApplicationContext(), Account.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra nếu RecyclerView rỗng
                if (tourDataArrayList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE); // Ẩn RecyclerView
                    chontour.setVisibility(View.GONE);
                    deleteAll.setVisibility(View.GONE);
                    thongbao.setVisibility(View.VISIBLE); // Hiển thị thông báo
                    thongbao.setText("Giỏ hàng của bạn trống"); // Đặt nội dung thông báo
                    Toast.makeText(payTour.this, "Giỏ hàng của bạn đang trống không thể xóa! Vui lòng thêm tour vào giỏ hàng để tiếp tục.", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder ok = new AlertDialog.Builder(payTour.this);
                    ok.setTitle("Giỏ hàng");
                    ok.setMessage("Bạn đã chắc chắn muốn xóa hết giỏ hàng này?");
                    ok.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            countCart.setVisibility(View.GONE);
                            deleteAll();
                        }
                    });
                    ok.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    ok.create().show();
                }

            }
        });

    }

    private void numberCart() {
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
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("manyTour").child(key);
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        countCart.setVisibility(View.VISIBLE);
                                        int count = (int) dataSnapshot.getChildrenCount();
                                        countCart.setText(String.valueOf(count));
                                    }else{
                                        countCart.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Xử lý khi có lỗi xảy ra
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

    private void deleteAll() {
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
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("manyTour");
                            myRef.child(key).removeValue();
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

    private void setDataPayTour() {
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
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("manyTour").child(key);
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    clearAll();
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        payTourData tourData=snapshot.getValue(payTourData.class);
                                        tourDataArrayList.add(tourData);
                                    }
                                    tourAdapter=new PayTourAdapter(getApplicationContext(),tourDataArrayList);
                                    recyclerView.setAdapter(tourAdapter);
                                    tourAdapter.notifyDataSetChanged();
                                    countCart.setText(String.valueOf(tourDataArrayList.size()));
                                    // Kiểm tra nếu danh sách dữ liệu rỗng
                                    if (tourDataArrayList.isEmpty()) {
                                        recyclerView.setVisibility(View.GONE); // Ẩn RecyclerView
                                        chontour.setVisibility(View.GONE);
                                        deleteAll.setVisibility(View.GONE);
                                        countCart.setVisibility(View.GONE);
                                        thongbao.setVisibility(View.VISIBLE); // Hiển thị thông báo
                                    } else {
                                        recyclerView.setVisibility(View.VISIBLE); // Hiển thị RecyclerView
                                        thongbao.setVisibility(View.GONE); // Ẩn thông báo
                                        chontour.setVisibility(View.VISIBLE);
                                        countCart.setVisibility(View.VISIBLE);
                                        deleteAll.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(payTour.this,"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    }
                }
                else {
                    // Nếu không có dữ liệu, ẩn RecyclerView và hiển thị thông báo
                    recyclerView.setVisibility(View.GONE);
                    countCart.setVisibility(View.GONE);
                    thongbao.setVisibility(View.VISIBLE);
                    chontour.setVisibility(View.GONE);
                    deleteAll.setVisibility(View.GONE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xảy ra lỗi trong quá trình đọc dữ liệu
            }
        });

    }

    private void deleteTour() {
        String remove=getIntent().getStringExtra("REMOVE");

        if(remove!=null){
            AlertDialog.Builder ok = new AlertDialog.Builder(payTour.this);
            ok.setTitle("Giỏ Hàng");
            ok.setMessage("Bạn đã chắc chắn muốn xóa Tuor này?");
            ok.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    numberCart();
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
                                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("manyTour").child(key);
                                        myRef.child(remove).removeValue();
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
            });
            ok.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            ok.create().show();
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
}