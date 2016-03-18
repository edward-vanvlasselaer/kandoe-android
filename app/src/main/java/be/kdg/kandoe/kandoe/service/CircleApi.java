package be.kdg.kandoe.kandoe.service;

import java.util.List;

import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Chat;
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

    //@POST("/api/circle/{id}")
    //Call<Circle> updateCircle(@Body Circle circle, int id);

    @POST("/api/circle/{circleId}/vote")
    Call<Object> addVote(@Path("circleId") int circleId, @Body Card card);

    @POST("/api/circle/{circleId}/chat")
    Call<Object> addChatMessage(@Path("circleId") int circleId, @Body Chat chat);

    @GET("/api/circle/{circleId}/chat")
    Call<List<Chat>> getChatMessages(@Path("circleId") int circleId);

    @POST("/api/circle/join/{link}")
    Call<Object> joinCircleByLink(@Path("link") String link);
}
