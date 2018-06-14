package com.wangzhen.simplechartlib.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.wangzhen.simplechartlib.charts.Chart;
import com.wangzhen.simplechartlib.highlight.Highlight;

/**
 * Created by wangzhen on 2018/4/17.
 */

public abstract class ChartTouchListener<T extends Chart<?>> extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {

    public enum ChartGesture {
        NONE, DRAG, X_ZOOM, Y_ZOOM, PINCH_ZOOM, ROTATE, SINGLE_TAP, DOUBLE_TAP, LONG_PRESS, FLING
    }

    /**
     * 上次的gesture类型
     */
    protected ChartGesture mLastGesture = ChartGesture.NONE;

    //touch states
    protected static final int NONE = 0; //这个主要是单击，双击事件
    protected static final int DRAG = 1;//拖拽事件，包含在move事件中
    protected static final int X_ZOOM = 2;//x轴方向的缩放，包含在move事件中
    protected static final int Y_ZOOM = 3;//y轴方向的缩放，包含在move事件中
    protected static final int PINCH_ZOOM = 4;//包含x，y轴之间的缩放
    protected static final int POST_ZOOM = 5;
    protected static final int ROTATE = 6;

    //当前的touch state
    protected int mTouchMode = NONE;

    //TODO 暂时不用
//    protected Highlight mLastHighlighted;

    protected GestureDetector mGestureDetector;

    protected T mChart;

    protected Highlight mLastHighlighted;


    public ChartTouchListener(T chart) {
        this.mChart = chart;

        mGestureDetector = new GestureDetector(chart.getContext(), this);


    }


    public void setLastHighlighted(Highlight high) {
        mLastHighlighted = high;
    }

    public void startAction(MotionEvent me) {

        OnChartGestureListener l = mChart.getOnChartGestureListener();

        if (l != null)
            l.onChartGestureStart(me, mLastGesture);
    }

    public void endAction(MotionEvent me) {

        OnChartGestureListener l = mChart.getOnChartGestureListener();

        if (l != null)
            l.onChartGestureEnd(me, mLastGesture);
    }


    protected static float distance(float eventX, float startX, float eventY, float startY) {
        float dx = eventX - startX;
        float dy = eventY - startY;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    protected void performHighlight(Highlight h,MotionEvent e){

        if (h == null || h.equalTo(mLastHighlighted)) {
            mChart.highlightValue(null, true);
            mLastHighlighted = null;

        } else {
            mChart.highlightValue(h, true);
            mLastHighlighted = h;
        }

    }

}
