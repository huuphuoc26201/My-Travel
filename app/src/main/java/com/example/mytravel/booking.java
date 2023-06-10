package com.example.mytravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adapter.gioHangAdapter;
import com.example.model.gioHangData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class booking extends AppCompatActivity {
    ListView lvgiohang;
    TextView txtThongBao,total;
    Button thanhtoan,tieptuc;
    gioHangAdapter giohangAdapter;
    BottomNavigationView nav;
    ArrayList<gioHangData> arraygiohang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        nav = findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.booking);
        anhxa();
        checkData();
        EvenUltil();
        deletetour();
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(booking.this, "Home", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0, 0);
                        return true;


                    case R.id.tour:
                        Toast.makeText(booking.this, "Tour", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), Tour.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.booking:
                        Toast.makeText(booking.this, "Booking", Toast.LENGTH_LONG).show();

                        return true;


                    case R.id.news:
                        Toast.makeText(booking.this, "News", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), News.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profile:
                        Toast.makeText(booking.this, "Profile", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(getApplicationContext(), Account.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });
    }

    private  void loadData(){
        arraygiohang= (ArrayList<gioHangData>) giohangDatabase.getInstance(this).userDao().getListGiohang();
        giohangAdapter.setData(arraygiohang);
    }
    private void deletetour() {
        lvgiohang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(booking.this);
                builder.setTitle("Xác nhận xóa tour du lịch");
                builder.setMessage("Bạn  có chắc chắn muốn xóa tour du lịch này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Home.manggiohang.size()<=0){
                            txtThongBao.setVisibility(View.VISIBLE);
                        }else {
                            gioHangData giohangdata = Home.manggiohang.get(position);
                            giohangDatabase.getInstance(booking.this).userDao().deleteGiohang(giohangdata);
                            Home.manggiohang.remove(position);
                            loadData();
                            giohangAdapter.notifyDataSetChanged();
                            EvenUltil();
                            if(Home.manggiohang.size()<=0){
                                txtThongBao.setVisibility(View.VISIBLE);
                            }else {
                                txtThongBao.setVisibility(View.INVISIBLE);
                                giohangAdapter.notifyDataSetChanged();
                                EvenUltil();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        giohangAdapter.notifyDataSetChanged();
                        EvenUltil();
                    }
                });
                builder.show();
                return true;
            }
        });
    }


    private void EvenUltil() {
        loadData();
        long tongtien=0;
        for(int i=0;i< Home.manggiohang.size();i++){
            tongtien+=Home.manggiohang.get(i).getGiatour();
        }
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        total.setText(decimalFormat.format(tongtien)+" Đ");
    }


    private void checkData() {
        if(Home.manggiohang.size()<=0){
            giohangAdapter.notifyDataSetChanged();
            txtThongBao.setVisibility(View.VISIBLE);
            lvgiohang.setVisibility(View.INVISIBLE);
        }else {
            giohangAdapter.notifyDataSetChanged();
            txtThongBao.setVisibility(View.INVISIBLE);
            lvgiohang.setVisibility(View.VISIBLE);
        }
    }



    private void anhxa() {
        lvgiohang=(ListView) findViewById(R.id.recyclerview);
        txtThongBao=(TextView) findViewById(R.id.textView30);
        total=(TextView) findViewById(R.id.total);
        thanhtoan=(Button) findViewById(R.id.buttonthanhtoan);
        tieptuc=(Button) findViewById(R.id.buttontieptuc);

        arraygiohang= (ArrayList<gioHangData>) giohangDatabase.getInstance(this).userDao().getListGiohang();
        giohangAdapter=new gioHangAdapter(booking.this,arraygiohang);
        lvgiohang.setAdapter(giohangAdapter);

        giohangAdapter.setData(arraygiohang);

        tieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(booking.this, Tour.class);
                startActivity(intent);
            }
        });



        thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Home.manggiohang.size()<=0){
                    Toast.makeText(booking.this,"Giỏ hàng của bạn trống không thể thanh toán",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent=new Intent(booking.this, orderDetails.class);
                    startActivity(intent);
                }
            }
        });

    }
}