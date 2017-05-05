package com.logan.acthome.more;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.logan.bean.MySeatBean;

public class MySeatView extends View {
    // 设置填充位置
    int fillNum;

    int width;
    int squareside;
    int height5x = 14;

    MySeatBean mySeatBean = new MySeatBean();
    //总横数
    int allrow = 0;
    //第几大行
    int seatRow ;
    //int seatRow = Integer.parseInt(mySeatBean.getData().get(0).getSeatRow());

    public MySeatView(Context context) {
        super(context);
    }

    //上下文，第几大行，第几位
    public MySeatView(Context context,int seatRow,int seatsort) {
        super(context);
        this.seatRow=seatRow;
        this.fillNum=seatsort;
    }

    public MySeatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySeatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        squareside = displayMetrics.widthPixels / 28;
        height5x *= squareside;

        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(2);

        for (float i = squareside, k = 1; i < width; i += squareside * 3, k += 1) {
            for (int j = squareside * 3; j < height5x; j += squareside * 3) {
                canvas.drawRect(i, j, i + squareside * 2, j + squareside * 2, paint);
            }
            if (k % 2 == 0) i += squareside * 1.2;
        }

        Paint paint2 = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint2.setColor(Color.rgb(56, 194, 232));
        paint2.setStrokeWidth(0);

        int row = 0;
        int column = (fillNum / 8) * squareside * 2 + squareside * (fillNum / 8 + 3);
        if (fillNum % 8 == 0) row = fillNum % 8 * squareside + 7 * squareside * 2;
        else if (fillNum % 8 != 0)
            row = fillNum % 8 * squareside + (fillNum % 8 - 1) * squareside * 2;
        canvas.drawRect(row + (seatRow - 1) * squareside * 6 / 5, column, row + squareside
                * 2 + (seatRow - 1) * squareside * 6 / 5, column + squareside * 2, paint2);


        /*for (int j = 30; j <= 940; j += 90) {
            for (int i = 90; i <= 590; i += 100) {
                canvas.drawRect(j, i, j + 60, i + 60, paint);
            }
            if (j % 20 == 0)
                j += 60;
        }
        // 上下 canvas.drawRect(20, 180, 80, 240, paint);

        Paint paint2 = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint2.setColor(Color.rgb(56, 194, 232));
        paint2.setStrokeWidth(0);
        // 算法红色21,row横,column第几竖
        int column = fillNum / 6;
        int row = (fillNum + 7) % 6 - 1;
        int column_sing = 0;
        if (column >= 2) {
            column_sing += 60;
            if (column >= 4) {
                column_sing += 60;
                if (column >= 6) {
                    column_sing += 60;
                }
            }
        }
        canvas.drawRect(30 + 90 * row + column_sing, 90 + 100 * column, 90 + 90
                * row + column_sing, 150 + 100 * column, paint2);*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
