package be.kdg.kandoe.kandoe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.adapter.CardAdapter;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Circle;
import be.kdg.kandoe.kandoe.dom.Theme;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by claudiu on 15/03/16.
 */
public class ThemeCardFragment extends Fragment {
    private CardAdapter cardAdapter;
    private Callback<List<Card>> callbackList;
    private static Theme currentTheme;
    private int themeId;

    public static void setCurrentTheme(Theme currentTheme) {
        ThemeCardFragment.currentTheme = currentTheme;
    }

    public static Theme getCurrentTheme() throws Exception {
        if (currentTheme == null)
            throw new Exception("currentTheme is NULL");

        return currentTheme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_card, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.cardlist_listview);
        final TextView textView = (TextView) rootView.findViewById(R.id.txt_nocards);
        textView.setVisibility(View.GONE);
        cardAdapter = new CardAdapter(rootView.getContext());
        listView.setAdapter(cardAdapter);


        Call<List<Card>> call = KandoeApplication.getCardApi().getCardsByTheme(themeId);
        call.enqueue(new AbstractExceptionCallback<List<Card>>() {
            @Override
            public void onResponse(Response<List<Card>> response, Retrofit retrofit) {
                List<Card> newList = new ArrayList<>();
                if (response.body() != null) {
                    for (Card card : response.body()) {
                        newList.add(card);
                    }
                    getCardAdapter().setCards(newList);
                }else {
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeId = currentTheme.getThemeId();

    }

    public CardAdapter getCardAdapter() {
        return cardAdapter;
    }


}
