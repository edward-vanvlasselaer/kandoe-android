package be.kdg.kandoe.kandoe.service;

import java.util.List;

import be.kdg.kandoe.kandoe.dom.Theme;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by claudiu on 15/03/16.
 */
public interface ThemeApi {
    @GET("/api/theme/organisation/{id}")
    Call<List<Theme>> getThemesByOrganisation(@Path("id") int id);
}
