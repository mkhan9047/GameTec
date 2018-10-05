package app.gametec.com.gametec.Networking;

import app.gametec.com.gametec.Helper.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {




    public static Retrofit getRetrofitOfScalar(){

        Retrofit retrofit = null;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).
                    client(client).
                    addConverterFactory(ScalarsConverterFactory.create())
                    .build();


        return retrofit;
    }



    public static Retrofit getRetrofit(){

        Retrofit retrofit = null;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


            retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).
                    client(client).
                    addConverterFactory(GsonConverterFactory.create())
                    .build();


        return retrofit;
    }


}
