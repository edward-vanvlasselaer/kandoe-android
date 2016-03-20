package be.kdg.kandoe.kandoe.service;

import java.util.List;
import java.util.Objects;

import be.kdg.kandoe.kandoe.dom.Card;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * This interface is used to call the RESTful API for Cards
 */
public interface CardApi {

    /**
     * @param id identifier of the theme.
     * @return List of card Objects of the same theme.
     */
    @GET("/api/card/byTheme/{id}")
    Call<List<Card>> getCardsByTheme(@Path("id") int id);

    /**
     * @param id identifier of the theme.
     * @return card Object for the given id
     */
    @GET("/api/card/{id}")
    Call<Card> getCard(@Path("id") int id);

    /**
     * @param card card to be updated
     * @return the updated card Object.
     */
    @POST("/api/card")
    Call<Card> updateCard(@Body Card card);

    /**
     * @param themeId identifier of the theme.
     * @param card to be added to the circle.
     * @return empty response body - can contain HTTP.OK (200) response.
     */
    @POST("/api/theme/{themeId}/card")
    Call<Object> addCardToCircle(@Path("themeId") int themeId, @Body Card card);
}
