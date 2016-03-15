package be.kdg.kandoe.kandoe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.view.LayoutInflater.from;

/**
 * Created by Edward on 14/03/2016.
 */
public class CardAdapter extends BaseAdapter {
    private final Context context;
    private List<Card> cards;
    public Card selectedCard;

    private static CardAdapter instance = null;

    public static CardAdapter getInstance() {
        return instance;
    }

    public CardAdapter(Context context) {
        this.context = context;
        instance = this;
        this.cards = new ArrayList<>();
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final Card card = getItem(position);

        final ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = from(context).inflate(R.layout.cardlist_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        //Card tempCard = new Card();
        //tempCard.setCardName(); TODO
        //selectedCard = tempCard;

        viewHolder.title.setText(card.getCardName());
        viewHolder.description.setText(card.getDescription());
        //TODO:weg doen is om score ff te zien

        viewHolder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectedCard.setScore(selectedCard.getScore() == null ? 0 : selectedCard.getScore()+1);
                if (card != null && card.getScore() == null) {
                    card.setScore(0);
                } else {
                    card.setScore(card.getScore() + 1);
                    Call<Card> c= KandoeApplication.getCardApi().updateCard(card);
                    c.enqueue(new Callback<Card>() {
                        @Override
                        public void onResponse(Response<Card> response, Retrofit retrofit) {
                            viewHolder.description.setText(response.body().getScore());
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });


                }
                //selectedCard.setScore(selectedCard.getScore() == null ? 0 : selectedCard.getScore()+1);

            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView title;
        TextView description;
        Button upvote;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.carditem_txt_title);
            description = (TextView) view.findViewById(R.id.carditem_txt_description);
            upvote = (Button) view.findViewById(R.id.carditem_btn_upvote);
        }
    }
}
