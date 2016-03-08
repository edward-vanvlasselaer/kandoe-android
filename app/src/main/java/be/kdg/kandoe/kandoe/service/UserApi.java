package be.kdg.kandoe.kandoe.service;

import android.telecom.Call;

import be.kdg.kandoe.kandoe.dom.User;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.Callback;
import retrofit.http.Query;
import retrofit2.http.GET;

/**
 * Created by Edward on 05/03/2016.
 */

public interface UserApi {
    @POST("/user/authenticate")
    void login(@Query("username") String username,
               @Query("password") String password,
               Callback<?> token);

    @GET("/user/current")
    void getCurrentUser(Callback<?> user);

    @POST("/user/register")
    void registerUser(@Body User user, Callback<String> token);
}
