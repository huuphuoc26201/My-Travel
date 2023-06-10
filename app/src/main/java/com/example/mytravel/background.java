package com.example.mytravel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.data_local.MySharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class background extends AppCompatActivity {

    private static final String KEY_FIRST_INSTALL="KEY_FIRST_INSTALL";
    private static int Splash_Screen=5000;

    Animation Top,Bot;
    private TextView logo,slogan;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_background);

        final MySharedPreferences mySharedPreferences=new MySharedPreferences(this);
        logo = (TextView) findViewById(R.id.textView2);
        slogan = (TextView) findViewById(R.id.textView);

        Top = AnimationUtils.loadAnimation(this, R.drawable.top);
        Bot = AnimationUtils.loadAnimation(this, R.drawable.bot);
//Set animation to elements
        logo.setAnimation(Top);
        slogan.setAnimation(Bot);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mySharedPreferences.getBooleanValue(KEY_FIRST_INSTALL)){
                    netActivity();
                }
                else {
                    Intent intent = new Intent(background.this, MainActivity.class);
                    startActivity(intent);
                    mySharedPreferences.putBooleanValue(KEY_FIRST_INSTALL,true);
                }
            }
        },Splash_Screen);

    }

    private void netActivity() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            //chưa login
            Intent intent = new Intent(background.this, Login.class);
            startActivity(intent);
        }else{
            //đã login
            Intent intent = new Intent(background.this, Home.class);
            startActivity(intent);
        }
        finish();
    }
}