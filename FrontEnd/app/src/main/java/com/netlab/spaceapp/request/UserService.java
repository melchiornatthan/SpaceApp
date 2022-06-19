package com.netlab.spaceapp.request;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Class untuk menginisiasikan jenis jenis request yang akan dilakukan
 */
public interface UserService {
    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> register(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("additem")
    Call<ResponseBody> additem(@Field("name") String name, @Field("quantity") String quantity, @Field("description") String description);

    @FormUrlEncoded
    @POST("transferitem")
    Call<ResponseBody> transferitem(@Field("itemid") String itemid ,@Field("receiverid") String receiverid,@Field("quantity") String quantity);

    @FormUrlEncoded
    @PUT("updateitem")
    Call<ResponseBody> updateitem(@Field("itemid") String itemid, @Field("name") String name, @Field("quantity") String quantity, @Field("description") String description);

    @FormUrlEncoded
    @PUT("updatetransaction")
    Call<ResponseBody> updatetransaction(@Field("transactionid") String transactionid);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "deleteitem", hasBody = true)
    Call<ResponseBody> deleteitem(@Field("itemid") String itemid);

    @GET("getitems")
    Call<ResponseBody> getitems();

    @FormUrlEncoded
    @POST("searchitem")
    Call<ResponseBody> searchitem(@Field("key") String key);

    @GET("gettransaction")
    Call<ResponseBody> gettransaction();

    @GET("getusers")
    Call<ResponseBody> getUser();
}
