package be.kdg.kandoe.kandoe.service;

import java.util.List;

import be.kdg.kandoe.kandoe.dom.Theme;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * This interface is used to call the RESTful API for Organisations
 */
public interface ThemeApi {
    /**
     * @param id identifier of the organisation
     * @return List of theme Objects that belong to the organisation of the given Id
     */
    @GET("/api/theme/organisation/{id}")
    Call<List<Theme>> getThemesByOrganisation(@Path("id") int id);
}
