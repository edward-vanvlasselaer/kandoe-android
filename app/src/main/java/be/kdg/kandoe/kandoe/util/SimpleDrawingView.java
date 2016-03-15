package be.kdg.kandoe.kandoe.util;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import be.kdg.kandoe.kandoe.fragment.ChatFragment;
import be.kdg.kandoe.kandoe.fragment.GameFragment;

/**
 * Created by Edward on 15/03/2016.
 */
public class SimpleDrawingView extends View {
    private final int paintColor = Color.BLACK;
    private Paint drawPaint;

    private int maxWidth;
    private int maxHeight;
    private int[] center;

    public SimpleDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        maxWidth = GameFragment.base.getLayoutParams().width;
        maxHeight = GameFragment.base.getLayoutParams().height;
        center = new int[]{maxWidth / 2, maxHeight / 2};
        setupPaint();
    }
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPaint.setColor(Color.BLACK);
        canvas.drawCircle(center[0], center[1], 200, drawPaint);
        drawPaint.setColor(Color.WHITE);
        canvas.drawCircle(center[0], center[1], 150, drawPaint);
        drawPaint.setColor(Color.WHITE);
        canvas.drawCircle(center[0], center[1], 100, drawPaint);
        drawPaint.setColor(Color.WHITE);
        canvas.drawCircle(center[0], center[1], 50, drawPaint);
    }
}
