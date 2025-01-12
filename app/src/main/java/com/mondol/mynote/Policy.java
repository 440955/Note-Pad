package com.mondol.mynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class Policy extends AppCompatActivity {
    public static String OPERATE="";
    WebView webView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        getWindow().setNavigationBarColor(ContextCompat.getColor(Policy.this, R.color.statusbar));
        getWindow().setStatusBarColor(ContextCompat.getColor(Policy.this, R.color.statusbar));

        webView= findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        progressBar= findViewById(R.id.progressBar);

        if (OPERATE.contains("PrivacyPolicy")){
            webView.loadUrl("https://sites.google.com/view/mynoteapp44/home");
        } else if (OPERATE.contains("Developer")) {
            webView.loadUrl("https://sites.google.com/view/aboutdeveloperallapps/home");
        }
        else {
            onBackPressed();
        }



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        },5000);
    }
}