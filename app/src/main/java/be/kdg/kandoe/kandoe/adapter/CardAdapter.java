package be.kdg.kandoe.kandoe.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
import be.kdg.kandoe.kandoe.activity.MainActivity;
import be.kdg.kandoe.kandoe.activity.ThemeCardActivity;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Circle;
import be.kdg.kandoe.kandoe.dom.Theme;
import be.kdg.kandoe.kandoe.dom.User;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import be.kdg.kandoe.kandoe.util.AccountSettings;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

import static android.view.LayoutInflater.from;


public class CardAdapter extends BaseAdapter {
    private static CardAdapter instance = null;
    private final Context context;
    private List<Card> cards;

    public List<Card> getCards() {
        return cards;
    }

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

        if (!circle.getGameStatus().equals("STARTED")) {
            viewHolder.upvote.setVisibility(View.INVISIBLE);
        }


        //TODO: checken of werkt
        for (User user : circle.getUsers()) {
            if (user.getUserId() == AccountSettings.getLoggedInUser().getUserId()) {
                if (!user.isPlaying()) {
                    //Toast.makeText(context, "It's not your turn to play", Toast.LENGTH_SHORT).show();
                }
            }
        }

        String cardPosition = "";

        if (card.getPosition() != null) {
            cardPosition = card.getPosition();
            viewHolder.title.setText(cardPosition + ": " + card.getCardName());
        }
        if (card.getPosition() == null) {
            viewHolder.title.setText(card.getCardName());
        }
        viewHolder.description.setText(card.getDescription());

        final Theme finalTheme = theme;
        viewHolder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User playingUser = new User();
                playingUser.setUsername("NULL");
                for (User user : circle.getUsers()) {
                    if (user.isPlaying())
                        playingUser = user;
                }
                for (User user : circle.getUsers()) {
                    if (user.getUserId() == AccountSettings.getLoggedInUser().getUserId()) {
                        if (!user.isPlaying()) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Not so fast!")
                                    .setMessage("It's not your turn to play, please wait until it is your turn.\nNext move is: " + playingUser.getUsername())
                                    .setPositiveButton("Ok I'll wait", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                            return;
                        }
                    }
                }
                if (card.getScore() + 1 < circle.getTotalRounds()) {

                    Call<Object> call = KandoeApplication.getCircleApi().addVote(1, card);
                    call.enqueue(new AbstractExceptionCallback() {
                        @Override
                        public void onResponse(Response response, Retrofit retrofit) {
//                            card.setScore(card.getScore() + 1);
//                            GameFragment.getSingletonObject().moveCard(card);
                            MainActivity.getInstance().getViewPager().setCurrentItem(1);
                        }


                    });
                } else {
                    Toast.makeText(context, "Card is already in the middle", Toast.LENGTH_SHORT).show();
                }
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

    private void checkIfIsTurnOfUser() {
        for (User user : circle.getUsers()) {
            if (user.getUserId() == AccountSettings.getLoggedInUser().getUserId()) {
                if (user.isPlaying() == false) {
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
