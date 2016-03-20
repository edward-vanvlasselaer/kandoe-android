package be.kdg.kandoe.kandoe.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Theme;
import be.kdg.kandoe.kandoe.dom.User;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import be.kdg.kandoe.kandoe.activity.ThemeCardActivity;
import be.kdg.kandoe.kandoe.util.AccountSettings;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.view.LayoutInflater.from;

/**
 * Created by claudiu on 15/03/16.
 */
public class ThemeCardAdapter extends BaseAdapter {
    private static ThemeCardAdapter instance = null;
    private final Context context;
    public Card selectedCard;
    HashMap<Integer, Integer> mhashColorSelected = new HashMap<>();
    HashMap<Integer, Integer> mhashBtnVisibility = new HashMap<>();
    private List<Card> cards;
    private int cardsSelected = 0;
    private boolean go = false;

    public ThemeCardAdapter(Context context) {
        this.context = context;
        instance = this;
        this.cards = new ArrayList<>();

    }

    public static ThemeCardAdapter getInstance() {
        return instance;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
        for (int i = 0; i < cards.size(); i++) {
            mhashColorSelected.put(i, R.drawable.custom_themecard_item);
            mhashBtnVisibility.put(i, View.VISIBLE);
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Card getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Card card = getItem(position);

        final ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = from(context).inflate(R.layout.card_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        selectedCardsByUser();

        //Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/bakery.ttf");
       // viewHolder.title.setTypeface(face);

        viewHolder.title.setText(card.getCardName());
        viewHolder.description.setText(card.getDescription());
        viewHolder.select.setText(R.string.select_card);

        setLayoutOfAlreadySelectedCards(card, position);


        viewHolder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    card.setCircleId(ThemeCardActivity.getCurrentTheme().getCircle().getCircleId());

                    Call<Object> call = KandoeApplication.getCardApi().addCardToCircle(ThemeCardActivity.getCurrentTheme().getCircle().getCircleId(), card);
                    //noinspection unchecked
                    call.enqueue(new AbstractExceptionCallback() {
                        @Override
                        public void onResponse(Response response, Retrofit retrofit) {
                            //TODO: is dat wel mooi?
                            /*if (response.errorBody() != null) {
                                Toast.makeText(context, "You cannot select more cards than max", Toast.LENGTH_SHORT).show();
                                setGo(true);
                            } else {
                                setLayoutOfCardsOfUser(position);
                                incrementSelectedCards();
                            }*/
                            notifyDataSetChanged();
                        }

                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        });


        viewHolder.cardLayout.setBackground(ContextCompat.getDrawable(context, mhashColorSelected.get(position)));
        //noinspection ResourceType
        viewHolder.select.setVisibility(mhashBtnVisibility.get(position));

        return convertView;
    }


    private void incrementSelectedCards(){
        setCardsSelected(getCardsSelected() + 1);
    }

    private void setLayoutOfAlreadySelectedCards(Card card, int position)
    {
        if (card.getSelector() != 0 && card.getSelector()!=AccountSettings.getLoggedInUser().getUserId()) {
            mhashColorSelected.put(position, R.drawable.custom_themecard_already_selected);
            mhashBtnVisibility.put(position, View.INVISIBLE);
        }else if(card.getSelector()==AccountSettings.getLoggedInUser().getUserId()){
            setLayoutOfCardsOfUser(position);
        }
    }

    private void setLayoutOfCardsOfUser(int position){
        mhashColorSelected.put(position, R.drawable.custom_themecard_item_selected);
        mhashBtnVisibility.put(position, View.INVISIBLE);
    }

    private boolean hasSelectedLessThanMax() {
        Theme theme = null;
        try {
            theme = ThemeCardActivity.getCurrentTheme();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cardsSelected < theme.getCircle().getMaxCardsToSelect() || theme.getCircle().getMaxCardsToSelect() == null) {
            return true;
        } else {
            Toast.makeText(context, "You cannot select more than " + theme.getCircle().getMaxCardsToSelect() + " cards", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private int selectedCardsByUser() {
        for (Card card : cards) {
            if (card.getSelector() == AccountSettings.getLoggedInUser().getUserId()) {
                incrementSelectedCards();
            }
        }
        return cardsSelected;
    }

    public int getCardsSelected() {
        return cardsSelected;
    }

    public void setCardsSelected(int cardsSelected) {
        this.cardsSelected = cardsSelected;
    }

    public boolean isGo() {
        return go;
    }

    public void setGo(boolean go) {
        this.go = go;
    }

    private class ViewHolder {
        TextView title;
        TextView description;
        Button select;
        RelativeLayout cardLayout;
        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.carditem_txt_title);
            description = (TextView) view.findViewById(R.id.carditem_txt_description);
            select = (Button) view.findViewById(R.id.carditem_btn_upvote);
            cardLayout = (RelativeLayout) view.findViewById(R.id.card_layout);
        }
    }
}
