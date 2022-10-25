package ru.volodin.mylab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DrawView extends View {
    private final Activity activity;
    Map<Rect, Elem> listElems = new HashMap<>();
    String text = "";
    int countElem;
    int falseElem;

    {
        listElems.put(new Rect(1250, 150, 1450, 335 + 180), new Elem(1350f, 335f, 180));
        listElems.put(new Rect(220, 450, 300 + 50, 525), new Elem(280f, 478f, 50f));
        listElems.put(new Rect(500, 0, 800, 290), new Elem(650f, 150f, 150f));
        listElems.put(new Rect(750, 680, 1100, 1000), new Elem(950f, 850f, 200f));
        listElems.put(new Rect(1450, 200, 1700, 650), new Elem(1650f, 450f, 150f));
    }

    private final Paint p;

    public DrawView(Context context, Activity activity) {
        super(context);
        this.activity = activity;
        p = new Paint();
        Drawable d = getResources().getDrawable(R.drawable.img1);
        setBackground(d);
        countElem = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int w = displayMetrics.widthPixels;
        int h = displayMetrics.heightPixels;
        p.setAlpha(0);
        p.setColor(Color.RED);
        p.setTextSize(100);
        canvas.drawText(text, 10, 100, p);
        p.setStrokeWidth(10);
        p.setAlpha(127);
        for (Elem elem :
                listElems.values()) {
            if (elem.visible)
                canvas.drawCircle(elem.x, elem.y, elem.radius, p);
        }
    }

    private boolean checkWin() {
        if (countElem == 8) {
            System.out.println(listElems.size());
            text = "Победа";
            System.out.println("Победа");
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        //System.out.println(x + " " + y);
        checkWin();
        for (Rect e :
                listElems.keySet()) {
            if (e.contains(x, y)) {
                countElem += 1;
                listElems.get(e).visible = true;
                invalidate();
                return true;
            }
        }
        falseElem += 1;
        if (falseElem % 3 == 0 && falseElem > 0) {
            List<Elem> collect = listElems.values().stream()
                    .filter(e -> !e.visible)
                    .collect(Collectors.toList());
            if (collect.size() > 0) {
                text = collect.get(0).helpText;
                invalidate();
            }
        }
        return false;
    }
}
