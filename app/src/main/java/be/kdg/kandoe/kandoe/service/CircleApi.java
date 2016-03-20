package be.kdg.kandoe.kandoe.service;

import java.util.List;

import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Chat;
import be.kdg.kandoe.kandoe.dom.Circle;
import be.kdg.kandoe.kandoe.dom.Theme;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * This interface is used to call the RESTful API for Circles
 */
public interface CircleApi {

    /**
     * @param id identifier of the circle.
     * @return circleObject for the given circleId.
     */
    @GET("/api/circle/{id}")
    Call<Circle> getCircle(@Path("id") int id);

    /**
     * @param circleId identifier of the circle.
     * @param card     identifier of the card.
     * @return empty response body - can contain HTTP.OK (200) response.
     */
    @POST("/api/circle/{circleId}/vote")
    Call<Object> addVote(@Path("circleId") int circleId, @Body Card card);

    /**
     * @param circleId identifier of the circle.
     * @param chat     that has been updated/changed.
     * @return empty response body - can contain HTTP.OK (200) response.
     */
    @POST("/api/circle/{circleId}/chat")
    Call<Object> addChatMessage(@Path("circleId") int circleId, @Body Chat chat);

    /**
     * @param circleId identifier of the circle.
     * @return List of chat Objects for the fiven circleId.
     */
    @GET("/api/circle/{circleId}/chat")
    Call<List<Chat>> getChatMessages(@Path("circleId") int circleId);

    /**
     * @param link link or url to join a circle.
     * @return theme Object.
     */
    @POST("api/link/joincircle/{link}")
    Call<Theme> joinCircleByLink(@Path("link") String link);
}
