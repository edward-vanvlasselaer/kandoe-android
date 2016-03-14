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
    @POST("/user/authenticate")
    Call<Token> login(@Query("username") String username,
                      @Query("password") String password);

    @GET("/user/current")
    retrofit.Call<User> getCurrentUser();

    @POST("/user/register")
    Call<Token> registerUser(@Body User user);
}
