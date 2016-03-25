package be.kdg.kandoe.kandoe.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.activity.ThemeCardActivity;
import be.kdg.kandoe.kandoe.adapter.CardAdapter;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Circle;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import be.kdg.kandoe.kandoe.exception.CardException;
import be.kdg.kandoe.kandoe.util.CustomComparator;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CardFragment extends Fragment {
    private static CardFragment instance;
    private CardAdapter cardAdapter;
    private Callback<List<Card>> callbackList;
    private int circleId = 0;
    private ListView listView;
    private List<Card> newList;
    private List<View> cardViews;
    private TextView textView;

    private Runnable backgroundService;
    private Handler handler;
    private Fragment fragment;
    private int interval = 5000;

    public static synchronized CardFragment getInstance() {
        if (instance == null)
            throw new RuntimeException("CardFragment doesn't exist for some reason");
        return instance;
    }

    public ListView getListView() {
        return listView;
    }

    public CardFragment() {
        fragment = this;
        createBackgroundService();
    }

    private void createBackgroundService() {
        handler = new Handler();
        backgroundService = new Runnable() {
            @Override
            public void run() {
                requestCards(textView);
                handler.postDelayed(backgroundService, interval);
            }
        };
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
        textView = (TextView) rootView.findViewById(R.id.txt_nocards);
        textView.setVisibility(View.GONE);
        cardAdapter = new CardAdapter(rootView.getContext());
        listView.setAdapter(cardAdapter);

        final Circle  circle=null;

        try {
            circleId = ThemeCardActivity.getCurrentTheme().getCircle().getCircleId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestCards(textView);
        return rootView;
    }

    private void requestCards(final TextView textView) {
        Call<Circle> call = KandoeApplication.getCircleApi().getCircle(circleId);
        call.enqueue(new AbstractExceptionCallback<Circle>() {
            @Override
            public void onResponse(Response<Circle> response, Retrofit retrofit) {
                getCardAdapter().setCircle(response.body());
                newList = new ArrayList<>();
                if (response.body() != null && response.body().getCards() != null) {
                    for (Card card : response.body().getCards()) {
                        newList.add(card);
                    }
                    Collections.sort(newList,new CustomComparator());
                    if(newList.equals(getCardAdapter().getCards())){
                        return;
                    }else {
                        getCardAdapter().setCards(newList);
                    }
                } else {
                    textView.setVisibility(View.VISIBLE);
                }

                if(response.body().getGameStatus().equals("FINISHED")){
                    new AlertDialog.Builder(getContext())
                            .setTitle("Game is finished")
                            .setMessage("You cannot longer play in this game because the game is finished")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }

                if(response.body().getGameStatus().equals("PLANNED")){
                    new AlertDialog.Builder(getContext())
                            .setTitle("Game is planned")
                            .setMessage("You cannot play in this game because the game is not started yet")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });
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
        if (fragment.getContext() != null) {
            if (menuVisible) {
                handler.postDelayed(backgroundService, interval);
            } else {
                handler.removeCallbacks(backgroundService);
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
