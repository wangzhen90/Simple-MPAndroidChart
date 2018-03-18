package com.wangzhen.simplechartlib.interfaces.dataSets;

import com.wangzhen.simplechartlib.data.entry.BarEntry;

/**
 * Created by wangzhen on 2018/3/13.
 */

public interface IBarDataSet extends IBarLineScatterCandleBubbleDataSet<BarEntry> {


    boolean isStacked();

    int getStackSize();

    int getHighLightAlpha();

    String[] getStackLabels();
}
