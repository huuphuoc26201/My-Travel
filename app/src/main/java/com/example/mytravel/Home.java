package com.example.mytravel;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.adapter.PhotoviewPagerAdapter;
import com.example.adapter.TourPlacesAdapter;
import com.example.adapter.ZoomOutPageTransformer;
import com.example.adapter.homeHotelAdapter;
import com.example.adapter.tourLikeAdapter;
import com.example.adapter.whyAdapter;
import com.example.model.Photo;
import com.example.model.TourData;
import com.example.model.hoTelData;
import com.example.model.tourLikeData;
import com.example.model.whydata;
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
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class Home extends AppCompatActivity {

    RecyclerView  recyclerView,rcvhotel,rcv_why,rcv_like;
    TourPlacesAdapter tourAdapter;
    homeHotelAdapter homehotelAdapter;
    private whyAdapter whyadapter;
    ArrayList<TourData> tourDataArrayList;
    ArrayList<hoTelData> hotelDataArrayList;
    private ImageView imageView;
    BottomNavigationView nav;
    private TextView seeall;
    private TextView search,countCart;
    private Button hotel,maybay,amthuc;

    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCircleIndicator3;
    private List<Photo> mListPhoto;
    private Handler mHandler =new Handler();
    private Runnable mRunnable =new Runnable() {
        @Override
        public void run() {
            if(mViewPager2.getCurrentItem()==mListPhoto.size()-1){
                mViewPager2.setCurrentItem(0);
            }else {
                mViewPager2.setCurrentItem(mViewPager2.getCurrentItem() + 1);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imageView=(ImageView) findViewById(R.id.imageView);
        nav=findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.home);
        mViewPager2=findViewById(R.id.viewPager);
        mCircleIndicator3=findViewById(R.id.circleIndicator);
        mListPhoto=getListPhoto();
        hotel=(Button) findViewById(R.id.hotel);
        countCart=findViewById(R.id.count);
        maybay=(Button) findViewById(R.id.maybay);
        amthuc=(Button) findViewById(R.id.amthucviet);
        seeall=(TextView) findViewById(R.id.textView4);
        search=(TextView) findViewById(R.id.editText);

        numberCart();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,Account.class);
                startActivity(intent);
            }
        });


        rcv_why=(RecyclerView) findViewById(R.id.rcv_why);
        whyadapter = new whyAdapter(this);

        LinearLayoutManager linearLayout_Manager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rcv_why.setLayoutManager(linearLayout_Manager);
        whyadapter.setData(getListWhy());
        rcv_why.setAdapter(whyadapter);

        rcv_like=(RecyclerView) findViewById(R.id.rcv_like);
        tourLikeAdapter tourLikeAdapter = new tourLikeAdapter(this);
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        rcv_like.setLayoutManager(linearLayoutManager1);
        tourLikeAdapter.setData(getListLike());
        rcv_like.setAdapter(tourLikeAdapter);
        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,Hotel.class);
                startActivity(intent);
            }
        });
        amthuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,webViewHotel.class);
                startActivity(intent);
            }
        });
        maybay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,webViewAir.class);
                startActivity(intent);
            }
        });
        seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,Tour.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,Tour.class);
                startActivity(intent);
            }
        });



        PhotoviewPagerAdapter adapter=new PhotoviewPagerAdapter(mListPhoto);
        mViewPager2.setAdapter(adapter);

        mCircleIndicator3.setViewPager(mViewPager2);

        mHandler.postDelayed(mRunnable,3000);
        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable,3000);
            }
        });
        mViewPager2.setPageTransformer(new ZoomOutPageTransformer());

        recyclerView=findViewById(R.id.recent_recycler);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tourAdapter);

        clearAll();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Tours");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAll();

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    TourData tourData=snapshot.getValue(TourData.class);
                    if(tourData.getPlaceName().contains("."))
                        tourDataArrayList.add(tourData);
                }
                tourAdapter=new TourPlacesAdapter(getApplicationContext(),tourDataArrayList);
                recyclerView.setAdapter(tourAdapter);
                tourAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this,"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });


        rcvhotel=findViewById(R.id.rcv_hotel);

        RecyclerView.LayoutManager linearlayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rcvhotel.setLayoutManager(linearlayoutManager);
        rcvhotel.setAdapter(homehotelAdapter);

        clearAll();
        DatabaseReference database_Reference=FirebaseDatabase.getInstance().getReference("Hotel");
        database_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAllhotel();

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    hoTelData hoTeldata =snapshot.getValue(hoTelData.class);
                    hotelDataArrayList.add(hoTeldata);
                }
                homehotelAdapter=new homeHotelAdapter(getApplicationContext(),hotelDataArrayList);
                rcvhotel.setAdapter(homehotelAdapter);
                homehotelAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this,"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });



        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Toast.makeText(Home.this,"Home",Toast.LENGTH_LONG).show();
                        return true;


                    case R.id.tour:
                        Toast.makeText(Home.this,"Tour",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),Tour.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.booking:
                        Toast.makeText(Home.this,"Booking",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),payTour.class));
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.news:
                        Toast.makeText(Home.this,"News",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),News.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        Toast.makeText(Home.this,"Profile",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),Account.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
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

    private List<Photo> getListPhoto(){
        List<Photo> list=new ArrayList<>();
        list.add(new Photo(R.drawable.phoco_hoian));
        list.add(new Photo(R.drawable.bien_phuquoc));
        list.add(new Photo(R.drawable.codo_hue));
        list.add(new Photo(R.drawable.nhatho_hcm));
        list.add(new Photo(R.drawable.nhatho_dalat));
        list.add(new Photo(R.drawable.tour21_h1));
        list.add(new Photo(R.drawable.tour17_h1));
        list.add(new Photo(R.drawable.tour12_h1));
        list.add(new Photo(R.drawable.h3_tuor8));
        list.add(new Photo(R.drawable.tour2_h1));
        list.add(new Photo(R.drawable.tour1_h1));
        return list;
    }
    private List<whydata> getListWhy() {
        List<whydata> list = new ArrayList<>();
        list.add(new whydata(R.drawable.ic_baseline_cell_wifi_24,"Mạng bán tour","Đầu tiên tại Việt Nam\nỨng dụng công nghệ mới nhất"));
        list.add(new whydata(R.drawable.ic_baseline_monetization_on_24,"Giá cả","Luôn có mức giá tốt nhất"));
        list.add(new whydata(R.drawable.ic_baseline_payment_24,"Thanh toán","An toàn & linh hoạt"));
        list.add(new whydata(R.drawable.ic_baseline_library_books_24,"Sản phẩm & dịch dụ","Đa dạng - Chất lượng - An toàn"));
        list.add(new whydata(R.drawable.ic_baseline_book_online_24,"Đặt tour","Dễ dàng & nhanh chóng với những thao tác đơn giản"));
        list.add(new whydata(R.drawable.ic_baseline_contact_phone_24,"Hỗ trợ","Hotline & trực tuyến (08h00 - 22h00)"));
        return list;
    }

    private List<tourLikeData> getListLike() {
        List<tourLikeData> list = new ArrayList<>();
        list.add(new tourLikeData(R.drawable.tour21_h1,"HÀ NỘI","172,072 Lượt khách"));
        list.add(new tourLikeData(R.drawable.tour17_h1,"ĐÀ NẴNG","80,779 lượt khách"));
        list.add(new tourLikeData(R.drawable.nhatho_dalat,"ĐÀ LẠT","50,735 lượt khách"));
        list.add(new tourLikeData(R.drawable.bien_phuquoc,"PHÚ QUỐC","78,510 lượt khách"));
        list.add(new tourLikeData(R.drawable.tour12_h1,"NHA TRANG","70,120 lượt khách"));
        list.add(new tourLikeData(R.drawable.codo_hue,"HUẾ","60,262 lượt khách"));
        return list;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable,3000);
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
    private void clearAllhotel() {
        if(hotelDataArrayList!=null){
            hotelDataArrayList.clear();
            if(homehotelAdapter!=null){
                homehotelAdapter.notifyDataSetChanged();
            }
        }
        hotelDataArrayList=new ArrayList<>();
    }
}