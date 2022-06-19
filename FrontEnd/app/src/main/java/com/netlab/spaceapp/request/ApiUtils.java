package com.netlab.spaceapp.request;

/**
 * Berfungsi untuk mengkonfigurasi route basic yang akan digunakan dalam request ke API
 */
public class ApiUtils {
    public static final String BASE_URL_API = "http://10.0.2.2:3333/spaceapp/";

    public static UserService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(UserService.class);
    }
}
