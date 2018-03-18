package com.wangzhen.simplechartlib.interfaces.charts;


import com.wangzhen.simplechartlib.data.chartData.BarLineScatterCandleBubbleData;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {
    //TODO transformer 暂时不用
//    Transformer getTransformer(AxisDependency axis);
//    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
