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
import be.kdg.kandoe.kandoe.dom.Card;

import static android.view.LayoutInflater.from;

/**
 * Created by Edward on 14/03/2016.
 */
public class CardAdapter extends BaseAdapter {
    private final Context context;
    private List<Card> cards;
    public Card selectedCard;

    private static CardAdapter instance=null;
    public static CardAdapter getInstance(){return instance;}

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

        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = from(context).inflate(R.layout.cardlist_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            System.out.println("convertView = from(context).inflate(R.layout.asm_question_item, parent, false);\n");
        }

        //Card tempCard = new Card();
        //tempCard.setCardName(); TODO
        //selectedCard = tempCard;

        viewHolder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               selectedCard.setScore(selectedCard.getScore()+1);
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
