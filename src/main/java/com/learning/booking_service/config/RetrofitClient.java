package com.learning.booking_service.config;


import com.learning.booking_service.client.LocationServiceApi;
import com.netflix.discovery.EurekaClient;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitClient {
    private final EurekaClient eurekaClient;

    public RetrofitClient(EurekaClient eurekaClient){
        this.eurekaClient = eurekaClient;
    }

    public String getUrl(String serviceName){
        return eurekaClient.getNextServerFromEureka(serviceName,false).getHomePageUrl();
    }

    @Bean
    public LocationServiceApi createRetroFitClient(){
        return new Retrofit.Builder()
                .baseUrl(getUrl("LOCATION-SERVICE"))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(LocationServiceApi.class);

    }

}
