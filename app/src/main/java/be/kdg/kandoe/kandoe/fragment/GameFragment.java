package be.kdg.kandoe.kandoe.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import be.kdg.kandoe.kandoe.R;
import be.kdg.kandoe.kandoe.activity.MainActivity;
import be.kdg.kandoe.kandoe.activity.ThemeCardActivity;
import be.kdg.kandoe.kandoe.application.KandoeApplication;
import be.kdg.kandoe.kandoe.dom.Card;
import be.kdg.kandoe.kandoe.dom.Circle;
import be.kdg.kandoe.kandoe.dom.Theme;
import be.kdg.kandoe.kandoe.exception.AbstractExceptionCallback;
import be.kdg.kandoe.kandoe.exception.CardException;
import be.kdg.kandoe.kandoe.exception.ThemeException;
import be.kdg.kandoe.kandoe.util.CustomComparator;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;


public class GameFragment extends Fragment {
    private static GameFragment ref;
    private Theme currentTheme;
    private Circle currentCircle;
    private int currentOrganisationId;
    private List<Card> circleCards;
    private List<Button> circleButtons;
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
    private LinearLayout gameStatus;
    private List<Integer> horizontalList;
    private List<Integer> xIsUsed;

    private Runnable backgroundService;
    private Handler handler;
    private Fragment fragment;
    private int interval = 1000;

    private Thread th;


    public GameFragment() {
        fragment = this;
        createBackgroundService();
        try {
            currentTheme = ThemeCardActivity.getCurrentTheme();
            currentCircle = currentTheme.getCircle();
            tableStarts = new int[currentCircle.getTotalRounds()];
        } catch (Exception e) {
            throw new RuntimeException("theme not initilized in gamefragment");
        }
        circleCards = new ArrayList<>();
        circleButtons = new ArrayList<>();
        horizontalList = new ArrayList<>();
        xIsUsed = new ArrayList<>();
    }

    private void createBackgroundService() {
        handler = new Handler();
        backgroundService = new Runnable() {
            @Override
            public void run() {
                requestCards();
                drawCards();
                handler.postDelayed(backgroundService, interval);
            }
        };
//        th = new Thread(backgroundService){
//            @Override
//            public void run() {
//                requestCards();
//                drawCards();
//                handler.post(this);
//                try {
//                    sleep(interval);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
    }

