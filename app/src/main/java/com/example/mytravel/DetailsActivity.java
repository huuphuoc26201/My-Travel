package com.example.mytravel;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.model.TourData;
import com.example.model.gioHangData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imageview,back,image_view,image_view1,image_view2,thatym,huytym,star,lttour,lttour1;
    private TextView time,trip,phone;
    private TextView matour;
    private TextView placename;
    private TextView about2;
    private TextView start;
    private TextView gia1;
    private TextView gia2;
    private Button booking;
    private TextView chonngay;
    private TextView gia3,nglon,treem;
    private static final int REQUEST_CALL = 1;
    int famount=0;
    int famount1=0;
    int famount2=0;
    int famount3=0;
    int sum=0;
    int n=0;
    int i=0;
    int j=0;
    int tim=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        chonngay=(TextView) findViewById(R.id.chonngay);
        booking=(Button) findViewById(R.id.button);
        imageview= (ImageView) findViewById(R.id.imageView);
        image_view= (ImageView) findViewById(R.id.imageView8);
        image_view1= (ImageView) findViewById(R.id.imageView9);
        image_view2= (ImageView) findViewById(R.id.imageView10);
        star= (ImageView) findViewById(R.id.imageView5);
        thatym= (ImageView) findViewById(R.id.tim);
        huytym= (ImageView) findViewById(R.id.huytim);
        thatym.setVisibility(View.VISIBLE);
        huytym.setVisibility(View.GONE);
        time= (TextView) findViewById(R.id.time_2);
        start= (TextView) findViewById(R.id.time_1);
        trip=(TextView) findViewById(R.id.textView5);
        lttour=(ImageView) findViewById(R.id.ltrinhtour);
        lttour1=(ImageView) findViewById(R.id.ltrinhtour1);
        matour= (TextView) findViewById(R.id.gia);
        gia1= (TextView) findViewById(R.id.gia1);
        gia2= (TextView) findViewById(R.id.gia2);
        gia3= (TextView) findViewById(R.id.gia3);
        nglon= (TextView) findViewById(R.id.textView10);
        treem= (TextView) findViewById(R.id.treem);
        placename= (TextView) findViewById(R.id.place_name);
        about2= (TextView) findViewById(R.id.about);
        back=(ImageView) findViewById(R.id.back);
        phone=(TextView) findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdt = "0943101434";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + sdt));
                if (ContextCompat.checkSelfPermission(DetailsActivity.this, android.Manifest.permission.CALL_PHONE) != getPackageManager().PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailsActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    return;
                }
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetailsActivity.this, Tour.class);
                startActivity(intent);
            }
        });

        favotire();

        chonngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chonngay();
            }
        });

        Glide.with(DetailsActivity.this).load(getIntent().getStringExtra("imageUrl"))
                .into(imageview);
        Glide.with(DetailsActivity.this).load(getIntent().getStringExtra("imageUrl"))
                .into(image_view);
        Glide.with(DetailsActivity.this).load(getIntent().getStringExtra("imageUrl1"))
                .into(image_view1);
        Glide.with(DetailsActivity.this).load(getIntent().getStringExtra("imageUrl2"))
                .into(image_view2);
        time.setText(getIntent().getStringExtra("time"));
        matour.setText(getIntent().getStringExtra("maTour"));
        placename.setText(getIntent().getStringExtra("placeName"));
        String about=getIntent().getStringExtra("about");
        about2.setText(about.replace("\\n", "\n"));
        start.setText(getIntent().getStringExtra("start"));
        String price1 = getIntent().getStringExtra("money");

        String Trip=getIntent().getStringExtra("trip");
        trip.setText(Trip.replace("\\n", "\n"));
        //trip.setText(Html.fromHtml(Trip));
        Glide.with(DetailsActivity.this).load(getIntent().getStringExtra("schedule"))
                .into(lttour);
        Glide.with(DetailsActivity.this).load(getIntent().getStringExtra("schedule1"))
                .into(lttour1);
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        double price=Double.parseDouble(price1);
        famount1= (int) (famount+price/2);
        famount3= (int) (famount2+price);
        sum=famount+famount2;
        gia3.setText(decimalFormat.format(sum)+" ₫");
    }

    private void favotire() {

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
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        String childName = childSnapshot.child("maTour").getValue(String.class);
                                        if (childName.equals(getIntent().getStringExtra("maTour"))) {
                                            huytym.setVisibility(View.VISIBLE);
                                            thatym.setVisibility(View.GONE);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Xử lý lỗi nếu có.
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

    private void chonngay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog và ẩn các ngày trước ngày hiện tại
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Hiển thị ngày đã chọn trên TextView
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    chonngay.setText(date);

                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    public void add_nl(View view){
        i++;
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        nglon.setText(Integer.toString(i));
        famount2=famount3+famount2;
        sum=famount2+famount;
        gia3.setText(decimalFormat.format(sum)+" ₫  ");
        gia1.setText(decimalFormat.format(famount2)+" ₫");
    }
    public void sub_nl(View view){
        if(i>0){
        i--;
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        nglon.setText(Integer.toString(i));
        famount2=famount2-famount3;
        sum=famount2+famount;
        gia3.setText(decimalFormat.format(sum)+" ₫  ");
        gia1.setText(decimalFormat.format(famount2)+" ₫");}
    }
    public void add_te(View view){
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        j++;
        famount=famount1+famount;
        sum=famount+famount2;
        treem.setText(Integer.toString(j));
        gia3.setText(decimalFormat.format(sum)+" ₫  ");
        gia2.setText(decimalFormat.format(famount)+" ₫");
    }
    public void sub_te(View view){
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        if(j>0){
            j--;
            treem.setText(Integer.toString(j));
            famount=famount-famount1;
            sum=famount2+famount;
            gia3.setText(decimalFormat.format(sum)+" ₫  ");
            gia2.setText(decimalFormat.format(famount)+" ₫");}
    }

    public void next(View view){
        n++;
        if(n%3==0){
            Glide.with(DetailsActivity.this).load(getIntent().getStringExtra("imageUrl"))
                    .into(imageview);
        }
        else if ((n+2)%3==0){
            Glide.with(DetailsActivity.this).load(getIntent().getStringExtra("imageUrl1"))
                    .into(imageview);
        }
        else if ((n+1)%3==0){
            Glide.with(DetailsActivity.this).load(getIntent().getStringExtra("imageUrl2"))
                    .into(imageview);
        }
    }
    public void prev(View view) {
        n--;
        if (n%3==0) {
            Glide.with(DetailsActivity.this).load(getIntent().getStringExtra("imageUrl"))
                    .into(imageview);
        }

        else if ((n+2)%3==0) {
                Glide.with(DetailsActivity.this).load(getIntent().getStringExtra("imageUrl1"))
                        .into(imageview);
            } else if ((n+1)%3==0){
            Glide.with(DetailsActivity.this).load(getIntent().getStringExtra("imageUrl2"))
                    .into(imageview);
        }
    }
    public void tym(View view){
        huytym.setVisibility(View.VISIBLE);
        thatym.setVisibility(View.GONE);
        TourData tourdata=new TourData(getIntent().getStringExtra("placeName"),getIntent().getStringExtra("money"),getIntent().getStringExtra("start"),getIntent().getStringExtra("time")
                ,getIntent().getStringExtra("imageUrl"),getIntent().getStringExtra("imageUrl1"),getIntent().getStringExtra("imageUrl2"),getIntent().getStringExtra("about"),getIntent().getStringExtra("trip")
                ,getIntent().getStringExtra("maTour"),getIntent().getStringExtra("schedule"),getIntent().getStringExtra("schedule1"));
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
                            String matour = getIntent().getStringExtra("maTour"); // Lấy giá trị thuộc tính ename của đối tượng User
                            myRef.child(matour).setValue(tourdata);
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

    public void huytym(View view){
        thatym.setVisibility(View.VISIBLE);
        huytym.setVisibility(View.GONE);
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
                            String matour = getIntent().getStringExtra("maTour"); // Lấy giá trị thuộc tính ename của đối tượng User
                            myRef.child(matour).removeValue();
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
    public void booking(View view){
        String Date = chonngay.getText().toString();
        String ten = getIntent().getStringExtra("placeName");
        String ha = getIntent().getStringExtra("imageUrl");
        String matour = getIntent().getStringExtra("maTour");
        int sluong= Integer.parseInt(nglon.getText().toString());
        int sluong1= Integer.parseInt(treem.getText().toString());

        if(sluong>0||sluong1>0) {
            if (Home.manggiohang.size() > 0) {
                boolean exists = false;
                for (int i = 0; i < Home.manggiohang.size(); i++) {
                    if (Home.manggiohang.get(i).getTentour() == ten && Home.manggiohang.get(i).getDate() == Date) {
                        long gia = Long.parseLong(getIntent().getStringExtra("money"));
                        int sl= Integer.parseInt(nglon.getText().toString());
                        int sl1= Integer.parseInt(treem.getText().toString());
                        Home.manggiohang.get(i).setNglon(Home.manggiohang.get(i).getNglon() + sl);
                        Home.manggiohang.get(i).setTreem(Home.manggiohang.get(i).getTreem() + sl1);
                        Home.manggiohang.get(i).setGiatour(gia * Home.manggiohang.get(i).getNglon() + gia * Home.manggiohang.get(i).getTreem());
                        exists = true;
                    }
                }
                if (exists == false) {
                    long gia = Long.parseLong(getIntent().getStringExtra("money"));
                    long giamoi = sluong * gia + sluong1 * gia / 2;
                    gioHangData gioHangData=new gioHangData(ten, giamoi, ha, Date, sluong, sluong1,matour);
                    giohangDatabase.getInstance(this).userDao().insertgiohang(gioHangData);
                    Home.manggiohang.add(gioHangData);
                }
            } else {
                long gia = Long.parseLong(getIntent().getStringExtra("money"));
                long giamoi = sluong * gia + sluong1 * gia / 2;
                gioHangData gioHangData=new gioHangData(ten, giamoi, ha, Date, sluong, sluong1,matour);
                giohangDatabase.getInstance(this).userDao().insertgiohang(gioHangData);
                Home.manggiohang.add(gioHangData);
            }
            Intent intent = new Intent(DetailsActivity.this, booking.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(DetailsActivity.this,"Vui lòng nhập số lượng người đi!",Toast.LENGTH_LONG).show();
        }
    }




}
