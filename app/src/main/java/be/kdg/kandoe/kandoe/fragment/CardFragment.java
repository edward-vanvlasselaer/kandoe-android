package be.kdg.kandoe.kandoe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import be.kdg.kandoe.kandoe.exception.CardException;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import java.util.ArrayList;
import java.util.List;

public class CardFragment extends Fragment {
    private static CardFragment instance;
    private CardAdapter cardAdapter;
    private Callback<List<Card>> callbackList;
    private int circleId = 0;
    private ListView listView;
    private List<Card> newList;
    private List<View> cardViews;

    public static synchronized CardFragment getInstance() {
        if (instance == null)
            throw new RuntimeException("CardFragment doesn't exist for some reason");
        return instance;
    }

    public ListView getListView() {
        return listView;
    }

    public int getPositionByCardId(int cardId) throws CardException {
        if (newList == null)
            throw new CardException("list with cards hasn't been initialized yet");
        for (Card card : newList) {
            if (cardId == card.getCardId())
                return newList.indexOf(card);
        }
        throw new RuntimeException("card not found in list");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_card, container, false);
        listView = (ListView) rootView.findViewById(R.id.cardlist_listview);
        final TextView textView = (TextView) rootView.findViewById(R.id.txt_nocards);
        textView.setVisibility(View.GONE);
        cardAdapter = new CardAdapter(rootView.getContext());
        listView.setAdapter(cardAdapter);


        try {
            circleId = ThemeCardActivity.getCurrentTheme().getCircle().getCircleId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Call<Circle> call = KandoeApplication.getCircleApi().getCircle(circleId);
        call.enqueue(new AbstractExceptionCallback<Circle>() {
            @Override
            public void onResponse(Response<Circle> response, Retrofit retrofit) {
                newList = new ArrayList<>();
                if (response.body() != null && response.body().getCards() != null) {
                    for (Card card : response.body().getCards()) {
                        newList.add(card);
                    }
                    getCardAdapter().setCards(newList);
                } else {
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });

        return rootView;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        if (!menuVisible && newList != null) {
            for (Card card : newList) {
                int position = newList.indexOf(card);
                View singleItem = getCardAdapter().getViewByPosition(position, listView);
                singleItem.setBackground(ContextCompat.getDrawable(this.getContext(), R.drawable.custom_card_item));
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
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
