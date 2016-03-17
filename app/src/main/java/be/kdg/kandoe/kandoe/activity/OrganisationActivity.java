package be.kdg.kandoe.kandoe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.net.URISyntaxException;
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

import static android.content.Intent.getIntent;

/**
 * Created by claudiu on 15/03/16.
 */
public class OrganisationActivity extends AppCompatActivity {
    private OrganisationAdapter organisationAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_organisations);

        ListView listView = (ListView) findViewById(R.id.organisations_listview);
        organisationAdapter = new OrganisationAdapter(this.getApplicationContext());
        listView.setAdapter(organisationAdapter);

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
