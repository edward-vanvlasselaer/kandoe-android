package be.kdg.kandoe.kandoe.service;

import java.util.List;

import be.kdg.kandoe.kandoe.dom.Circle;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by claudiu on 15/03/16.
 */
public interface CircleApi {
    @GET("/api/circle/{id}")
    Call<Circle> getCircle(@Path("id") int id);

    @POST("/api/circle/{id}")
    Call<Circle> updateCircle(@Body Circle circle, int id);

}
