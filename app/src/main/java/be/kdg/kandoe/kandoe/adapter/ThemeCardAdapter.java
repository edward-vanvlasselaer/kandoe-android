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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import be.kdg.kandoe.kandoe.activity.ThemeCardActivity;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

import static android.view.LayoutInflater.from;

/**
 * Created by claudiu on 15/03/16.
 */
public class ThemeCardAdapter extends BaseAdapter {
    private final Context context;
    private List<Card> cards;
    public Card selectedCard;

    HashMap<Integer, Integer> mhashColorSelected = new HashMap<>();

    HashMap<Integer, Integer> mhashBtnVisibility = new HashMap<>();


    private static ThemeCardAdapter instance = null;

    public static ThemeCardAdapter getInstance() {
        return instance;
    }

    public ThemeCardAdapter(Context context) {
        this.context = context;
        instance = this;
        this.cards = new ArrayList<>();

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


        //Card tempCard = new Card();
        //tempCard.setCardName(); TODO
        //selectedCard = tempCard;

        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/bakery.ttf");
        viewHolder.title.setTypeface(face);

        viewHolder.title.setText(card.getCardName());
        viewHolder.description.setText(card.getDescription());
        viewHolder.select.setText(R.string.select_card);
        //viewHolder.select.setVisibility(View.GONE);
        viewHolder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    card.setCircleId(ThemeCardActivity.getCurrentTheme().getCircle().getCircleId());

                    Call<Object> call = KandoeApplication.getCardApi().addCardToCircle(ThemeCardActivity.getCurrentTheme().getCircle().getCircleId(), card);
                    call.enqueue(new AbstractExceptionCallback() {
                        @Override
                        public void onResponse(Response response, Retrofit retrofit) {
                            //viewHolder.cardLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.md_grey_600));
                            //viewHolder.select.setVisibility(View.INVISIBLE);
                            mhashColorSelected.put(position, R.drawable.custom_themecard_item_selected);
                            mhashBtnVisibility.put(position, View.INVISIBLE);
                            notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //if(card.getC)
            }
        });

        // viewHolder.select.setVisibility(mhashBtnVisibility.get(position));


        viewHolder.cardLayout.setBackground(ContextCompat.getDrawable(context, mhashColorSelected.get(position)));
        if (card.getCircleId() != 0) {
            viewHolder.cardLayout.setBackground(ContextCompat.getDrawable(context, mhashColorSelected.get(position)));
        }

        //noinspection ResourceType
        viewHolder.select.setVisibility(mhashBtnVisibility.get(position));

        return convertView;
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
