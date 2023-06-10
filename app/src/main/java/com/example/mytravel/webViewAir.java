package com.example.mytravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class webViewAir extends AppCompatActivity {
    private WebView mWebView;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_air);
        mWebView = findViewById(R.id.webview);
        back=(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(webViewAir.this,Home.class);
                startActivity(intent);
            }
        });
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://www.skyscanner.com.vn/flights");

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