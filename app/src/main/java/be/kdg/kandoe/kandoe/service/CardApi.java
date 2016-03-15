package be.kdg.kandoe.kandoe.service;

import java.util.List;

import be.kdg.kandoe.kandoe.dom.Card;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;


/**
 * Created by Edward on 14/03/2016.
 */
public interface CardApi {
    @GET("/api/card/byTheme/{id}")
    Call<List<Card>> getCardsByTheme(@Path("id") int id);

    @GET("/api/card/{id}")
    Call<Card> getCard(@Path("id") int id);

    @POST("/api/card")
    Call<Card> updateCard(@Body Card card);
}
