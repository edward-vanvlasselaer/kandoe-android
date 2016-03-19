package be.kdg.kandoe.kandoe.application;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;


import java.io.IOException;

import be.kdg.kandoe.kandoe.service.CardApi;
import be.kdg.kandoe.kandoe.service.CircleApi;
import be.kdg.kandoe.kandoe.service.OrganisationApi;
import be.kdg.kandoe.kandoe.service.UserApi;
import be.kdg.kandoe.kandoe.util.SharedStorage;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


public class KandoeApplication extends Application{
    private Retrofit retrofit;
    private static String userToken;

    private static OrganisationApi organisationApi;
    private static CardApi cardApi;
    private static UserApi userApi;
    private static CircleApi circleApi;
    private static Application app;

    public static String getUserToken() {
        if(userToken!=null)return userToken;
        userToken = new SharedStorage(app).getValue("token");
        return userToken;
    }

    public static void setUserToken(String userToken) {
        KandoeApplication.userToken = userToken;
        new SharedStorage(app).setValue("token",userToken);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Iconify.with(new FontAwesomeModule());
        createApi();
        userApi = createUserApi();
        organisationApi = createOrganisationApi();
        cardApi = createCardApi();
        circleApi=createCircleApi();
    }

    public static UserApi getUserApi(){
        return userApi;
    }

    public static OrganisationApi getOrganisationApi() {
        return organisationApi;
    }

    public static CardApi getCardApi() {
        return cardApi;
    }

    public static CircleApi getCircleApi(){
        return circleApi;
    }

    private void createApi() {
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request;

                if(getUserToken() != null)
                    request = chain.request()
                                .newBuilder()
                                .addHeader("X-Auth-Token", getUserToken())
                                .build();
                else
                    request = chain.request()
                                    .newBuilder()
                                    .build();

                return chain.proceed(request);
            }
        });
        client.interceptors().add(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://dolha.in:8080")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private UserApi createUserApi(){
        return retrofit.create(UserApi.class);
    }

    private OrganisationApi createOrganisationApi(){
        return retrofit.create(OrganisationApi.class);
    }

    private CardApi createCardApi(){
        return retrofit.create(CardApi.class);
    }

    private CircleApi createCircleApi(){
        return retrofit.create(CircleApi.class);
    }
}
