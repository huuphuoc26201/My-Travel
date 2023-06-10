package com.example.mytravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class webViewHotel extends AppCompatActivity {

    private WebView mWebView;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_hotel);

        //assignviewbyid
        mWebView = findViewById(R.id.webview);
        back=(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(webViewHotel.this,Home.class);
                startActivity(intent);
            }
        });

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://webamthuc.vn/");
        //https://www.elines.vn/

    }
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack())
        {
            mWebView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}