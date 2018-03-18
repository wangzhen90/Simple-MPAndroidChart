package com.wangzhen.simplechartlib.data.chartData;

import com.wangzhen.simplechartlib.interfaces.dataSets.IBarDataSet;

import java.util.List;

/**
 * Created by wangzhen on 2018/3/18.
 */

public class BarData extends BarLineScatterCandleBubbleData<IBarDataSet> {


    /**
     * TODO 什么是in values ?  貌似是百分比
     *
     * the width of the bars on the x-axis, in values (not pixels)
     */
    private float mBarWidth = 0.85f;

    public BarData() {
        super();
    }

    public BarData(IBarDataSet... dataSets) {
        super(dataSets);
    }

    public BarData(List<IBarDataSet> dataSets) {
        super(dataSets);
    }



    /**
     * Sets the width each bar should have on the x-axis (in values, not pixels).
     * Default 0.85f
     *
     * @param mBarWidth
     */
    public void setBarWidth(float mBarWidth) {
        this.mBarWidth = mBarWidth;
    }

    public float getBarWidth() {
        return mBarWidth;
    }

    /**
     *修改所有的entry的 x-value
     * 早先修改的会被覆盖，通过指定的参数给bars之间留出空间
     * 调用完这个方法之后要调用notifyDataSetChanged
     * @param fromX   the starting point on the x-axis where the grouping should begin
     * @param groupSpace   the space between groups of bars in values (not pixels) e.g. 0.8f for bar width 1f  一组中有多个柱子
     * @param barSpace   the space between individual bars in values (not pixels) e.g. 0.1f for bar width 1f   每一组柱子之间的space
     */
    public void groupBars(float fromX, float groupSpace,float barSpace){

        int setCount = mDataSets.size();
        if (setCount <= 1) {
            throw new RuntimeException("BarData needs to hold at least 2 BarDataSets to allow grouping.");
        }
        IBarDataSet max = getMaxEntryCountSet();
        int maxEntryCount = max.getEntryCount();

        float groupSpaceWidthHalf = groupSpace / 2f;
        float barSpaceHalf = barSpace / 2f;
        float barWidthHalf = mBarWidth / 2f;

        float interval = getGroupWidth(groupSpace, barSpace);




    }

    public float getGroupWidth(float groupSpace, float barSpace) {
        return mDataSets.size() * (mBarWidth + barSpace) + groupSpace;
    }

}
