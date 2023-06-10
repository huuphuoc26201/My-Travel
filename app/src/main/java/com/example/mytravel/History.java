package com.example.mytravel;

import static com.example.mytravel.Home.manggiooder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adapter.historyAdapter;
import com.example.model.orderDetailsData;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    ListView lvgiohang;
    historyAdapter giohangAdapter;
    ImageView back;
    ArrayList<orderDetailsData> arraygiohang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        lvgiohang=(ListView) findViewById(R.id.recyclerview);
        back=(ImageView) findViewById(R.id.back);

        arraygiohang= (ArrayList<orderDetailsData>) historyDatabase.getInstance(this).historyDao().getListHistory();

        giohangAdapter=new historyAdapter(History.this, arraygiohang);
        lvgiohang.setAdapter(giohangAdapter);

        giohangAdapter.setData(arraygiohang);

        giohangAdapter.notifyDataSetChanged();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(History.this,Account.class);
                startActivity(intent);
            }
        });
        deletetour();

    }



    private  void loadData(){
        arraygiohang= (ArrayList<orderDetailsData>) historyDatabase.getInstance(this).historyDao().getListHistory();
        giohangAdapter.setData(arraygiohang);
    }
    private void deletetour() {
        lvgiohang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(History.this);
                builder.setTitle("Xác nhận xóa lịch sử giao dịch");
                builder.setMessage("Bạn  có chắc chắn muốn xóa lịch sử giao dịch này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(manggiooder.size()<=0){
                           // txtThongBao.setVisibility(View.VISIBLE);
                        }else {
                            orderDetailsData orderDetailsData = manggiooder.get(position);
                            historyDatabase.getInstance(History.this).historyDao().deleteGiohang(orderDetailsData);
                            manggiooder.remove(position);
                            loadData();
                            giohangAdapter.notifyDataSetChanged();
                            if(manggiooder.size()<=0){
                                //txtThongBao.setVisibility(View.VISIBLE);
                            }else {
                                //txtThongBao.setVisibility(View.INVISIBLE);
                                giohangAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        giohangAdapter.notifyDataSetChanged();
                    }
                });
                builder.show();
                return true;
            }
        });
    }
}