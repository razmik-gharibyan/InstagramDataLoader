package com.gharibyan.razmik.instagramdataloader.repository.api;

import com.gharibyan.razmik.instagramdataloader.repository.model.UserInfo;
import com.gharibyan.razmik.instagramdataloader.repository.model.UserToken;
import com.gharibyan.razmik.instagramdataloader.repository.model.UserTokenLong;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InstagramPlaceHolderApi {

    @FormUrlEncoded
    @POST("https://api.instagram.com/oauth/access_token")
    Call<UserToken> getToken(
            @Field("app_id") Long appId,
            @Field("app_secret") String appSecret,
            @Field("grant_type") String grantType,
            @Field("redirect_uri") String redirectUrl,
            @Field("code") String code
    );

    @GET("https://graph.instagram.com/access_token")
    Call<UserTokenLong> getLongToken (
            @Query("grant_type") String grantType,
            @Query("client_secret") String clientSecret,
            @Query("access_token") String accessToken
    );

    @GET("https://graph.instagram.com/{user_id}")
    Call<UserInfo> getUserInfo(
            @Path("user_id") Long userId,
            @Query("fields") String fields,
            @Query("access_token") String accessToken
    );

    @GET("https://www.instagram.com/{username}")
    Call<String> getUserPersonal(
            @Path("username") String username,
            @Query("__a") Integer a
    );

}
