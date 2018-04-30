package com.wangzhen.simplechartlib.interfaces.charts;

import android.graphics.RectF;

import com.wangzhen.simplechartlib.data.chartData.ChartData;
import com.wangzhen.simplechartlib.formatter.IValueFormatter;
import com.wangzhen.simplechartlib.utils.MPPointF;

/**
 * Created by wangzhen on 2018/3/16.
 */

public interface ChartInterface {


    float getXChartMin();
    float getXChartMax();
    float getXRange();



    float getYChartMin();
    float getYChartMax();


    int getWidth();
    int getHeight();
    MPPointF getCenterOffsets();
    RectF getContentRect();

    //获取view的中心点
    MPPointF getCenterOfView();


    IValueFormatter getDefaultValueFormatter();


    ChartData getData();

    int getMaxVisibleCount();

    float getMaxHighlightDistance();


}
