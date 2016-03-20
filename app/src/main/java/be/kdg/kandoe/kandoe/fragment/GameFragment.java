package be.kdg.kandoe.kandoe.fragment;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.activity.MainActivity;
import be.kdg.kandoe.kandoe.activity.ThemeCardActivity;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Circle;
import be.kdg.kandoe.kandoe.dom.Theme;
import be.kdg.kandoe.kandoe.exception.CardException;
import be.kdg.kandoe.kandoe.exception.ThemeException;


public class GameFragment extends Fragment {
    private static GameFragment ref;
    private Theme currentTheme;
    private Circle currentCircle;
    private int currentOrganisationId;
    private List<Card> circleCards;
    private List<ImageButton> circleButtons;
    private int viewHeight;
    private int viewWidth;
    private int marginCard;
    private int[] tableStarts;
    private RelativeLayout background;
    private ViewGroup rootView;
    private ViewTreeObserver vto;

    public GameFragment() {
        try {
            currentTheme = ThemeCardActivity.getCurrentTheme();
            currentCircle = currentTheme.getCircle();
            tableStarts = new int[currentCircle.getTotalRounds()];
        } catch (Exception e) {
           throw new RuntimeException("theme not initilized in gamefragment");
        }
        circleCards = new ArrayList<>();
        circleButtons = new ArrayList<>();

    }

    public static synchronized GameFragment getSingletonObject() {
        if (ref == null) {
            ref = new GameFragment();
        }
        return ref;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game, container, false);

        ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    viewWidth = rootView.getWidth();
                    viewHeight = rootView.getHeight();
                    marginCard = (viewHeight / 9) / 2;

                    int j = 0;
                    for (int i = currentCircle.getTotalRounds(); i > 0 ; i--) {
                        tableStarts[i-1] = marginCard + (j*marginCard);
                        j++;
                    }
                    drawCards();
                }
            });
        }
        try {
            currentTheme = ThemeCardActivity.getCurrentTheme();
            currentCircle = currentTheme.getCircle();
        } catch (Exception e) {
            throw new ThemeException("theme not found");
        }
        background = setBackground(rootView);
        circleCards = currentCircle.getCards();

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
                break;
            case 4:
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c4);
                break;
            case 5:
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c5);
                break;
            case 6:
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c6);
                break;
            case 7:
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c7);
                break;
            case 8:
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c8);
                break;
            case 9:
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c9);
                break;
            default:
                throw new NullPointerException("number of rounds not found");
        }
        background.setBackground(drawable);
        return background;
    }


    public void moveCard(int cardId){
        ImageButton myButton = null;
        Card myCard = null;

        for (ImageButton btn : circleButtons){
            if (btn.getId() == cardId){
                myButton = btn;
            }
        }
        circleButtons.remove(myButton);

        for (Card c : circleCards){
            if (c.getCardId() == cardId){
                myCard = c;
            }
        }
        background.removeView(myButton);

        //assert myButton != null;

        RelativeLayout.LayoutParams params= null;
        if (myButton != null && myCard != null) {
            params = (RelativeLayout.LayoutParams) myButton.getLayoutParams();
            params.topMargin = tableStarts[myCard.getScore()];
            background.addView(myButton, params);
            myButton.setOnClickListener(getButtonAndDoAction(myButton));
            circleButtons.add(myButton);
            return;
        }
        throw new RuntimeException("Cannot move card for some reason.");
    }

    private void drawCards() {
        circleButtons = new ArrayList<>();
        for (Card card : circleCards) {
            ImageButton myImageButton = new ImageButton(this.getContext()); //generate ImageButton
            myImageButton.setId(card.getCardId()); //Set Id of button
            myImageButton.setBackgroundResource(R.drawable.circle_card_icon);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);

            Random rnd = new Random();
            int min = 25;
            int max = viewWidth;
            int range = max - min -25;
            int rndLeft = rnd.nextInt(range) + min;

            Random rndCol = new Random();
            int color = Color.argb(255, rndCol.nextInt(256), rndCol.nextInt(256), rndCol.nextInt(256));

            Drawable backgroundWhereShapeIsPlaced = myImageButton.getBackground();
            if (backgroundWhereShapeIsPlaced instanceof ShapeDrawable) {
                ShapeDrawable shapeDrawable = (ShapeDrawable)backgroundWhereShapeIsPlaced;
                shapeDrawable.getPaint().setColor(color);
            } else if (backgroundWhereShapeIsPlaced instanceof GradientDrawable) {
                GradientDrawable gradientDrawable = (GradientDrawable)backgroundWhereShapeIsPlaced;
                gradientDrawable.setColor(color);
            } else if (backgroundWhereShapeIsPlaced instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable)backgroundWhereShapeIsPlaced;
                colorDrawable.setColor(color);
            }

            params.leftMargin = rndLeft;
            if (card.getScore()==null)
                card.setScore(0);
            params.topMargin = tableStarts[card.getScore()];
            background.addView(myImageButton, params); //Add view
            myImageButton.setOnClickListener(getButtonAndDoAction(myImageButton));
            circleButtons.add(myImageButton);
        }
    }

    View.OnClickListener getButtonAndDoAction(final ImageButton button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                ViewPager pager = MainActivity.getInstance().getViewPager();
                pager.setCurrentItem(3);
                CardFragment cardFragment = CardFragment.getInstance();
                ListView cardList = cardFragment.getListView();
                int position = 0;
                try {
                    position = cardFragment.getPositionByCardId(button.getId());
                    View singleItem = cardFragment.getCardAdapter().getViewByPosition(position,cardList);
                    singleItem.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.custom_themecard_item_selected));
                    cardList.smoothScrollToPosition(cardFragment.getPositionByCardId(button.getId()));
                } catch (CardException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), "button clicked" + button.getId(), Toast.LENGTH_SHORT).show();
            }
        };
    }


}
