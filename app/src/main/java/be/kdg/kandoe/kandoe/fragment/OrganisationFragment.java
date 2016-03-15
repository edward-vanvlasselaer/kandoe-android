package be.kdg.kandoe.kandoe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.adapter.OrganisationAdapter;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Organisation;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by claudiu on 15/03/16.
 */
public class OrganisationFragment extends Fragment {
    private OrganisationAdapter organisationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_organisations, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.organisations_listview);
        organisationAdapter = new OrganisationAdapter(rootView.getContext());
        listView.setAdapter(organisationAdapter);
        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Call<List<Organisation>> call = KandoeApplication.getOrganisationApi().getOrganisations();
        call.enqueue(new AbstractExceptionCallback<List<Organisation>>() {
            @Override
            public void onResponse(Response<List<Organisation>> response, Retrofit retrofit) {
                List<Organisation> organisations = new ArrayList<>();
                for (Organisation organisation : response.body()) {
                    organisations.add(organisation);
                }
                getOrganisationAdapter().setOrganisations(organisations);
            }
        });
    }

    public OrganisationAdapter getOrganisationAdapter() {
        return organisationAdapter;
    }
}
