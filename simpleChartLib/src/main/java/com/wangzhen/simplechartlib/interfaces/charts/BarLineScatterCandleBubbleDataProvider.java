package com.wangzhen.simplechartlib.interfaces.charts;


import com.wangzhen.simplechartlib.data.chartData.BarLineScatterCandleBubbleData;
import com.wangzhen.simplechartlib.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {
    Transformer getTransformer();
//    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
