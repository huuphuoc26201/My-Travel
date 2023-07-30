package com.example.mytravel;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.model.TourData;
import com.example.model.payTourData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Random;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imageview,back,image_view,image_view1,image_view2,thatym,huytym,star,lttour,lttour1,setdate;
    private TextView time,trip,phone;
    private TextView matour;
    private TextView placename;
    private TextView about2;
    private TextView start;
    private TextView gia1;
    private TextView gia2;
    private Button booking;
    private EditText chonngay;
    private TextView gia3,nglon,treem;
    private Calendar calendar;
    private static final int REQUEST_CALL = 1;
    int famount=0;
    int famount1=0;
    int famount2=0;
    int famount3=0;
    int sum=0;
    int n=0;
    int i=0;
    int j=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        chonngay=(EditText) findViewById(R.id.chonngay);
        setdate=findViewById(R.id.imageView3);
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
                String back=getIntent().getStringExtra("back");
                Intent intent;
                if(back!=null){
                    intent = new Intent(DetailsActivity.this, Home.class);
                }else{
                    intent = new Intent(DetailsActivity.this, Tour.class);
                }
                startActivity(intent);

            }
        });
        favotire();


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

        calendar = Calendar.getInstance();

        chonngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyborard();
                showDatePickerDialog();
            }
        });
        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }
    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String selectedDate = DateFormat.format("dd/MM/yyyy", calendar).toString();
                chonngay.setText(selectedDate);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                DetailsActivity.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Ẩn ngày trước ngày hiện tại
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            datePickerDialog.setOnDateSetListener(dateSetListener);
        }
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // Xử lý khi bảng ngày tháng bị hủy
            }
        });

        // Ẩn các ngày thứ 2 và thứ 5
        String textDate = matour.getText().toString();
        String textDateTime = start.getText().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePickerDialog.getDatePicker().setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(Calendar.YEAR, year);
                    selectedCalendar.set(Calendar.MONTH, monthOfYear);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    int selectedDayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK);

                        if (textDate.toLowerCase().contains("NHPT01".toLowerCase())||textDate.toLowerCase().contains("NHPT14".toLowerCase())) {
                            if (selectedDayOfWeek != 5) {
                                // Nếu không là thứ 5, không cho phép chọn
                                Toast.makeText(getApplicationContext(),"Quý khách vui lòng Chọn lại. Chương trình Tuor bắt đầu từ ngày " + textDateTime,Toast.LENGTH_SHORT).show();
                                view.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),this);
                            }
                        }else if (textDate.toLowerCase().contains("NHPT02".toLowerCase())||textDate.toLowerCase().contains("NHPT03".toLowerCase())||textDate.toLowerCase().contains("NHPT05".toLowerCase())) {
                            if (selectedDayOfWeek != 2||selectedDayOfWeek != 7 ) {
                            // Nếu không là thứ 2 và thứ 7, không cho phép chọn
                            Toast.makeText(DetailsActivity.this, "Quý khách vui lòng Chọn lại. Chương trình Tuor bắt đầu từ ngày " + textDateTime, Toast.LENGTH_SHORT).show();
                                view.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),this);
                        }
                    }else if (textDate.toLowerCase().contains("NHPT04".toLowerCase())||textDate.toLowerCase().contains("NHPT09".toLowerCase())||textDate.toLowerCase().contains("NHPT10".toLowerCase())
                            ||textDate.toLowerCase().contains("NHPT17".toLowerCase())||textDate.toLowerCase().contains("NHPT18".toLowerCase())||textDate.toLowerCase().contains("NHPT20".toLowerCase())) {
                            if (selectedDayOfWeek != 7) {
                            // Nếu không là thứ 7, không cho phép chọn
                            Toast.makeText(DetailsActivity.this, "Quý khách vui lòng Chọn lại. Chương trình Tuor bắt đầu từ ngày " + textDateTime, Toast.LENGTH_SHORT).show();
                                view.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),this);
                        }
                    }else if (textDate.toLowerCase().contains("NHPT06".toLowerCase())||textDate.toLowerCase().contains("NHPT07".toLowerCase())) {
                            if (selectedDayOfWeek != 1) {
                            // Nếu không là chủ nhật, không cho phép chọn
                            Toast.makeText(DetailsActivity.this, "Quý khách vui lòng Chọn lại. Chương trình Tuor bắt đầu từ ngày " + textDateTime, Toast.LENGTH_SHORT).show();
                                view.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),this);
                        }
                    }else if (textDate.toLowerCase().contains("NHPT08".toLowerCase())||textDate.toLowerCase().contains("NHPT12".toLowerCase())||textDate.toLowerCase().contains("NHPT16".toLowerCase())) {
                            if (selectedDayOfWeek != 4) {
                            // Nếu không là thứ 4, không cho phép chọn
                            Toast.makeText(DetailsActivity.this, "Quý khách vui lòng Chọn lại. Chương trình Tuor bắt đầu từ ngày " + textDateTime, Toast.LENGTH_SHORT).show();
                                view.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),this);
                        }
                    }else if (textDate.toLowerCase().contains("NHPT13".toLowerCase())||textDate.toLowerCase().contains("NHPT19".toLowerCase())) {
                            if (selectedDayOfWeek !=2) {
                            // Nếu không là thứ 2, không cho phép chọn
                            Toast.makeText(DetailsActivity.this, "Quý khách vui lòng Chọn lại. Chương trình Tuor bắt đầu từ ngày " + textDateTime, Toast.LENGTH_SHORT).show();
                                view.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),this);
                        }
                    }else if (textDate.toLowerCase().contains("NHPT15".toLowerCase())||textDate.toLowerCase().contains("NHPT11".toLowerCase())) {
                            if (selectedDayOfWeek !=6) {
                            // Nếu không là thứ 6, không cho phép chọn
                            Toast.makeText(DetailsActivity.this, "Quý khách vui lòng Chọn lại. Chương trình Tuor bắt đầu từ ngày " + textDateTime, Toast.LENGTH_SHORT).show();
                                view.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),this);
                        }
                    }
                }
            });
        }
        datePickerDialog.show();
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
    public String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(10);
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }
    public void booking(View view){
        String Date = chonngay.getText().toString().trim();
        String ten = getIntent().getStringExtra("placeName");
        String ha = getIntent().getStringExtra("imageUrl");
        String matour = getIntent().getStringExtra("maTour");
        int sluong= Integer.parseInt(nglon.getText().toString());
        int sluong1= Integer.parseInt(treem.getText().toString());
        String id=generateRandomString();
        long gia = Long.parseLong(getIntent().getStringExtra("money"));
        long giamoi = sluong * gia + sluong1 * gia / 2;

        payTourData payTourData=new payTourData(id,ten,String.valueOf(giamoi),ha,Date,String.valueOf(sluong),String.valueOf(sluong1),matour);

        if(sluong>0 ) {
            if(!Date.isEmpty()) {
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
                                    myRef.child(id).setValue(payTourData);
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Xảy ra lỗi trong quá trình đọc dữ liệu
                    }
                });
                Intent intent = new Intent(DetailsActivity.this, payTour.class);
                startActivity(intent);
            }else {
                Toast.makeText(DetailsActivity.this, "Vui lòng chọn ngày đi!", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(DetailsActivity.this,"Vui lòng nhập số lượng người đi!",Toast.LENGTH_LONG).show();
        }
    }


    public void hideSoftKeyborard(){
        try{
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

}
