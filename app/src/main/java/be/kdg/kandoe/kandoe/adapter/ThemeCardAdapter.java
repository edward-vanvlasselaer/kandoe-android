package be.kdg.kandoe.kandoe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
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
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Theme;
import retrofit.Call;
import retrofit.Callback;
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
            convertView = from(context).inflate(R.layout.card_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        //Card tempCard = new Card();
        //tempCard.setCardName(); TODO
        //selectedCard = tempCard;

        viewHolder.title.setText(card.getCardName());
        viewHolder.description.setText(card.getDescription());
        viewHolder.select.setText(R.string.select_card);
        viewHolder.select.setVisibility(View.GONE);
        viewHolder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!card.isSelected()) {
                    v.setBackgroundColor(ContextCompat.getColor(context, R.color.md_grey_600));
                    card.setIsSelected(true);
                }else {
                    v.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_card_item));
                    card.setIsSelected(false);
                }
            }
        });

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
            cardLayout = (RelativeLayout) view.findViewById(R.id.cardlist_item);

        }
    }
}
