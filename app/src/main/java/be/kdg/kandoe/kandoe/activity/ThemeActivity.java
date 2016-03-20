package be.kdg.kandoe.kandoe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.mikepenz.materialdrawer.Drawer;

import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.adapter.ThemeAdapter;
import be.kdg.kandoe.kandoe.dom.Organisation;
import be.kdg.kandoe.kandoe.dom.Theme;
import be.kdg.kandoe.kandoe.util.ToolbarBuilder;


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

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.organisation_toolbar);
        toolbar.setTitle("Kandoe");

        Drawer drawer = ToolbarBuilder.makeDefaultDrawer(ThemeActivity.this, toolbar);
        drawer.removeItems(1, 2, 3);

        Bundle extras = getIntent().getExtras();
        organisation = (Organisation) extras.get("organisation");


        List<Theme> themes = organisation.getThemes();
        getThemeAdapter().setThemes(themes);

        if (getThemeAdapter().getCount() == 0)
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