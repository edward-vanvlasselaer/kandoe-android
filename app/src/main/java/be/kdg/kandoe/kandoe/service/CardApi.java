package be.kdg.kandoe.kandoe.service;

import java.util.List;

import be.kdg.kandoe.kandoe.dom.Card;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Edward on 14/03/2016.
 */
public interface CardApi {
    @GET("/api/card/byTheme/{id}")
    void getCardsByTheme(@Path("id") int id, Callback<List<Card>> callback);
}
