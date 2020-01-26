package com.gharibyan.razmik.instagramdataloader.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.gharibyan.razmik.instagramdataloader.R;
import com.gharibyan.razmik.instagramdataloader.editor.ImageURLProcessing;
import com.gharibyan.razmik.instagramdataloader.repository.api.InstagramPlaceHolderApi;
import com.gharibyan.razmik.instagramdataloader.repository.model.personal.ObjectResponse;
import com.gharibyan.razmik.instagramdataloader.repository.model.UserInfo;
import com.gharibyan.razmik.instagramdataloader.repository.model.UserToken;
import com.gharibyan.razmik.instagramdataloader.repository.model.UserTokenLong;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private InstagramPlaceHolderApi instagramPlaceHolderApi;

    private ImageURLProcessing imageURLProcessing;

    private final String TAG = getClass().toString();

    private final String authorizeUrl = "https://api.instagram.com/oauth/authorize?app_id=460485674626498&redirect_uri=https://github.com/R43M1K&scope=user_profile,user_media&response_type=code";
    private final String redirectFullBeforeCode = "https://github.com/R43M1K?code=";
    private final String redirectBeforeCode = "R43M1K?code=";
    private final String redirectAfterCode = "#_";

    private String code;

    private WebView webView;
    private TextView textView;
    private ImageView imageView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.web_view);
        textView = findViewById(R.id.text_view);
        imageView = findViewById(R.id.image_view);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.instagram.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        instagramPlaceHolderApi = retrofit.create(InstagramPlaceHolderApi.class);
        imageURLProcessing = new ImageURLProcessing();

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
                    getTokenByCode(code);
                }
                return false; //Allow WebView to load url
            }
        });

        webView.loadUrl(authorizeUrl);

    }

    private void getTokenByCode(String code) {
        Call<UserToken> call = instagramPlaceHolderApi.getToken(460485674626498L,
                "0962b86387a8461431728427dfb3a9e6",
                "authorization_code",
                "https://github.com/R43M1K",
                code
                );
        call.enqueue(new Callback<UserToken>() {
            @Override
            public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, response.message());
                    return;
                }

                UserToken userToken = response.body();
                String content = ""
                        + "User ID: " + userToken.getUser_id() + "\n"
                        + "Access Token: " + userToken.getAccess_token();
                Log.d(TAG, content);

                getLongToken(userToken.getAccess_token(), userToken.getUser_id());
            }

            @Override
            public void onFailure(Call<UserToken> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void getLongToken(String shortToken, final Long userId) {
        Call<UserTokenLong> call = instagramPlaceHolderApi.getLongToken("ig_exchange_token",
                "0962b86387a8461431728427dfb3a9e6",
                shortToken);
        call.enqueue(new Callback<UserTokenLong>() {
            @Override
            public void onResponse(Call<UserTokenLong> call, Response<UserTokenLong> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, response.message());
                    return;
                }

                UserTokenLong userTokenLong = response.body();
                String content = ""
                        + "Access Token: " + userTokenLong.getAccess_token() + "\n"
                        + "Token Type: " + userTokenLong.getToken_type() + "\n"
                        + "Expires In: " + userTokenLong.getExpires_in();
                Log.d(TAG, content);
                getUserInfo(userId, userTokenLong.getAccess_token());
            }

            @Override
            public void onFailure(Call<UserTokenLong> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void getUserInfo(final Long userId, String accessToken) {
        Call<UserInfo> call = instagramPlaceHolderApi.getUserInfo(userId,
                "account_type,id,media_count,username",accessToken);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, response.message());
                    return;
                }

                UserInfo userInfo = response.body();
                String content = ""
                        + "Account Type: " + userInfo.getAccount_type() + "\n"
                        + "User Id: " + userInfo.getId() + "\n"
                        + "Media Count: " + userInfo.getMedia_count() + "\n"
                        + "Username: " + userInfo.getUsername();
                Log.d(TAG, content);
                getUserPersonal(userInfo.getUsername());

                webView.setVisibility(View.GONE);
                textView.setText(userInfo.getUsername());
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void getUserPersonal(String username) {
        Call<ObjectResponse> call = instagramPlaceHolderApi.getUserPersonal(username,1);
        call.enqueue(new Callback<ObjectResponse>() {
            @Override
            public void onResponse(Call<ObjectResponse> call, Response<ObjectResponse> response) {
                if(!response.isSuccessful()) {
                    Log.d(TAG, response.message());
                    return;
                }

                ObjectResponse objectResponse = response.body();

                String profilePicUrlHD = objectResponse.getGraphql().getUser().getProfilePicUrlHD();
                Long followersCount = objectResponse.getGraphql().getUser().getEdgeFollowedBy().getFollowersCount();

                String content = ""
                        + "Profile Picture URL: " + profilePicUrlHD + "\n"
                        + "Followers Count: " + followersCount;

                textView.append("Followers Count: " + followersCount);
                try {
                    Bitmap bitmap = imageURLProcessing.execute(profilePicUrlHD).get();
                    imageView.setImageBitmap(bitmap);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, content);
            }

            @Override
            public void onFailure(Call<ObjectResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }
}
