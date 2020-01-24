package com.gharibyan.razmik.instagramdataloader;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().toString();

    private final String authorizeUrl = "https://api.instagram.com/oauth/authorize?app_id=460485674626498&redirect_uri=https://github.com/R43M1K&scope=user_profile,user_media&response_type=code";
    private final String redirectFullBeforeCode = "https://github.com/R43M1K?code=";
    private final String redirectBeforeCode = "R43M1K?code=";
    private final String redirectAfterCode = "#_";


    private String code;

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.web_view);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains(redirectFullBeforeCode)) {
                    Log.d(TAG, url);
                    int index = url.lastIndexOf(redirectBeforeCode) + redirectBeforeCode.length();
                    int hashTagIndex = url.lastIndexOf(redirectAfterCode);
                    code = url.substring(index, hashTagIndex);
                    Log.d(TAG, code);
                }
                return false; //Allow WebView to load url
            }
        });

        webView.loadUrl(authorizeUrl);

    }
}
