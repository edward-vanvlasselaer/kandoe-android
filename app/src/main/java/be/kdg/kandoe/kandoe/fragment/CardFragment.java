package be.kdg.kandoe.kandoe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.activity.ThemeCardActivity;
import be.kdg.kandoe.kandoe.adapter.CardAdapter;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Circle;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import java.util.ArrayList;
import java.util.List;

public class CardFragment extends Fragment {
    private CardAdapter cardAdapter;
    private Callback<List<Card>> callbackList;
    private int circleId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_card, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.cardlist_listview);
        final TextView textView=(TextView) rootView.findViewById(R.id.txt_nocards);
        textView.setVisibility(View.GONE);
        cardAdapter = new CardAdapter(rootView.getContext());
        listView.setAdapter(cardAdapter);


        try {
            circleId=ThemeCardActivity.getCurrentTheme().getCircle().getCircleId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Call<Circle> call = KandoeApplication.getCircleApi().getCircle(circleId);
        call.enqueue(new AbstractExceptionCallback<Circle>() {
            @Override
            public void onResponse(Response<Circle> response, Retrofit retrofit) {
                List<Card> newList = new ArrayList<>();
                if (response.body() != null && response.body().getCards() !=null) {
                    for (Card card : response.body().getCards()) {
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
        try {
            circleId = ThemeCardActivity.getCurrentTheme().getCircle().getCircleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public CardAdapter getCardAdapter() {
        return cardAdapter;
    }


}
