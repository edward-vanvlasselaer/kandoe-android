package be.kdg.kandoe.kandoe.service;

import org.json.JSONObject;

import be.kdg.kandoe.kandoe.dom.Token;
import be.kdg.kandoe.kandoe.dom.User;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * This interface is used to call the RESTful API for users
 */
public interface UserApi {
    /**
     * @param username of the user to be logged in
     * @param password of the user to be loggin in
     * @return token Object to authenticate the user during the usage of the application
     */
    @POST("/api/user/authenticate")
    Call<Token> login(@Query("username") String username,
                      @Query("password") String password);

    /**
     * @return user Object of the current user that has been logged in (only works after login() has been used succesfully)
     */
    @GET("/api/user/current")
    Call<User> getCurrentUser();

    /**
     * TO BE IMPLEMENTED
     * @return empty response body - can contain HTTP.OK (200) response.
     */
    @GET("/api/user/logout")
    Call<Object> logout();

    /**
     * registers and automatically logins the user.
     * @param user to be registered
     * @return token Object to authenticate the user during the usage of the application
     */
    @POST("/api/user/register")
    Call<Token> registerUser(@Body User user);

    /**
     * @param user that wants to join as a guest
     * @return token Object to authenticate the user during the usage of the application
     */
    @POST("/api/user/registerguest")
    Call<Token> registerGuest(@Body User user);
}
