package com.logan.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Z2z on 2017/3/20.
 */

public class AlignGridView extends GridView {

    public AlignGridView(Context context) {
        super(context);
    }

    public AlignGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlignGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


    public void setOnCreateContextMenuListener() {
    }
}
