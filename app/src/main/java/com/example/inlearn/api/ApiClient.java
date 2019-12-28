package com.example.inlearn.api;

import android.content.Context;

import com.example.inlearn.utils.SharedPreferencedHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
//
//    private static final String BASE_URL_API = "http://10.0.2.2:8000/";

//
    private static final String BASE_URL_API = "https://inlearnmobileapp.000webhostapp.com/";

    public static String getAPI(){
        return BASE_URL_API+ "api/";
    }



    public static ApiService getService(Context context){
        final SharedPreferencedHelper sharedPreferencedHelper = new SharedPreferencedHelper(context);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client=new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request;
                        if (sharedPreferencedHelper.getSPSudahLogin()){
                            request=chain
                                    .request()
                                    .newBuilder()
                                    .addHeader("Content-Type","application/json")
                                    .addHeader("Authorization","Bearer "+sharedPreferencedHelper.getSpToken())
                                    .build();
                        }else {
                            request=chain
                                    .request()
                                    .newBuilder()
                                    .addHeader("Content-Type","application/json")
                                    .build();
                        }
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getAPI())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)

                .build();

        return retrofit.create(ApiService.class);
    }
}
