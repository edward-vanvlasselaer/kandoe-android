package be.kdg.kandoe.kandoe.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Circle;
import be.kdg.kandoe.kandoe.dom.Theme;
import be.kdg.kandoe.kandoe.exception.ThemeException;


public class GameFragment extends Fragment {
    private Theme currentTheme;
    private Circle currentCircle;
    private int currentOrganisationId;
    private ArrayList<Card> circleCards;
    private ArrayList<ImageButton> circleButtons;
    private int viewHeight;
    private int viewWidth;
    private RelativeLayout background;

    public GameFragment() {
    }

    public static synchronized GameFragment getSingletonObject() {
        if (ref == null) {
            ref = new GameFragment();
        }
        return ref;
    }

    private static GameFragment ref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        circleCards = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game, container, false);
        try {
            currentTheme = ThemeCardFragment.getCurrentTheme();
            currentCircle = currentTheme.getCircle();
        } catch (Exception e) {
            throw new ThemeException("theme not found");
        }
        background = setBackground(rootView);
        drawCards();
        return rootView;
    }

    public void addCardToCircleSet(Card card) {
        circleCards.add(card);
        drawCards();
    }

    public void removeCardFromCircleSet(Card card) {
        circleCards.remove(card);
        drawCards();
    }

    private RelativeLayout setBackground(View view) {
        background = (RelativeLayout) view.findViewById(R.id.game_background);
        Drawable drawable;
        switch (currentCircle.getTotalRounds()) {
            case 3:
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c3);
                ;
                break;
            case 4:
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c4);
                ;
                break;
            case 5:
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c5);
                ;
                break;
            case 6:
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c6);
                ;
                break;
            case 7:
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c7);
                ;
                break;
            case 8:
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c8);
                ;
                break;
            case 9:
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c9);
                ;
                break;
            default:
                throw new NullPointerException("number of rounds not found");
        }
        background.setBackground(drawable);
        return background;
    }

    private void drawCards() {
        for (Card card : circleCards) {
            ImageButton myImageButton = new ImageButton(this.getContext()); //generate ImageButton
            myImageButton.setId(card.getCardId()); //Set Id of button
            myImageButton.setBackgroundResource(R.drawable.circle_card_icon);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.leftMargin = 0;//Generate left margin dynamically TODO
            params.topMargin = 0;//Generate right margin dynamically TODO
            background.addView(myImageButton, params); //Add view
            myImageButton.setOnClickListener(getButtonAndDoAction(myImageButton));
            circleButtons.add(myImageButton);
        }
    }

    View.OnClickListener getButtonAndDoAction(final ImageButton button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "button clicked" + button.getId(), Toast.LENGTH_SHORT).show();
            }
        };
    }
}
