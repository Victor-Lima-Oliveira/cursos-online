package API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigRetrofit {
     private static final String URL_BASE = "http://192.168.0.113/sistemacurso/";

     private static Retrofit retrofit;

     static public Retrofit getRetrofit(){
          if(retrofit == null){
               retrofit = new Retrofit.Builder().baseUrl(URL_BASE)
                       .addConverterFactory(GsonConverterFactory.create())
                       .build();
          }
          return retrofit;
     }
}
