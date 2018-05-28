package com.wangzhen.simplechartlib.data.dataSet;



import com.wangzhen.simplechartlib.data.entry.BarEntry;
import com.wangzhen.simplechartlib.interfaces.dataSets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhen on 2018/3/14.
 */

public class BarDataSet extends BarLineScatterCandleBubbleDataSet<BarEntry> implements IBarDataSet {

    /**
     * 一个x对应最大的stack的数量
     */
    private int mStackSize = 1;

    private int mHighLightAlpha = 120;

    /**
     * the overall entry count, including counting each stack-value individually
     */
    private int mEntryCountStacks = 0;

    /**
     * array of labels used to describe the different values of the stacked bars
     */
    private String[] mStackLabels = new String[]{
            "Stack"
    };


    public BarDataSet(List yValues) {
        super(yValues);

        calcStackSize(yValues);
        calcEntryCountIncludingStacks(yValues);

    }

    public BarDataSet(List yValues,String dataSetName) {
        super(yValues);

        this.dataSetName = dataSetName;
        calcStackSize(yValues);
        calcEntryCountIncludingStacks(yValues);

    }



    private void calcStackSize(List<BarEntry> yVals){

        for(int i = 0; i < yVals.size(); i++){
            float[] vals = yVals.get(i).getYVals();

            if(vals != null && vals.length > mStackSize){
                mStackSize = vals.length;
            }
        }

    }


    /**
     * 计算包含stack在内的所有实体的个数
     */
    private void calcEntryCountIncludingStacks(List<BarEntry> yVals) {

        mEntryCountStacks = 0;

        for (int i = 0; i < yVals.size(); i++) {

            float[] vals = yVals.get(i).getYVals();

            if (vals == null)
                mEntryCountStacks++;
            else
                mEntryCountStacks += vals.length;
        }
    }



    @Override
    public boolean isVisible() {
        return mVisible;
    }



    @Override
    public void setValueTextColors(List colors) {

    }

    @Override
    public DataSet copy() {
        List<BarEntry> yVals = new ArrayList<BarEntry>();
        yVals.clear();

        for (int i = 0; i < mValues.size(); i++) {
            yVals.add(mValues.get(i).copy());
        }

        BarDataSet copied = new BarDataSet(yVals);
        copied.mColors = mColors;
        copied.mStackSize = mStackSize;
        copied.mStackLabels = mStackLabels;

        return copied;
    }


    @Override
    protected void calcMinMax(BarEntry e) {

        if (e != null && !Float.isNaN(e.getY())) {

            if (e.getYVals() == null) {

                if (e.getY() < mYMin)
                    mYMin = e.getY();

                if (e.getY() > mYMax)
                    mYMax = e.getY();
            } else {

                if (-e.getNegativeSum() < mYMin)
                    mYMin = -e.getNegativeSum();

                if (e.getPositiveSum() > mYMax)
                    mYMax = e.getPositiveSum();
            }

            calcMinMaxX(e);
        }
    }


    @Override
    public int getStackSize() {
        return mStackSize;
    }

    public void setHighLightAlpha(int alpha) {
        mHighLightAlpha = alpha;
    }


    @Override
    public int getHighLightAlpha() {
        return mHighLightAlpha;
    }

    @Override
    public String[] getStackLabels() {
        return mStackLabels;
    }

    public void setStackLabels(String[] labels) {
        mStackLabels = labels;
    }




    @Override
    public boolean isStacked() {
        return mStackSize > 1 ? true : false;
    }
}
