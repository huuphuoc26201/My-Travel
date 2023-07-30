package com.example.mytravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.historyHotelAdapter;
import com.example.adapter.historyTourAdapter;
import com.example.model.bookHotelData;
import com.example.model.historyTourData;
import com.example.model.supportData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    ImageView back;
    Button btnHotel,btnTour;
    RecyclerView  recyclerVieHotel;
    historyHotelAdapter historyHotelAdapter;
    ArrayList<bookHotelData> hotelDataArrayList;

    RecyclerView recyclerViewTour;
    historyTourAdapter tourAdapter;
    ArrayList<historyTourData> tourDataArrayList;

    EditText edtname,edtphone, editemail,edtstkmomo;
    TextView id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        back=(ImageView) findViewById(R.id.back);
        btnHotel=findViewById(R.id.btnhotel);
        btnTour=findViewById(R.id.btntour);
        recyclerVieHotel=findViewById(R.id.recyclerviewHotel);
        recyclerViewTour=findViewById(R.id.recyclerviewTour);

        LinearLayoutManager linearLayoutManagerTour=new LinearLayoutManager(this);
        recyclerViewTour.setLayoutManager(linearLayoutManagerTour);
        recyclerViewTour.setAdapter(tourAdapter);
        clearAllTour();
        tour();

        recyclerVieHotel.setVisibility(View.INVISIBLE);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerVieHotel.setLayoutManager(linearLayoutManager);
        recyclerVieHotel.setAdapter(historyHotelAdapter);
        clearAllHotel();
        hotel();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(History.this,Account.class);
                startActivity(intent);
            }
        });

        deleteHotel();
        deleteTour();
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
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("BookingTour").child(key);
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    clearAllTour();
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        historyTourData tourData=snapshot.getValue(historyTourData.class);
                                        tourDataArrayList.add(tourData);
                                    }
                                    tourAdapter=new historyTourAdapter(getApplicationContext(),tourDataArrayList);
                                    recyclerViewTour.setAdapter(tourAdapter);
                                    tourAdapter.notifyDataSetChanged();
                                    // Kiểm tra nếu danh sách dữ liệu rỗng
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(History.this,"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

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

    private void deleteHotel() {
        String remove=getIntent().getStringExtra("REMOVE");
        String payment=getIntent().getStringExtra("payHotel");
        if(payment!=null && !payment.equals("Thanh toán Trực tiếp")){
            recyclerViewTour.setVisibility(View.INVISIBLE);
            recyclerVieHotel.setVisibility(View.VISIBLE);
            btnHotel.setBackgroundResource(R.drawable.custom_button_tour);
            btnTour.setBackgroundResource(R.drawable.custom_button_hotel);
            AlertDialog.Builder ok = new AlertDialog.Builder(History.this);
            ok.setTitle("Lịch sử giao dịch");
            ok.setMessage("Hóa đơn được thanh toán bằng Momo.\nVui lòng liên hệ để được hỗ trợ hoàn tiền!");
            ok.setPositiveButton("Liên hệ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditDialog();
                }
            });
            ok.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            ok.create().show();
        }
        else
        if(remove!=null){
            recyclerVieHotel.setVisibility(View.VISIBLE);
            btnHotel.setBackgroundResource(R.drawable.custom_button_tour);
            btnTour.setBackgroundResource(R.drawable.custom_button_hotel);
            AlertDialog.Builder ok = new AlertDialog.Builder(History.this);
            ok.setTitle("Lịch sử giao dịch");
            ok.setMessage("Bạn đã chắc chắn muốn hủy giao dịch này?");
            ok.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    btnHotel.setBackgroundResource(R.drawable.custom_button_tour);
                    btnTour.setBackgroundResource(R.drawable.custom_button_hotel);
                    recyclerVieHotel.setVisibility(View.VISIBLE);
                    recyclerViewTour.setVisibility(View.INVISIBLE);
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
                                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("BookingHotel").child(key);
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
    private void deleteTour() {
        String remove=getIntent().getStringExtra("REMOVETOUR");
        String payment=getIntent().getStringExtra("paymenttour");
        if(payment!=null && !payment.equals("Thanh toán Trực tiếp")){
            btnHotel.setBackgroundResource(R.drawable.custom_button_hotel);
            btnTour.setBackgroundResource(R.drawable.custom_button_tour);
            recyclerVieHotel.setVisibility(View.INVISIBLE);
            recyclerViewTour.setVisibility(View.VISIBLE);
            AlertDialog.Builder ok = new AlertDialog.Builder(History.this);
            ok.setTitle("Lịch sử giao dịch");
            ok.setMessage("Hóa đơn được thanh toán bằng Momo.Vui lòng liên hệ để được hỗ trợ hoàn tiền!");
            ok.setPositiveButton("Liên hệ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditDialog();
                }
            });
            ok.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            ok.create().show();
        }
        else
        if(remove!=null){
            AlertDialog.Builder ok = new AlertDialog.Builder(History.this);
            ok.setTitle("Lịch sử giao dịch");
            ok.setMessage("Bạn đã chắc chắn muốn hủy giao dịch này?");
            ok.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    recyclerVieHotel.setVisibility(View.INVISIBLE);
                    recyclerViewTour.setVisibility(View.VISIBLE);
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
                                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("BookingTour").child(key);
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

    private void EditDialog() {
        AlertDialog.Builder builderD = new AlertDialog.Builder(History.this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.support, null);
        builderD.setView(view);
        builderD.setTitle("Liên hệ hỗ trợ");
        builderD.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builderD.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!validatePhone()){
                }else{
                    supportUser();
                }
            }
            private boolean validatePhone() {
                String val = edtphone.getText().toString();
                String sdt = "[0-9]{10,11}";
                if (val.isEmpty()) {
                    edtphone.setError("SĐT không được để trống");
                    return false;
                } else if (!val.matches(sdt)) {
                    edtphone.setError("Số điện thoại phải có 10 đến 11 chữ số");
                    return false;
                } else {
                    edtphone.setError(null);
                    return true;
                }
            }
        });//Get text from edit text in dialog into display textview
        edtname = view.findViewById(R.id.editname);
        editemail=view.findViewById(R.id.edtemail);
        edtphone=view.findViewById(R.id.edtphone);
        edtstkmomo=view.findViewById(R.id.edtstkmomo);
        id=view.findViewById(R.id.edtidtour);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }
        String email=user.getEmail();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tài Khoản");
        ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        com.example.model.user User = ds.getValue(com.example.model.user.class);
                        if (User != null) {
                            String name = User.getName();
                            String email = User.getEmail();
                            String phone = User.getPhone();
                            String maidTour=getIntent().getStringExtra("REMOVETOUR");
                            if(maidTour!=null){
                                id.setText(maidTour);
                            }
                            String maidHotel=getIntent().getStringExtra("REMOVE");
                            if(maidHotel!=null){
                                id.setText(maidHotel);
                            }
                            editemail.setText(email);
                            edtname.setText(name);
                            edtphone.setText(phone);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xảy ra lỗi trong quá trình đọc dữ liệu
            }
        });

        builderD.create();
        builderD.show();
        }

    private void supportUser() {
        String sfullName  = edtname.getText().toString();
        String sphone  = edtphone.getText().toString();
        String semail  = editemail.getText().toString();
        String stkmomo  = edtstkmomo.getText().toString();
        String maId  = id.getText().toString();
        supportData supportData=new supportData(maId,sfullName,sphone,semail,stkmomo);

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
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("ContactHelp").child(key);
                            myRef.child(maId).setValue(supportData);
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
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("BookingHotel").child(key);
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    clearAllHotel();
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        bookHotelData hoTeldata =snapshot.getValue(bookHotelData.class);
                                        hotelDataArrayList.add(hoTeldata);
                                    }
                                    historyHotelAdapter=new historyHotelAdapter(getApplicationContext(),hotelDataArrayList);
                                    recyclerVieHotel.setAdapter(historyHotelAdapter);
                                    historyHotelAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(History.this,"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

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
    private void clearAllHotel() {
        if(hotelDataArrayList!=null){
            hotelDataArrayList.clear();
            if(historyHotelAdapter!=null){
                historyHotelAdapter.notifyDataSetChanged();
            }
        }
        hotelDataArrayList=new ArrayList<>();
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

    public void btnTour(View view){
        recyclerVieHotel.setVisibility(View.INVISIBLE);
        recyclerViewTour.setVisibility(View.VISIBLE);
        btnTour.setBackgroundResource(R.drawable.custom_button_tour);
        btnHotel.setBackgroundResource(R.drawable.custom_button_hotel);
    }
    public void btnHotel(View view){
        recyclerViewTour.setVisibility(View.INVISIBLE);
        recyclerVieHotel.setVisibility(View.VISIBLE);
        btnHotel.setBackgroundResource(R.drawable.custom_button_tour);
        btnTour.setBackgroundResource(R.drawable.custom_button_hotel);
    }
}