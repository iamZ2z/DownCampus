package com.logan.acthome.more;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

public class MySeatView extends View {
	// 设置填充红色的位置
	int fillNum = 21;

	public MySeatView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MySeatView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MySeatView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		WindowManager windowManager=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
		int width=windowManager.getDefaultDisplay().getWidth();
		int height=windowManager.getDefaultDisplay().getHeight();

		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.GRAY);
		paint.setStrokeWidth(2);

		// for (int i = 20;;i+)
		// canvas.drawRect(i, i + 60, i + 60, i + 120, paint);
		for (int j = 30; j <= 940; j += 90) {
			for (int i = 90; i <= 590; i += 100) {
				canvas.drawRect(j, i, j + 60, i + 60, paint);
			}
			if (j % 20 == 0)
				j += 60;
		}
		// 上下 canvas.drawRect(20, 180, 80, 240, paint);

		Paint paint2 = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint2.setColor(Color.rgb(56,194,232));
		paint2.setStrokeWidth(0);
		canvas.drawRect(120, 390, 120 + 60, 390 + 60, paint2);
		// 算法红色21,row排,column第几竖
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
				* row + column_sing, 150 + 100 * column, paint2);
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
