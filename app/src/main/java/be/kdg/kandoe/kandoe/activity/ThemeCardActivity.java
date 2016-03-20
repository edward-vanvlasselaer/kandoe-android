package be.kdg.kandoe.kandoe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.adapter.ThemeCardAdapter;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Theme;
import be.kdg.kandoe.kandoe.dom.User;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by claudiu on 15/03/16.
 */
public class ThemeCardActivity extends AppCompatActivity {
    private ThemeCardAdapter themeCardAdapter;
    private static Theme currentTheme;
    private int themeId;
    private Theme theme;
    private Button startBtn;


    public static void setCurrentTheme(Theme currentTheme) {
        ThemeCardActivity.currentTheme = currentTheme;
    }

    public static Theme getCurrentTheme() throws Exception {
        if (currentTheme == null)
            throw new Exception("currentTheme is NULL");

        return currentTheme;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_card);

        Bundle extras = getIntent().getExtras();
//        theme = (Theme) extras.get("theme");




        themeId = currentTheme.getThemeId();


        final TextView textView = (TextView) findViewById(R.id.txt_nocards);
        startBtn = (Button) findViewById(R.id.btn_select);
        ListView listView = (ListView) findViewById(R.id.cardlist_listview);
        startBtn.setVisibility(View.VISIBLE);

        Call<List<Card>> call = KandoeApplication.getCardApi().getCardsByTheme(themeId);
        call.enqueue(new AbstractExceptionCallback<List<Card>>() {
            @Override
            public void onResponse(Response<List<Card>> response, Retrofit retrofit) {
                List<Card> newList = new ArrayList<>();
                if (response.body() != null) {
                    for (Card card : response.body()) {
                        newList.add(card);
                    }
                    getThemeCardAdapter().setCards(newList);
                } else {
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });

        themeCardAdapter = new ThemeCardAdapter(this.getApplicationContext());
        listView.setAdapter(themeCardAdapter);

        initListener();


    }

    private void initListener() {

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasSelectedMoreThanMin()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    //organisationIntent.putExtra("uri",uri.toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("theme", currentTheme);
                    getApplicationContext().startActivity(intent);
                }
            }
        });

    }

    private boolean hasSelectedMoreThanMin() {
        if (currentTheme.getCircle().getMinCardsToSelect() == null || getThemeCardAdapter().getCardsSelected() >= currentTheme.getCircle().getMinCardsToSelect()) {
            getThemeCardAdapter().setGo(true);
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "You have to select at least " + currentTheme.getCircle().getMinCardsToSelect() + " card", Toast.LENGTH_SHORT).show();
            return false;
        }
    }



    public ThemeCardAdapter getThemeCardAdapter() {
        return themeCardAdapter;
    }


}
