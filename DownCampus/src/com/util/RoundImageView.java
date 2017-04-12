package com.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageView extends ImageView {
	private float[] rids = { 10.0f, 10.0f, 10.0f, 10.0f, 0.0f, 0.0f, 0.0f,
			0.0f, };

	public RoundImageView(Context context) {
		super(context);
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/** * 画图 * by Hankkin at:2015-08-30 21:15:53 * @param canvas */
	protected void onDraw(Canvas canvas) {
		Path path = new Path();
		int w = this.getWidth();
		int h = this.getHeight();
		/* 向路径中添加圆角矩形。radii数组定义圆角矩形的四个圆角的x,y半径。radii长度必须为8 */
		path.addRoundRect(new RectF(0, 0, w, h), rids, Path.Direction.CW);
		canvas.clipPath(path);
		super.onDraw(canvas);
	}
}