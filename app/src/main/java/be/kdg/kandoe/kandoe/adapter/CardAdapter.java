package be.kdg.kandoe.kandoe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.activity.ThemeCardActivity;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Circle;
import be.kdg.kandoe.kandoe.dom.Theme;
import be.kdg.kandoe.kandoe.dom.User;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import be.kdg.kandoe.kandoe.fragment.GameFragment;
import be.kdg.kandoe.kandoe.util.AccountSettings;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

import static android.view.LayoutInflater.from;


public class CardAdapter extends BaseAdapter {
    private static CardAdapter instance = null;
    private final Context context;
    private List<Card> cards;
    private Circle circle;

    public CardAdapter(Context context) {
        this.context = context;
        instance = this;
        this.cards = new ArrayList<>();
    }

    public static CardAdapter getInstance() {
        return instance;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }


    public void setCircle(Circle circle) {
        this.circle = circle;
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
            theme = ThemeCardActivity.getCurrentTheme();
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

        //TODO: checken of werkt
        for(User user: circle.getUsers())
        {
            if(user.getUserId()== AccountSettings.getLoggedInUser().getUserId()){
                if(!user.isPlaying()){
                    Toast.makeText(context, "It's not your turn to play", Toast.LENGTH_SHORT).show();

                }
            }
        }
        viewHolder.title.setText(card.getCardName());
        viewHolder.description.setText(card.getDescription());

        final Theme finalTheme = theme;
        viewHolder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameFragment.getSingletonObject().moveCard(card.getCardId());
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
        return convertView;
    }


    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private void checkIfIsTurnOfUser(){
        for(User user: circle.getUsers())
        {
            if(user.getUserId()== AccountSettings.getLoggedInUser().getUserId()){
                if(user.isPlaying()==false){
                    Toast.makeText(context, "It's not your turn to play", Toast.LENGTH_SHORT).show();

                }
            }
        }
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
