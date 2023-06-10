package com.example.mytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class Web_view extends AppCompatActivity {

    WebView webview;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        back=(ImageView) findViewById(R.id.back);
        webview=(WebView) findViewById(R.id.webView);
        Intent intent=getIntent();
        String duonglink=intent.getStringExtra("link");
        webview.loadUrl(duonglink);
        webview.setWebViewClient(new WebViewClient());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Web_view.this, News.class);
                startActivity(intent);
            }
        });
    }
}