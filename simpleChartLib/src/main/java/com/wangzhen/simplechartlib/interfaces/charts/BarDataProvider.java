package com.wangzhen.simplechartlib.interfaces.charts;


import com.wangzhen.simplechartlib.data.chartData.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BarData getBarData();
//    boolean isDrawBarShadowEnabled();
//    boolean isDrawValueAboveBarEnabled();
//    boolean isHighlightFullBarEnabled();
}
