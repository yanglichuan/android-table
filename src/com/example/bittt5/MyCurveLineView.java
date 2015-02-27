
package com.example.bittt5;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import android.R.array;
import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

class MyCurveLineView extends View {

    public Context context;

    public MyCurveLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Paint paint;
    public Line line;
    public Line line_ddd;

    public MyCurveLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC));
        paint.setTextSize(10);

        line = new Line();
        line.tag = "售完";
        line.xDatas.add("12-08");
        line.xDatas.add("12-15");
        line.xDatas.add("12-22");
        line.xDatas.add("12-29");
        line.xDatas.add("01-05");
        line.xDatas.add("01-12");

        line.yDatas.add(20000);
        line.yDatas.add(20500);
        line.yDatas.add(30000);
        line.yDatas.add(30500);
        line.yDatas.add(25000);
        line.yDatas.add(36000);

        line_ddd = new Line();
        line_ddd.tag = "在售";
        line_ddd.color = Color.RED;
        line_ddd.xDatas.add("12-08");
        line_ddd.xDatas.add("12-15");
        line_ddd.xDatas.add("12-22");
        line_ddd.xDatas.add("12-29");
        line_ddd.xDatas.add("01-05");
        line_ddd.xDatas.add("01-12");

        line_ddd.yDatas.add(10000);
        line_ddd.yDatas.add(30000);
        line_ddd.yDatas.add(20000);
        line_ddd.yDatas.add(30500);
        line_ddd.yDatas.add(35000);
        line_ddd.yDatas.add(80000);
    }

    class Line {
        public ArrayList<String> xDatas = new ArrayList<String>();
        public ArrayList<Integer> yDatas = new ArrayList<Integer>();

        public ArrayList<Integer> xCCC = new ArrayList<Integer>();
        public ArrayList<Integer> yCCC = new ArrayList<Integer>();

        public int color = Color.GREEN;
        public String tag = "";
    }

    public MyCurveLineView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) defaultW + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);
            }
        }
        screenW = result;
        return result;
    }

    public int screenH = 0;
    public int screenW = 0;
    public int defaultH = 200;
    public int defaultW = 200;

    /**
     * Determines the height of this view
     * 
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultH + getPaddingTop()
                    + getPaddingBottom();
            Log.i("bbb", "sdfsdf::" + result);
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);
            }
        }

        screenH = result;
        return result;
    }

    public final int leftMargin = 100;
    public final int rightMargin = 100;
    public final int topMargin = 100;
    public final int bottomMargin = 100;

    public int perW = 0;
    public int perH = 0;

    public int getBottomLength() {
        return screenW - leftMargin - rightMargin;
    }

    public int getLeftLength() {
        return screenH - topMargin - bottomMargin;
    }

    public ArrayList<Integer> getYDivider() {
        int max = Collections.max(line.yDatas) > Collections.max(line_ddd.yDatas) ? Collections
                .max(line.yDatas) : Collections.max(line_ddd.yDatas);
        int min = Collections.min(line.yDatas) < Collections.max(line_ddd.yDatas) ? Collections
                .min(line.yDatas) : Collections.max(line_ddd.yDatas);

        Log.i("mimi", "max=" + max + "   min=" + min);

        int dy = (max - min) / 2;
        ArrayList<Integer> ydivers = new ArrayList<Integer>();
        ydivers.add(min - dy);
        ydivers.add(min);
        ydivers.add(min + dy);
        ydivers.add(min + 2 * dy);
        ydivers.add(min + 3 * dy);
        return ydivers;
    }

    public float getPercentY(int data) {
        int max = Collections.max(line.yDatas) > Collections.max(line_ddd.yDatas) ? Collections
                .max(line.yDatas) : Collections.max(line_ddd.yDatas);
        int min = Collections.min(line.yDatas) < Collections.min(line_ddd.yDatas) ? Collections
                .min(line.yDatas) : Collections.min(line_ddd.yDatas);
        int dy = (max - min) / 2;

        int chazhi = (data - (min - dy));
        float ttt = (float) chazhi / (float) (5 * dy);

        return 1 - ttt;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(leftMargin, topMargin);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(5);
        paint.setStrokeCap(Cap.ROUND);

        int leftLength = getLeftLength();
        int bottomLength = getBottomLength();
        perH = leftLength / 5;
        perW = bottomLength / (line.xDatas.size() + 1);

        canvas.drawLine(0, 0, 0, leftLength, paint);
        canvas.drawLine(0, leftLength, bottomLength, leftLength, paint);

        for (int i = 0; i < line.xDatas.size(); i++) {
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(2);
            canvas.drawLine((i + 1) * perW, leftLength, (i + 1) * perW, leftLength + 10, paint);
            paint.setStrokeWidth(0);
            paint.setTextSize(30);
            canvas.drawText(line.xDatas.get(i), (i + 1) * perW - 40, leftLength + 40, paint);
            line.xCCC.add((i + 1) * perW);
            line_ddd.xCCC.add((i + 1) * perW);
        }

        canvas.drawText(unit, -50, -50, paint);

        ArrayList<Integer> divider = getYDivider();
        for (int i = 0; i < 5; i++) {
            paint.setStrokeWidth(2);
            paint.setColor(Color.GREEN);
            canvas.drawLine(0, leftLength - (i) * perH, -10, leftLength - (i) * perH, paint);
            paint.setStrokeWidth(0);
            paint.setTextSize(30);
            canvas.drawText(divider.get(i) + "", -leftMargin + 10, leftLength - (i) * perH, paint);
            paint.setColor(Color.GRAY);
            canvas.drawLine(0, leftLength - (i) * perH, bottomLength, leftLength - (i) * perH,
                    paint);
        }
        drawLine(line, leftLength, Color.GREEN, canvas);
        drawLine(line_ddd, leftLength, Color.RED, canvas);

        drawLineLengen1(line, canvas);
        drawLineLengen2(line_ddd, canvas);

        canvas.restore();
    }

    public void drawLine(Line line, int leftLength, int color, Canvas canvas) {
        for (int i = 0; i < line.xDatas.size(); i++) {
            line.yCCC.add((int) (leftLength * getPercentY(line.yDatas.get(i))));
        }
        paint.setColor(line.color);
        paint.setStrokeWidth(4);
        Path path = new Path();
        for (int i = 0; i < line.xDatas.size(); i++) {
            if (i == 0) {
                path.moveTo(line.xCCC.get(0), line.yCCC.get(0));
            } else {
                path.lineTo(line.xCCC.get(i), line.yCCC.get(i));
            }
        }
        canvas.drawPath(path, paint);
        paint.setStrokeWidth(14);
        // 画点
        for (int i = 0; i < line.xDatas.size(); i++) {
            canvas.drawPoint(line.xCCC.get(i), line.yCCC.get(i), paint);
        }
        paint.setStrokeWidth(4);
    }

    public void drawLineLengen1(Line line, Canvas canvas) {
        int leftLength = getLeftLength();
        int bottomLength = getBottomLength();

        paint.setColor(line.color);
        paint.setStrokeWidth(4);

        paint.setStrokeWidth(0);
        canvas.drawText(line.tag, bottomLength, leftLength, paint);
    }

    public void drawLineLengen2(Line line, Canvas canvas) {
        int leftLength = getLeftLength();
        int bottomLength = getBottomLength();

        paint.setColor(line.color);
        paint.setStrokeWidth(4);

        paint.setStrokeWidth(0);
        canvas.drawText(line.tag, bottomLength, leftLength + bottomMargin / 2, paint);
    }
    public String unit = "元/每平米";

}
