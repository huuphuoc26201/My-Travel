package com.example.mytravel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Intro extends AppCompatActivity {
    private ImageView back;
    private TextView phone;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        back=(ImageView) findViewById(R.id.back);
        phone=(TextView) findViewById(R.id.phone);


        // Quay lại
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intro.this, Account.class);
                startActivity(intent);
            }
        });
        //Gọi liên hệ
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdt = "0943101434";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + sdt));
                if (ContextCompat.checkSelfPermission(Intro.this, android.Manifest.permission.CALL_PHONE) != getPackageManager().PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Intro.this,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    return;
                }
                startActivity(intent);
            }
        });
    }
}