package be.kdg.kandoe.kandoe.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import be.kdg.kandoe.kandoe.adapter.ThemeAdapter;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Organisation;
import be.kdg.kandoe.kandoe.dom.Theme;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

import static android.content.Intent.getIntent;


/**
 * Created by claudiu on 15/03/16.
 */
public class ThemeActivity extends AppCompatActivity {
    private ThemeAdapter themeAdapter;
    private Organisation organisation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_theme);

        ListView listView = (ListView) findViewById(R.id.themes_listview);
        themeAdapter = new ThemeAdapter(this.getApplicationContext());
        listView.setAdapter(themeAdapter);


        Bundle extras = getIntent().getExtras();
        organisation = (Organisation) extras.get("organisation");


        List<Theme> themes = organisation.getThemes();
        getThemeAdapter().setThemes(themes);

        if(getThemeAdapter().getCount()==0)
            listView.setVisibility(View.GONE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public ThemeAdapter getThemeAdapter() {
        return themeAdapter;
    }
}