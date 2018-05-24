package com.wangzhen.simplechartlib.highlight;

import com.wangzhen.simplechartlib.data.chartData.BarLineScatterCandleBubbleData;
import com.wangzhen.simplechartlib.data.dataSet.DataSet;
import com.wangzhen.simplechartlib.data.entry.Entry;
import com.wangzhen.simplechartlib.interfaces.charts.BarLineScatterCandleBubbleDataProvider;
import com.wangzhen.simplechartlib.interfaces.dataSets.IDataSet;
import com.wangzhen.simplechartlib.utils.MPPointD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhen on 2018/5/24.
 * 如何查找highlight
 1. 根据此次点击事件的x，y坐标，经过矩阵转换获得对应的 xValue 和 yValue

 2.遍历所有显示highlight的dataSet，使用二分查找法获取该xValue上所有的entry，然后将根据这些entry生成一个highlight的数组highlights

 3.遍历highlights，寻找距离(x,y)最近的Highlight
 *
 */

public class ChartHighlighter<T extends BarLineScatterCandleBubbleDataProvider> implements IHighlighter {

    protected T mChart;

    protected List<Highlight> mHighlightBuffer = new ArrayList<>();

    public ChartHighlighter(T chart) {
        this.mChart = chart;
    }

    @Override
    public Highlight getHighlight(float x, float y) {

        MPPointD pos = getValsForTouch(x, y);

        float xVal = (float) pos.x;
        MPPointD.recycleInstance(pos);

        Highlight high = getHighlightForX(xVal,x,y);

        return high;
    }

    protected MPPointD getValsForTouch(float x, float y) {

        MPPointD pos = mChart.getTransformer().getValuesByTouchPoint(x, y);
        return pos;
    }


    protected Highlight getHighlightForX(float xVal, float x, float y) {
        List<Highlight> closestValues = getHighlightsAtXValue(xVal, x, y);

        if (closestValues.isEmpty()) {
            return null;
        }

        Highlight detail = getClosestHighlightByPixel(closestValues, x, y, mChart.getMaxHighlightDistance());

        return detail;
    }

    protected BarLineScatterCandleBubbleData getData() {
        return mChart.getData();
    }


    protected List<Highlight> getHighlightsAtXValue(float xVal, float x, float y) {
        mHighlightBuffer.clear();

        BarLineScatterCandleBubbleData data = getData();
        if (data == null)
            return mHighlightBuffer;

        for (int i = 0, dataSetCount = data.getDataSetCount(); i < dataSetCount; i++) {

            IDataSet dataSet = data.getDataSetByIndex(i);

            // don't include DataSets that cannot be highlighted
            if (!dataSet.isHighlightEnabled())
                continue;

            mHighlightBuffer.addAll(buildHighlights(dataSet, i, xVal, DataSet.Rounding.CLOSEST));
        }

        return mHighlightBuffer;
    }

    protected List<Highlight> buildHighlights(IDataSet set, int dataSetIndex, float xVal, DataSet.Rounding rounding) {


        ArrayList<Highlight> highlights = new ArrayList<>();

        List<Entry> entries = set.getEntriesForXValue(xVal);

        if (entries.size() == 0) {
            final Entry closest = set.getEntryForXValue(xVal, Float.NaN, rounding);

            if (closest != null) {
                entries = set.getEntriesForXValue(closest.getX());
            }

        }

        if (entries.size() == 0)
            return highlights;

        for (Entry e : entries) {

            MPPointD pixels = mChart.getTransformer().getPixelForValues(e.getX(), e.getY());

            highlights.add(new Highlight(
                    e.getX(), e.getY(),
                    (float) pixels.x, (float) pixels.y,
                    dataSetIndex));
        }

        return highlights;
    }


    public Highlight getClosestHighlightByPixel(List<Highlight> closestValues, float x, float y, float minSelectionDistance) {
        Highlight closest = null;
        float distance = minSelectionDistance;

        for (int i = 0; i < closestValues.size(); i++) {

            Highlight high = closestValues.get(i);

            float cDistance = getDistance(x, y, high.getXPx(), high.getYPx());

            if (cDistance < distance) {
                closest = high;
                distance = cDistance;
            }
        }

        return closest;

    }

    protected float getDistance(float x1, float y1, float x2, float y2) {

        return (float) Math.hypot(x1 - x2, y1 - y2);
    }


}
