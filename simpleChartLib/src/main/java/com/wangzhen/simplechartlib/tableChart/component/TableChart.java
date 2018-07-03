package com.wangzhen.simplechartlib.tableChart.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by wangzhen on 2018/6/28.
 */

public class TableChart extends ViewGroup {


    public TableChart(Context context) {
        super(context);
    }

    public TableChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TableChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(left, top, right, bottom);
        }
    }



}
