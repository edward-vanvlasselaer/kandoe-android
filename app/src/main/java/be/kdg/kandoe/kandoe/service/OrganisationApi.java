package be.kdg.kandoe.kandoe.service;

import java.util.List;

import be.kdg.kandoe.kandoe.dom.Organisation;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * This interface is used to call the RESTful API for Organisations
 */
public interface OrganisationApi {
    /**
     * @return List of all organisations
     */
    @GET("/api/organisation")
    Call<List<Organisation>> getOrganisations();
}