    private void requestCards() {
        Call<Circle> call = KandoeApplication.getCircleApi().getCircle(currentCircle.getCircleId());
        call.enqueue(new AbstractExceptionCallback<Circle>() {
            @Override
            public void onResponse(Response<Circle> response, Retrofit retrofit) {
                circleCards = new ArrayList<>();
                if (response.body() != null && response.body().getCards() != null) {
                    for (Card card : response.body().getCards()) {
                        circleCards.add(card);
                    }
                    Collections.sort(circleCards, new CustomComparator());
                }
            }
        });
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
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

        //TODO: checken if game STARTED is
        gameStatus = (LinearLayout) rootView.findViewById(R.id.game_status);

        ViewTreeObserver viewTreeObserver = gameView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    gameView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    viewWidth = gameView.getWidth();
                    viewHeight = gameView.getHeight();
                    int debug = rootView.getHeight();
                    marginCard = (viewHeight / 9);

                    int xMargin = Math.round((viewWidth / 130));


                    int j = 0;
                    for (int i = currentCircle.getTotalRounds(); i > 0; i--) {
                        tableStarts[i - 1] = marginCard + (j * marginCard) - 165;
                        j++;
                    }

                    for (int i = 25; i < viewWidth - 130; i += 130) {
                        horizontalList.add(i);
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

    @Override
    public void onResume() {
        super.onResume();
        if (fragment.getContext() != null) {
            handler.postDelayed(backgroundService, interval);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        MainActivity.getInstance().getViewPager().setCurrentItem(0);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MainActivity.getInstance().getViewPager().setCurrentItem(1);
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
                new AlertDialog.Builder(this.getContext())
                                    .setTitle("Too many circles :(")
                                    .setMessage("It's best if you continue to play this game on the website, because there are too many circles :(")
                                    .setPositiveButton("no problemo", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
        }
        background.setBackground(drawable);
        return background;
    }


    public void moveCard(Card card) {
        Button myButton = null;
//        Card myCard = null;

        for (Button btn : circleButtons) {
            if (btn.getId() == card.getCardId()) {
                myButton = btn;
            }
        }
        circleButtons.remove(myButton);

//        for (Card c : circleCards) {
//            if (c.getCardId() == card.getCardId()) {
//                myCard = c;
//            }
//        }
        //((ViewGroup) myButton.getParent()).removeView(myButton);
        background.removeView(myButton);

        RelativeLayout.LayoutParams params = null;
        if (myButton != null && card != null) {
            params = (RelativeLayout.LayoutParams) myButton.getLayoutParams();
            params.topMargin = tableStarts[card.getScore()];
            myButton.setOnClickListener(getButtonAndDoAction(myButton));
            //((ViewGroup) myButton.getParent()).addView(myButton,params);
            background.addView(myButton, params);
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
    private void setupCardsOnGrid() {
        //gridTable = (TableLayout) rootView.findViewById(R.id.game_table);
        //TableLayout tableLayout=(TableLayout)rootView.findViewById(R.id.table_layout);
       /* TableRow row1 = (TableRow) gridTable.findViewById(R.id.game_table_row1);
        TableRow row2 = (TableRow) gridTable.findViewById(R.id.game_table_row2);
        TableRow row3 = (TableRow) gridTable.findViewById(R.id.game_table_row3);
        TableRow row4 = (TableRow) gridTable.findViewById(R.id.game_table_row4);
        TableRow row5 = (TableRow) gridTable.findViewById(R.id.game_table_row5);
        TableRow row6 = (TableRow) gridTable.findViewById(R.id.game_table_row6);
        TableRow row7 = (TableRow) gridTable.findViewById(R.id.game_table_row7);
        TableRow row8 = (TableRow) gridTable.findViewById(R.id.game_table_row8);
        TableRow row9 = (TableRow) gridTable.findViewById(R.id.game_table_row9);
        TableRow row10 = (TableRow) gridTable.findViewById(R.id.game_table_row10);

        ImageButton imageButton_r1c1 = (ImageButton) gridTable.findViewById(R.id.img_r1c1);
        ImageButton imageButton_r1c2 = (ImageButton) gridTable.findViewById(R.id.img_r1c2);
        ImageButton imageButton_r1c3 = (ImageButton) gridTable.findViewById(R.id.img_r1c3);
        ImageButton imageButton_r1c4 = (ImageButton) gridTable.findViewById(R.id.img_r1c4);
        ImageButton imageButton_r1c5 = (ImageButton) gridTable.findViewById(R.id.img_r1c5);
        ImageButton imageButton_r1c6 = (ImageButton) gridTable.findViewById(R.id.img_r1c6);
        ImageButton imageButton_r1c7 = (ImageButton) gridTable.findViewById(R.id.img_r1c7);
        ImageButton imageButton_r1c8 = (ImageButton) gridTable.findViewById(R.id.img_r1c8);
        ImageButton imageButton_r1c9 = (ImageButton) gridTable.findViewById(R.id.img_r1c9);
        ImageButton imageButton_r1c10 = (ImageButton) gridTable.findViewById(R.id.img_r1c10);

        ImageButton imageButton_r2c1 = (ImageButton) gridTable.findViewById(R.id.img_r2c1);
        ImageButton imageButton_r2c2 = (ImageButton) gridTable.findViewById(R.id.img_r2c2);
        ImageButton imageButton_r2c3 = (ImageButton) gridTable.findViewById(R.id.img_r2c3);
        ImageButton imageButton_r2c4 = (ImageButton) gridTable.findViewById(R.id.img_r2c4);
        ImageButton imageButton_r2c5 = (ImageButton) gridTable.findViewById(R.id.img_r2c5);
        ImageButton imageButton_r2c6 = (ImageButton) gridTable.findViewById(R.id.img_r2c6);
        ImageButton imageButton_r2c7 = (ImageButton) gridTable.findViewById(R.id.img_r2c7);
        ImageButton imageButton_r2c8 = (ImageButton) gridTable.findViewById(R.id.img_r2c8);
        ImageButton imageButton_r2c9 = (ImageButton) gridTable.findViewById(R.id.img_r2c9);
        ImageButton imageButton_r2c10 = (ImageButton) gridTable.findViewById(R.id.img_r2c10);

        ImageButton imageButton_r3c1 = (ImageButton) gridTable.findViewById(R.id.img_r3c1);
        ImageButton imageButton_r3c2 = (ImageButton) gridTable.findViewById(R.id.img_r3c2);
        ImageButton imageButton_r3c3 = (ImageButton) gridTable.findViewById(R.id.img_r3c3);
        ImageButton imageButton_r3c4 = (ImageButton) gridTable.findViewById(R.id.img_r3c4);
        ImageButton imageButton_r3c5 = (ImageButton) gridTable.findViewById(R.id.img_r3c5);
        ImageButton imageButton_r3c6 = (ImageButton) gridTable.findViewById(R.id.img_r3c6);
        ImageButton imageButton_r3c7 = (ImageButton) gridTable.findViewById(R.id.img_r3c7);
        ImageButton imageButton_r3c8 = (ImageButton) gridTable.findViewById(R.id.img_r3c8);
        ImageButton imageButton_r3c9 = (ImageButton) gridTable.findViewById(R.id.img_r3c9);
        ImageButton imageButton_r3c10 = (ImageButton) gridTable.findViewById(R.id.img_r3c10);

        ImageButton imageButton_r4c1 = (ImageButton) gridTable.findViewById(R.id.img_r4c1);
        ImageButton imageButton_r4c2 = (ImageButton) gridTable.findViewById(R.id.img_r4c2);
        ImageButton imageButton_r4c3 = (ImageButton) gridTable.findViewById(R.id.img_r4c3);
        ImageButton imageButton_r4c4 = (ImageButton) gridTable.findViewById(R.id.img_r4c4);
        ImageButton imageButton_r4c5 = (ImageButton) gridTable.findViewById(R.id.img_r4c5);
        ImageButton imageButton_r4c6 = (ImageButton) gridTable.findViewById(R.id.img_r4c6);
        ImageButton imageButton_r4c7 = (ImageButton) gridTable.findViewById(R.id.img_r4c7);
        ImageButton imageButton_r4c8 = (ImageButton) gridTable.findViewById(R.id.img_r4c8);
        ImageButton imageButton_r4c9 = (ImageButton) gridTable.findViewById(R.id.img_r4c9);
        ImageButton imageButton_r4c10 = (ImageButton) gridTable.findViewById(R.id.img_r4c10);

        ImageButton imageButton_r5c1 = (ImageButton) gridTable.findViewById(R.id.img_r5c1);
        ImageButton imageButton_r5c2 = (ImageButton) gridTable.findViewById(R.id.img_r5c2);
        ImageButton imageButton_r5c3 = (ImageButton) gridTable.findViewById(R.id.img_r5c3);
        ImageButton imageButton_r5c4 = (ImageButton) gridTable.findViewById(R.id.img_r5c4);
        ImageButton imageButton_r5c5 = (ImageButton) gridTable.findViewById(R.id.img_r5c5);
        ImageButton imageButton_r5c6 = (ImageButton) gridTable.findViewById(R.id.img_r5c6);
        ImageButton imageButton_r5c7 = (ImageButton) gridTable.findViewById(R.id.img_r5c7);
        ImageButton imageButton_r5c8 = (ImageButton) gridTable.findViewById(R.id.img_r5c8);
        ImageButton imageButton_r5c9 = (ImageButton) gridTable.findViewById(R.id.img_r5c9);
        ImageButton imageButton_r5c10 = (ImageButton) gridTable.findViewById(R.id.img_r5c10);

        ImageButton imageButton_r6c1 = (ImageButton) gridTable.findViewById(R.id.img_r6c1);
        ImageButton imageButton_r6c2 = (ImageButton) gridTable.findViewById(R.id.img_r6c2);
        ImageButton imageButton_r6c3 = (ImageButton) gridTable.findViewById(R.id.img_r6c3);
        ImageButton imageButton_r6c4 = (ImageButton) gridTable.findViewById(R.id.img_r6c4);
        ImageButton imageButton_r6c5 = (ImageButton) gridTable.findViewById(R.id.img_r6c5);
        ImageButton imageButton_r6c6 = (ImageButton) gridTable.findViewById(R.id.img_r6c6);
        ImageButton imageButton_r6c7 = (ImageButton) gridTable.findViewById(R.id.img_r6c7);
        ImageButton imageButton_r6c8 = (ImageButton) gridTable.findViewById(R.id.img_r6c8);
        ImageButton imageButton_r6c9 = (ImageButton) gridTable.findViewById(R.id.img_r6c9);
        ImageButton imageButton_r6c10 = (ImageButton) gridTable.findViewById(R.id.img_r6c10);

        ImageButton imageButton_r7c1 = (ImageButton) gridTable.findViewById(R.id.img_r7c1);
        ImageButton imageButton_r7c2 = (ImageButton) gridTable.findViewById(R.id.img_r7c2);
        ImageButton imageButton_r7c3 = (ImageButton) gridTable.findViewById(R.id.img_r7c3);
        ImageButton imageButton_r7c4 = (ImageButton) gridTable.findViewById(R.id.img_r7c4);
        ImageButton imageButton_r7c5 = (ImageButton) gridTable.findViewById(R.id.img_r7c5);
        ImageButton imageButton_r7c6 = (ImageButton) gridTable.findViewById(R.id.img_r7c6);
        ImageButton imageButton_r7c7 = (ImageButton) gridTable.findViewById(R.id.img_r7c7);
        ImageButton imageButton_r7c8 = (ImageButton) gridTable.findViewById(R.id.img_r7c8);
        ImageButton imageButton_r7c9 = (ImageButton) gridTable.findViewById(R.id.img_r7c9);
        ImageButton imageButton_r7c10 = (ImageButton) gridTable.findViewById(R.id.img_r7c10);

        ImageButton imageButton_r8c1 = (ImageButton) gridTable.findViewById(R.id.img_r8c1);
        ImageButton imageButton_r8c2 = (ImageButton) gridTable.findViewById(R.id.img_r8c2);
        ImageButton imageButton_r8c3 = (ImageButton) gridTable.findViewById(R.id.img_r8c3);
        ImageButton imageButton_r8c4 = (ImageButton) gridTable.findViewById(R.id.img_r8c4);
        ImageButton imageButton_r8c5 = (ImageButton) gridTable.findViewById(R.id.img_r8c5);
        ImageButton imageButton_r8c6 = (ImageButton) gridTable.findViewById(R.id.img_r8c6);
        ImageButton imageButton_r8c7 = (ImageButton) gridTable.findViewById(R.id.img_r8c7);
        ImageButton imageButton_r8c8 = (ImageButton) gridTable.findViewById(R.id.img_r8c8);
        ImageButton imageButton_r8c9 = (ImageButton) gridTable.findViewById(R.id.img_r8c9);
        ImageButton imageButton_r8c10 = (ImageButton) gridTable.findViewById(R.id.img_r8c10);

        ImageButton imageButton_r9c1 = (ImageButton) gridTable.findViewById(R.id.img_r9c1);
        ImageButton imageButton_r9c2 = (ImageButton) gridTable.findViewById(R.id.img_r9c2);
        ImageButton imageButton_r9c3 = (ImageButton) gridTable.findViewById(R.id.img_r9c3);
        ImageButton imageButton_r9c4 = (ImageButton) gridTable.findViewById(R.id.img_r9c4);
        ImageButton imageButton_r9c5 = (ImageButton) gridTable.findViewById(R.id.img_r9c5);
        ImageButton imageButton_r9c6 = (ImageButton) gridTable.findViewById(R.id.img_r9c6);
        ImageButton imageButton_r9c7 = (ImageButton) gridTable.findViewById(R.id.img_r9c7);
        ImageButton imageButton_r9c8 = (ImageButton) gridTable.findViewById(R.id.img_r9c8);
        ImageButton imageButton_r9c9 = (ImageButton) gridTable.findViewById(R.id.img_r9c9);
        ImageButton imageButton_r9c10 = (ImageButton) gridTable.findViewById(R.id.img_r9c10);

        ImageButton imageButton_r10c1 = (ImageButton) gridTable.findViewById(R.id.img_r10c1);
        ImageButton imageButton_r10c2 = (ImageButton) gridTable.findViewById(R.id.img_r10c2);
        ImageButton imageButton_r10c3 = (ImageButton) gridTable.findViewById(R.id.img_r10c3);
        ImageButton imageButton_r10c4 = (ImageButton) gridTable.findViewById(R.id.img_r10c4);
        ImageButton imageButton_r10c5 = (ImageButton) gridTable.findViewById(R.id.img_r10c5);
        ImageButton imageButton_r10c6 = (ImageButton) gridTable.findViewById(R.id.img_r10c6);
        ImageButton imageButton_r10c7 = (ImageButton) gridTable.findViewById(R.id.img_r10c7);
        ImageButton imageButton_r10c8 = (ImageButton) gridTable.findViewById(R.id.img_r10c8);
        ImageButton imageButton_r10c9 = (ImageButton) gridTable.findViewById(R.id.img_r10c9);
        ImageButton imageButton_r10c10 = (ImageButton) gridTable.findViewById(R.id.img_r10c10);


*/
        TableRow row;
        for (int i = 0; i < currentCircle.getTotalRounds(); i++) {
            row = new TableRow(this.getContext());
        }
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
        background.removeAllViews();

        Collections.sort(circleCards, new CustomComparator());
        int x = 0;
        for (Card card : circleCards) {
            Button myButton = new Button(this.getContext()); //generate ImageButton
            myButton.setId(card.getCardId()); //Set Id of button
            myButton.setBackgroundResource(R.drawable.circle_card_icon);
            myButton.setText(String.valueOf(card.getCardId()));
            myButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(130, 130);

            Random rnd = new Random();

//            int max = horizontalList.size();
//            int range = max-1;
//            int randomNr = rnd.nextInt(range);
//
//            int randomX = horizontalList.get(randomNr);
//
//            if(xIsUsed.contains(randomX)){
//                params.leftMargin = horizontalList.get(randomNr)+65;
//            }else{
//                params.leftMargin = horizontalList.get(randomNr);
//                xIsUsed.add(randomX);
//            }



            //Random rndCol = new Random();
            //int color = Color.argb(255, rndCol.nextInt(256), rndCol.nextInt(256), rndCol.nextInt(256));

//            LayerDrawable layerlist = (LayerDrawable) ContextCompat.getDrawable(this.getContext(),R.drawable.circle_card_icon);
//            GradientDrawable shape = (GradientDrawable) layerlist.findDrawableByLayerId(R.id.icon_circle_background);
//            shape.setColor(color);

//            Drawable backgroundWhereShapeIsPlaced = myImageButton.getBackground();
//            if (backgroundWhereShapeIsPlaced instanceof ShapeDrawable) {
//                ShapeDrawable shapeDrawable = (ShapeDrawable) backgroundWhereShapeIsPlaced;
//                shapeDrawable.getPaint().setColor(color);
//            } else if (backgroundWhereShapeIsPlaced instanceof GradientDrawable) {
//                GradientDrawable gradientDrawable = (GradientDrawable) backgroundWhereShapeIsPlaced;
//                gradientDrawable.setColor(color);
//            } else if (backgroundWhereShapeIsPlaced instanceof ColorDrawable) {
//                ColorDrawable colorDrawable = (ColorDrawable) backgroundWhereShapeIsPlaced;
//                colorDrawable.setColor(color);
//            }


            if (card.getScore() == null)
                card.setScore(0);
            if(x>= horizontalList.size()){
                x=0;
                params.leftMargin =  horizontalList.get(x) + 65;
            }else{
                params.leftMargin = horizontalList.get(x);
                x++;
            }

            params.topMargin = tableStarts[card.getScore()];
            background.addView(myButton, params); //Add view
            myButton.setOnClickListener(getButtonAndDoAction(myButton));
            circleButtons.add(myButton);
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

    View.OnClickListener getButtonAndDoAction(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                ViewPager pager = MainActivity.getInstance().getViewPager();
                pager.setCurrentItem(2);
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
