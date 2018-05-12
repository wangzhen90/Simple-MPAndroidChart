package com.wangzhen.simplechartlib.data.chartData;

import com.wangzhen.simplechartlib.data.entry.BarEntry;
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
     * 如果有多个DataSet的话，需要（在Activity或者其他setData之后）调用此方法，调用完这个方法之后要调用notifyDataSetChanged
     * 这个方法会修改所有的entry的 x-value，这个x-value都是每个Bar的中点的x
     *
     * @param fromX 开始的x，比如有10个dataSet，通常就是0
     * @param groupSpace   groups之间的空隙
     * @param barSpace   每一组柱子之间的空隙，通常我们的BarSpace都是0
     */
    public void groupBars(float fromX, float groupSpace,float barSpace){

        int setCount = mDataSets.size();
        if (setCount <= 1) {
            throw new RuntimeException("BarData needs to hold at least 2 BarDataSets to allow grouping.");
        }
        //获得含有Entry最多的DataSet
        IBarDataSet max = getMaxEntryCountSet();
        int maxEntryCount = max.getEntryCount();

        float groupSpaceWidthHalf = groupSpace / 2f;
        float barSpaceHalf = barSpace / 2f;
        float barWidthHalf = mBarWidth / 2f;

        float interval = getGroupWidth(groupSpace, barSpace);

        for (int i = 0; i < maxEntryCount; i++) {

            float start = fromX;
            fromX += groupSpaceWidthHalf;

            for (IBarDataSet set : mDataSets) {

                fromX += barSpaceHalf;
                fromX += barWidthHalf;

                if (i < set.getEntryCount()) {

                    BarEntry entry = set.getEntryForIndex(i);

                    if (entry != null) {
                        entry.setX(fromX);
                    }
                }

                fromX += barWidthHalf;
                fromX += barSpaceHalf;
            }

            fromX += groupSpaceWidthHalf;

            //下面的这些代码是为了防止groupBarWidth不是1的错误情况。
            float end = fromX;
            float innerInterval = end - start;
            float diff = interval - innerInterval;

            // correct rounding errors
            if (diff > 0 || diff < 0) {
                fromX += diff;
            }
        }

        notifyDataChanged();

    }

    /**
     * 获得group的宽度（是value而不是px）
     * @param groupSpace
     * @param barSpace
     * @return
     */
    public float getGroupWidth(float groupSpace, float barSpace) {
        return mDataSets.size() * (mBarWidth + barSpace) + groupSpace;
    }

}
