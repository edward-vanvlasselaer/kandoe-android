package be.kdg.kandoe.kandoe.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

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
    private TableLayout gridTable;
    private ViewGroup rootView;
    private List<TableRow> rowlist;
    private View gameView;
    private ImageButton buttonMatrix[][];
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
        gameView = rootView.findViewById(R.id.game_background);
        ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    gameView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    viewWidth = gameView.getWidth();
                    viewHeight = gameView.getHeight();
                    int debug = rootView.getHeight();
                    marginCard = (viewHeight / 9) / 2;

                    int j = 0;
                    for (int i = currentCircle.getTotalRounds(); i > 0; i--) {
                        tableStarts[i - 1] = marginCard + (j * marginCard);
                        j++;
                    }
                    //setupGrid2();
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

    @Deprecated
    public void addCardToCircleSet(Card card) {
        circleCards.add(card);
        drawCards();
    }

    @Deprecated
    public void removeCardFromCircleSet(Card card) {
        circleCards.remove(card);
        drawCards();
    }

    private RelativeLayout setBackground(View view) {
        background = (RelativeLayout) view.findViewById(R.id.game_background);
        Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.c8);
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


    public void moveCard(int cardId) {
        ImageButton myButton = null;
        Card myCard = null;

        for (ImageButton btn : circleButtons) {
            if (btn.getId() == cardId) {
                myButton = btn;
            }
        }
        circleButtons.remove(myButton);

        for (Card c : circleCards) {
            if (c.getCardId() == cardId) {
                myCard = c;
            }
        }
        background.removeView(myButton);

        RelativeLayout.LayoutParams params = null;
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

    @Deprecated
    private void setupGrid() {
//        //TableLayout table = (TableLayout) rootView.findViewById(R.id.game_table);
////         TableRow row1 = (TableRow) table.findViewById(R.id.game_table_row1);
////         TableRow row2 = (TableRow) table.findViewById(R.id.game_table_row2);
////         TableRow row3 = (TableRow) table.findViewById(R.id.game_table_row3);
////         TableRow row4 = (TableRow) table.findViewById(R.id.game_table_row4);
////         TableRow row5 = (TableRow) table.findViewById(R.id.game_table_row5);
////         TableRow row6 = (TableRow) table.findViewById(R.id.game_table_row6);
////         TableRow row7 = (TableRow) table.findViewById(R.id.game_table_row7);
////         TableRow row8 = (TableRow) table.findViewById(R.id.game_table_row8);
////         TableRow row9 = (TableRow) table.findViewById(R.id.game_table_row9);
//
//        rowlist = new ArrayList<TableRow>();
////        rowlist.add(row1);
////        rowlist.add(row2);
////        rowlist.add(row3);
////        rowlist.add(row4);
////        rowlist.add(row5);
////        rowlist.add(row6);
////        rowlist.add(row7);
////        rowlist.add(row8);
////        rowlist.add(row9);
//
//        int rows = currentCircle.getTotalRounds();
//        int columns = 20;
//        buttonMatrix = new ImageButton[rows][columns];
//
//        int index = 0;
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < columns; j++) {
//                Button btn = new Button(this.getContext());
//                btn.setBackgroundResource(R.drawable.test_icon);
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(20, viewHeight / 9);
//                //debug only
//                Random rndCol = new Random();
//                int color = Color.argb(255, rndCol.nextInt(256), rndCol.nextInt(256), rndCol.nextInt(256));
//                btn.setBackgroundColor(color);
//
//                btn.setId(newid(index));
//                buttonMatrix[i][j] = new ImageButton(this.getContext());
//
//                TableRow row = rowlist.get(i);
//                row.addView(btn, params);
//            }
//            TableRow row = rowlist.get(i);
//            table.addView(row);
//        }
//        background.addView(table);
//        rootView.addView(background);
    }

    @Deprecated
    private void setupGrid2() {
//        gridTable = (TableLayout) rootView.findViewById(R.id.game_table);
//        TableRow row1 = (TableRow) gridTable.findViewById(R.id.game_table_row1);
//        TableRow row2 = (TableRow) gridTable.findViewById(R.id.game_table_row2);
//
//
//        List<TableRow> rowList = new ArrayList<>();
//        rowList.add(row1);
//        rowList.add(row2);
//
//        ImageButton btn1 = (ImageButton) gameView.findViewById(R.id.img_r1c1);
//        ImageButton btn2 = (ImageButton) gameView.findViewById(R.id.img_r1c2);
//        ImageButton btn3 = (ImageButton) gameView.findViewById(R.id.img_r1c3);
//        ImageButton btn4 = (ImageButton) gameView.findViewById(R.id.img_r2c1);
//        ImageButton btn5 = (ImageButton) gameView.findViewById(R.id.img_r2c2);
//        ImageButton btn6 = (ImageButton) gameView.findViewById(R.id.img_r2c3);

        List<ImageButton> buttonsOfRow1 = new ArrayList<ImageButton>();
        List<ImageButton> buttonsOfRow2 = new ArrayList<ImageButton>();
//        buttonsOfRow1.add(btn1);
//        buttonsOfRow1.add(btn2);
//        buttonsOfRow1.add(btn3);
//
//        buttonsOfRow2.add(btn4);
//        buttonsOfRow2.add(btn5);
//        buttonsOfRow2.add(btn6);


//        for (ImageButton btn : list){
//            row.removeView(btn);
//            btn1 = new ImageButton(this.getContext());
//            //btn.setBackgroundResource(R.drawable.test_icon);
//            TableRow.LayoutParams params = new TableRow.LayoutParams(150, 150);
//            btn1.setLayoutParams(params);
//            row.addView(btn, params);
//        }
//
//        for (int i = 0; i < rowList.size(); i++) {
//            for (int j = 0; j < buttonsOfRow1.size(); j++) {
//                rowList.get(i).removeView(buttonsOfRow1.get(j));
//                TableRow.LayoutParams params = new TableRow.LayoutParams(viewWidth / 10, viewWidth / 9);
//                rowList.get(i).addView(buttonsOfRow1.get(j), params);
//            }
//            for (int j = 0; j < buttonsOfRow2.size(); j++) {
//                rowList.get(i).removeView(buttonsOfRow2.get(j));
//                TableRow.LayoutParams params = new TableRow.LayoutParams(viewWidth / 10, viewWidth / 9);
//                rowList.get(i).addView(buttonsOfRow2.get(j), params);
//            }
//        }
    }

    @Deprecated
    private int newid(int index) {
        return index++;
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
            int range = max - min - 25;
            int rndLeft = rnd.nextInt(range) + min;

            Random rndCol = new Random();
            int color = Color.argb(255, rndCol.nextInt(256), rndCol.nextInt(256), rndCol.nextInt(256));

            Drawable backgroundWhereShapeIsPlaced = myImageButton.getBackground();
            if (backgroundWhereShapeIsPlaced instanceof ShapeDrawable) {
                ShapeDrawable shapeDrawable = (ShapeDrawable) backgroundWhereShapeIsPlaced;
                shapeDrawable.getPaint().setColor(color);
            } else if (backgroundWhereShapeIsPlaced instanceof GradientDrawable) {
                GradientDrawable gradientDrawable = (GradientDrawable) backgroundWhereShapeIsPlaced;
                gradientDrawable.setColor(color);
            } else if (backgroundWhereShapeIsPlaced instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) backgroundWhereShapeIsPlaced;
                colorDrawable.setColor(color);
            }

            params.leftMargin = rndLeft;
            if (card.getScore() == null)
                card.setScore(0);

            params.topMargin = tableStarts[card.getScore()];
            background.addView(myImageButton, params); //Add view
            myImageButton.setOnClickListener(getButtonAndDoAction(myImageButton));
            circleButtons.add(myImageButton);
        }
    }

    @Deprecated
    private void drawCards2() {
       // gridTable = (TableLayout) rootView.findViewById(R.id.game_table);
        gridTable.removeAllViews();


        for (Card card : circleCards) {
            ImageButton myImageButton = new ImageButton(this.getContext());
            myImageButton.setId(card.getCardId());
            myImageButton.setBackgroundResource(R.drawable.circle_card_icon);


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
                    View singleItem = cardFragment.getCardAdapter().getViewByPosition(position, cardList);
                    singleItem.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.custom_themecard_item_selected));
                    cardList.smoothScrollToPosition(cardFragment.getPositionByCardId(button.getId()));
                } catch (CardException e) {
                    e.printStackTrace();
                }
            }
        };
    }


}
