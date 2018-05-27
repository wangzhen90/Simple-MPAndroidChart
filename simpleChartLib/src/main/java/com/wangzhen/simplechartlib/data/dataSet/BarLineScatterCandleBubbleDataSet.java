
package com.wangzhen.simplechartlib.data.dataSet;

import android.graphics.Color;


import com.wangzhen.simplechartlib.data.entry.Entry;
import com.wangzhen.simplechartlib.interfaces.dataSets.IBarLineScatterCandleBubbleDataSet;

import java.util.List;

public abstract class BarLineScatterCandleBubbleDataSet<T extends Entry> extends DataSet<T> implements IBarLineScatterCandleBubbleDataSet<T> {

    /** default highlight color */
    protected int mHighLightColor = Color.rgb(0, 0, 0);

    public BarLineScatterCandleBubbleDataSet(List<T> yVals) {
        super(yVals);
    }

    /**
     * Sets the color that is used for drawing the highlight indicators. Dont
     * forget to resolve the color using getResources().getColor(...) or
     * Color.rgb(...).
     * 
     * @param color
     */
    public void setHighLightColor(int color) {
        mHighLightColor = color;
    }

    @Override
    public int getHighLightColor() {
        return mHighLightColor;
    }
}
