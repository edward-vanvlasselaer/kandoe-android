package be.kdg.kandoe.kandoe.service;

import java.util.List;

import be.kdg.kandoe.kandoe.dom.Organisation;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Edward on 14/03/2016.
 */
public interface OrganisationApi {
    @GET("/api/organisation")
    Call<List<Organisation>> getOrganisations();
}
