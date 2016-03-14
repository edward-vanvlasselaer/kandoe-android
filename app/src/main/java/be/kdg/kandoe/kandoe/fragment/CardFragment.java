package be.kdg.kandoe.kandoe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.adapter.CardAdapter;
import be.kdg.kandoe.kandoe.dom.Card;
import retrofit.Callback;

import java.util.ArrayList;
import java.util.List;

public class CardFragment extends Fragment {
    private ListView listView;
    private CardAdapter cardAdapter;
    private Callback<List<Card>> callbackList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_card, container, false);
        listView = (ListView) rootView.findViewById(R.id.cardlist_listview);
        cardAdapter = new CardAdapter(rootView.getContext());
        listView.setAdapter(cardAdapter);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*callbackList = new Callback<List<Card>>() {
            @Override
            public void success(List<Card> cards, Response response) {
                List<Card> newList = new ArrayList<>();
                for (Card card : cards) {
                    newList.add(card);
                }
                getCardAdapter().setCards(newList);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                ExceptionHelper.logRetrofitError(retrofitError,CardFragment.class.getSimpleName());
            }
        };*/
    }

    public CardAdapter getCardAdapter(){
        return cardAdapter;
    }
}
