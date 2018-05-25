package com.wangzhen.simplechartlib.charts;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.wangzhen.simplechartlib.data.chartData.BarData;
import com.wangzhen.simplechartlib.data.entry.BarEntry;
import com.wangzhen.simplechartlib.highlight.BarHighlighter;
import com.wangzhen.simplechartlib.interfaces.charts.BarDataProvider;
import com.wangzhen.simplechartlib.interfaces.dataSets.IBarDataSet;
import com.wangzhen.simplechartlib.renderer.BarChartRenderer;

/**
 * Created by wangzhen on 2018/4/15.
 */

public class BarChart extends BarLineChartBase<BarData> implements BarDataProvider{

    public BarChart(Context context) {
        super(context);
    }

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new BarChartRenderer(mViewPortHandler,this);
//
        setHighlighter(new BarHighlighter(this));

        getXAxis().setSpaceMin(0.5f);
        getXAxis().setSpaceMax(0.5f);
    }




    @Override
    public float getMaxHighlightDistance() {
        return 0;
    }




    @Override
    public BarData getBarData() {
        return mData;
    }

    /**
     * 这个方法很有趣，根据entry的x，y的值获取当前点击的一条bar的区域
      * @param e
     * @param outputRect
     */
    public void getBarBounds(BarEntry e, RectF outputRect) {

        RectF bounds = outputRect;

        IBarDataSet set = mData.getDataSetForEntry(e);

        if (set == null) {
            bounds.set(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
            return;
        }

        float y = e.getY();
        float x = e.getX();

        float barWidth = mData.getBarWidth();

        float left = x - barWidth / 2f;
        float right = x + barWidth / 2f;
        float top = y >= 0 ? y : 0;
        float bottom = y <= 0 ? y : 0;

        bounds.set(left, top, right, bottom);

        getTransformer().rectValueToPixel(outputRect);
    }

    public void groupBars(float fromX, float groupSpace, float barSpace) {

        if (getBarData() == null) {
            throw new RuntimeException("You need to set data for the chart before grouping bars.");
        } else {
            getBarData().groupBars(fromX, groupSpace, barSpace);
            notifyDataSetChanged();
        }
    }

}
