package be.kdg.kandoe.kandoe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import be.kdg.kandoe.kandoe.R;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_card, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.cardlist_listview);
        cardAdapter = new CardAdapter(rootView.getContext());
        listView.setAdapter(cardAdapter);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Call<Circle> call= KandoeApplication.getCircleApi().getCircle(1);
        call.enqueue(new AbstractExceptionCallback<Circle>() {
            @Override
            public void onResponse(Response<Circle> response, Retrofit retrofit) {
                List<Card> newList = new ArrayList<>();
                for (Card card : response.body().getCards()) {
                    newList.add(card);
                }
                getCardAdapter().setCards(newList);
            }
        });
        /*call.enqueue(new AbstractExceptionCallback<List<Card>>() {
            @Override
            public void onResponse(Response<List<Card>> response, Retrofit retrofit) {
                List<Card> newList = new ArrayList<>();
                for (Card card : response.body()) {
                    newList.add(card);
                }
                getCardAdapter().setCards(newList);
            }
        });
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
