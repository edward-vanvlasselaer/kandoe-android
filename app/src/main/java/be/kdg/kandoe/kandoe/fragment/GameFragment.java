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
import be.kdg.kandoe.kandoe.exception.ThemeException;


public class GameFragment extends Fragment {
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
    private ViewTreeObserver vto;
    public GameFragment() {
        circleCards = new ArrayList<>();
        circleButtons = new ArrayList<>();
        tableStarts = new int[9];
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
        ref = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game, container, false);

        ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    viewWidth = rootView.getWidth();
                    viewHeight = rootView.getHeight();
                    marginCard = (viewHeight / 9) / 2;
                    for (int i = 0; i < 9; i++) {
                        tableStarts[i] = viewHeight - marginCard - ((marginCard * i) * 2);
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
        Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.rsz_c8);
        switch (currentCircle.getTotalRounds()) {
            case 3:
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c3);
                break;
            case 4:
                //drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c4); TODO c4.png missing..
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
                drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.rsz_c8);
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
            params.topMargin = tableStarts[0]; //TODO 0 -> card.getScore()
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
                int position = cardFragment.getPositionByCardId(button.getId());
                View singleItem = cardFragment.getCardAdapter().getViewByPosition(position,cardList);

                singleItem.setBackgroundColor(ContextCompat.getColor(ref.getContext(),R.color.md_light_blue_100));
                cardList.smoothScrollToPosition(cardFragment.getPositionByCardId(button.getId()));
                Toast.makeText(getActivity(), "button clicked" + button.getId(), Toast.LENGTH_SHORT).show();
            }
        };
    }
}
