package com.example.mytravel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.model.hoTelData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class bookHotel extends AppCompatActivity {
    ImageView imageUrl,imageUrl1,imageUrl2,imageUrl3,imageUrl4,about,back;
    TextView name,diachi,danhgia,money;
    TextView book;
    RatingBar ratingbar;
    CircleImageView tym,huytym;
    int n=0;
    private static final int REQUEST_CALL = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookhotel);

        tym=(CircleImageView) findViewById(R.id.tim);
        huytym=(CircleImageView) findViewById(R.id.huytim);
        tym.setVisibility(View.VISIBLE);
        huytym.setVisibility(View.GONE);
        imageUrl=(ImageView) findViewById(R.id.imageView);
        imageUrl1=(ImageView) findViewById(R.id.imageView8);
        imageUrl2=(ImageView) findViewById(R.id.imageView9);
        imageUrl3=(ImageView) findViewById(R.id.imageView11);
        imageUrl4=(ImageView) findViewById(R.id.imageView10);
        back=(ImageView) findViewById(R.id.back);
        name=(TextView) findViewById(R.id.place_name);
        diachi=(TextView) findViewById(R.id.textView53);
        about=(ImageView) findViewById(R.id.about);
        danhgia=(TextView) findViewById(R.id.textView7);
        money=(TextView) findViewById(R.id.money);
        book=findViewById(R.id.button);
        ratingbar=(RatingBar) findViewById(R.id.ratingbar);

        thatym();

        LayerDrawable stars = (LayerDrawable) ratingbar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this, R.color.yelow), PorterDuff.Mode.SRC_ATOP);

        Glide.with(bookHotel.this).load(getIntent().getStringExtra("imageUrl"))
                .into(imageUrl);
        Glide.with(bookHotel.this).load(getIntent().getStringExtra("imageUrl"))
                .into(imageUrl1);
        Glide.with(bookHotel.this).load(getIntent().getStringExtra("imageUrl1"))
                .into(imageUrl2);
        Glide.with(bookHotel.this).load(getIntent().getStringExtra("imageUrl2"))
                .into(imageUrl3);
        Glide.with(bookHotel.this).load(getIntent().getStringExtra("imageUrl3"))
                .into(imageUrl4);
        Glide.with(bookHotel.this).load(getIntent().getStringExtra("about"))
                .into(about);
        name.setText(getIntent().getStringExtra("name"));
        diachi.setText(getIntent().getStringExtra("diachi"));
        danhgia.setText(getIntent().getStringExtra("danhgia"));
        money.setText(getIntent().getStringExtra("gia")+" / đêm");
        ratingbar.setRating(Float.parseFloat(getIntent().getStringExtra("danhgia")));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String back=getIntent().getStringExtra("back");
                Intent intent;
                if(back!=null){
                    intent = new Intent(bookHotel.this, Home.class);
                }else {
                    intent = new Intent(bookHotel.this, Hotel.class);
                }
                startActivity(intent);
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdt =getIntent().getStringExtra("phone") ;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + sdt));
                if (ContextCompat.checkSelfPermission(bookHotel.this, android.Manifest.permission.CALL_PHONE) != getPackageManager().PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(bookHotel.this,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    return;
                }
                startActivity(intent);
            }
        });
        diachi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address =getIntent().getStringExtra("vitri");
                Uri intentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (ContextCompat.checkSelfPermission(bookHotel.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(bookHotel.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                    return;
                }
                startActivity(mapIntent);
            }
        });
    }

    private void thatym() {
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
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        String childName = childSnapshot.child("name").getValue(String.class);
                                        if (childName.equals(getIntent().getStringExtra("name"))) {
                                            huytym.setVisibility(View.VISIBLE);
                                            tym.setVisibility(View.GONE);
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

    public void next(View view){
        n++;
        if(n%4==0){
            Glide.with(bookHotel.this).load(getIntent().getStringExtra("imageUrl"))
                    .into(imageUrl);
        }
        else if ((n+3)%4==0){
            Glide.with(bookHotel.this).load(getIntent().getStringExtra("imageUrl1"))
                    .into(imageUrl);
        }
        else if ((n+2)%4==0){
            Glide.with(bookHotel.this).load(getIntent().getStringExtra("imageUrl2"))
                    .into(imageUrl);
        }else if ((n+1)%4==0){
            Glide.with(bookHotel.this).load(getIntent().getStringExtra("imageUrl3"))
                    .into(imageUrl);
        }
    }
    public void prev(View view) {
        n--;
        if (n%4==0) {
            Glide.with(bookHotel.this).load(getIntent().getStringExtra("imageUrl"))
                    .into(imageUrl);
        }

        else if ((n+3)%4==0) {
            Glide.with(bookHotel.this).load(getIntent().getStringExtra("imageUrl1"))
                    .into(imageUrl);
        } else if ((n+2)%4==0){
            Glide.with(bookHotel.this).load(getIntent().getStringExtra("imageUrl2"))
                    .into(imageUrl);
        }
        else if ((n+1)%4==0){
            Glide.with(bookHotel.this).load(getIntent().getStringExtra("imageUrl3"))
                    .into(imageUrl);
        }
    }
    public void favorite(View view){
        huytym.setVisibility(View.VISIBLE);
        tym.setVisibility(View.GONE);
        hoTelData hoteldata=new hoTelData(getIntent().getStringExtra("name"),getIntent().getStringExtra("diachi"),getIntent().getStringExtra("phone"),getIntent().getStringExtra("vitri"),getIntent().getStringExtra("gia"),
                getIntent().getStringExtra("danhgia"),getIntent().getStringExtra("about"),getIntent().getStringExtra("imageUrl"),getIntent().getStringExtra("imageUrl1"),getIntent().getStringExtra("imageUrl2"),getIntent().getStringExtra("imageUrl3"));
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
                            String ename = getIntent().getStringExtra("name"); // Lấy giá trị thuộc tính ename của đối tượng User
                            myRef.child(ename).setValue(hoteldata);
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
    public void unfavorite(View view){
        tym.setVisibility(View.VISIBLE);
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
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Favorite").child(key).child("Hotel and Resort");
                            String ename = getIntent().getStringExtra("name"); // Lấy giá trị thuộc tính ename của đối tượng User
                            myRef.child(ename).removeValue();
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
        Intent intent=new Intent(bookHotel.this,orderHotel.class);
        String n1 = getIntent().getStringExtra("gia");
        n1 = n1.replace(".", ""); // Loại bỏ dấu chấm
        n1 = n1.replace(" VNĐ", ""); // Loại bỏ chữ VNĐ
        intent.putExtra("money",n1);
        intent.putExtra("imageUrl",getIntent().getStringExtra("imageUrl"));
        intent.putExtra("imageUrl1",getIntent().getStringExtra("imageUrl1"));
        intent.putExtra("imageUrl2",getIntent().getStringExtra("imageUrl2"));
        intent.putExtra("imageUrl3",getIntent().getStringExtra("imageUrl3"));
        intent.putExtra("about",getIntent().getStringExtra("about"));
        intent.putExtra("diachi",getIntent().getStringExtra("diachi"));
        intent.putExtra("name",getIntent().getStringExtra("name"));
        intent.putExtra("danhgia",getIntent().getStringExtra("danhgia"));
        intent.putExtra("gia",getIntent().getStringExtra("gia"));
        startActivity(intent);
    }
}