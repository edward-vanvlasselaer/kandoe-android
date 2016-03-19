package be.kdg.kandoe.kandoe.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.activity.ThemeCardActivity;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Theme;
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
    //public Card selectedCard;

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
        Theme theme = null;
        try {
             theme= ThemeCardActivity.getCurrentTheme();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = from(context).inflate(R.layout.card_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }


        viewHolder.title.setText(card.getCardName());
        viewHolder.description.setText(card.getDescription());
        //TODO:weg doen is om score ff te zien

        final Theme finalTheme = theme;
        viewHolder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Object> call = KandoeApplication.getCircleApi().addVote(1, card);
                call.enqueue(new AbstractExceptionCallback() {
                    @Override
                    public void onResponse(Response response, Retrofit retrofit) {
                        //viewHolder.cardLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.md_grey_600));
                        if (card.getScore() == null) {
                          //  viewHolder.title.setText(String.valueOf(1));
                        } else {
                           // viewHolder.title.setText(card.getScore() + 1);
                        }
                    }


                });
            }
        });

        /*viewHolder.description.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setBackgroundColor(ContextCompat.getColor(context, R.color.md_grey_600));

                return false;
            }
        });
        viewHolder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.cardLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.md_grey_600));

            }
        });*/
        return convertView;
    }

    private class ViewHolder {
        TextView title;
        TextView description;
        Button upvote;
        RelativeLayout cardLayout;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.carditem_txt_title);
            description = (TextView) view.findViewById(R.id.carditem_txt_description);
            upvote = (Button) view.findViewById(R.id.carditem_btn_upvote);
            cardLayout = (RelativeLayout) view.findViewById(R.id.cardlist_item);


        }
    }


}
