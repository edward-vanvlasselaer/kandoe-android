package be.kdg.kandoe.kandoe.application;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import be.kdg.kandoe.kandoe.service.UserApi;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;


public class KandoeApplication extends Application{
    private static UserApi userApi;


    @Override
    public void onCreate() {
        super.onCreate();
        Iconify
                .with(new FontAwesomeModule());
        userApi = createUserApi();
    }

    public static UserApi getUserApi(){
        return userApi;
    }
    private UserApi createUserApi() {
        final Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        return new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint("http://wildfly-teamgip2kdgbe.rhcloud.com/api") //TODO zet nr string.xml of config.xml
                .setLogLevel(RestAdapter.LogLevel.FULL) // Om af te drukken welke http-calls er effectief gebeuren
                .build()
                .create(UserApi.class);
    }
}
