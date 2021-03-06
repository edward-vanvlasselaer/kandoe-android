package be.kdg.kandoe.kandoe.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.adapter.ThemeCardAdapter;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Theme;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import be.kdg.kandoe.kandoe.util.GenericSharedStorage;
import be.kdg.kandoe.kandoe.util.ToolbarBuilder;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;


public class ThemeCardActivity extends AppCompatActivity {
    private static Theme currentTheme;
    private ThemeCardAdapter themeCardAdapter;
    private int themeId;
    private Theme theme;
    private Button startBtn;
    private TextView noCards;

    private ProgressDialog dialog;
    private Thread mThread;

    public static Theme getCurrentTheme() {
        if (currentTheme != null) return currentTheme;
        currentTheme = new GenericSharedStorage<>(KandoeApplication.app, Theme.class).getObject("currentTheme");
        return currentTheme;
    }

    public static void setCurrentTheme(Theme currentTheme) {
        ThemeCardActivity.currentTheme = currentTheme;
        new GenericSharedStorage<>(KandoeApplication.app, Theme.class).setObject("currentTheme", currentTheme);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_card);//kan geen toolbar opzetten anders haaft cardFragment 2 toolbars, nooit layouts dubbel gebruiken

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading cards..");

        if (dialog != null)
            dialog.show();



        themeId = getCurrentTheme().getThemeId();

        final TextView textView = (TextView) findViewById(R.id.txt_nocards);
        startBtn = (Button) findViewById(R.id.btn_select);
        ListView listView = (ListView) findViewById(R.id.cardlist_listview);
        noCards=(TextView)findViewById(R.id.txt_nocards);
        startBtn.setVisibility(View.VISIBLE);

        if(getCurrentTheme().getCircle()==null){
            startBtn.setVisibility(View.GONE);
            noCards.setVisibility(View.VISIBLE);
        }

        mThread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    requestCards(textView);
                }
            }
        };
        mThread.start();
        themeCardAdapter = new ThemeCardAdapter(this.getApplicationContext());
        listView.setAdapter(themeCardAdapter);
        initListener();
    }

    private void requestCards(final TextView textView) {
        Call<List<Card>> call = KandoeApplication.getCardApi().getCardsByTheme(themeId);
        call.enqueue(new AbstractExceptionCallback<List<Card>>() {
            @Override
            public void onResponse(Response<List<Card>> response, Retrofit retrofit) {
                if (dialog != null)
                    dialog.dismiss();
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

            @Override
            public void onFailure(Throwable t) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }

    private void initListener() {
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasSelectedMoreThanMin()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("theme", getCurrentTheme());
                    getApplicationContext().startActivity(intent);
                }
            }
        });
    }

    private boolean hasSelectedMoreThanMin() {
        if (getCurrentTheme().getCircle().getMinCardsToSelect() == null || getThemeCardAdapter().selectedCardsByUser() >= getCurrentTheme().getCircle().getMinCardsToSelect()) {
            getThemeCardAdapter().setGo(true);
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "You have to select at least " + getCurrentTheme().getCircle().getMinCardsToSelect() + " card", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public ThemeCardAdapter getThemeCardAdapter() {
        return themeCardAdapter;
    }
}
