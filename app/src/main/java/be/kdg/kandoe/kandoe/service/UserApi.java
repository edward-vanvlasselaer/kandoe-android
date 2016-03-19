package be.kdg.kandoe.kandoe.service;

import org.json.JSONObject;

import be.kdg.kandoe.kandoe.dom.Token;
import be.kdg.kandoe.kandoe.dom.User;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;


public interface UserApi {
    @POST("/api/user/authenticate")
    Call<Token> login(@Query("username") String username,
                      @Query("password") String password);

    @GET("/api/user/current")
    Call<User> getCurrentUser();

    @GET("/api/user/logout")
    Call<Object> logout();

    @POST("/api/user/register")
    Call<Token> registerUser(@Body User user);

    @POST("/api/user/registerguest")
    Call<Token> registerGuest(@Body User user);
}
