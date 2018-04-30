package com.wangzhen.simplechartlib.buffer;

import com.wangzhen.simplechartlib.data.entry.BarEntry;
import com.wangzhen.simplechartlib.interfaces.dataSets.IDataSet;

/**
 * Created by wangzhen on 2018/4/30.
 */

public class BarBuffer extends AbstractBuffer<IDataSet> {

    protected int mDataSetIndex = 0;
    protected int mDataSetCount = 0;

    protected boolean mContainsStacks = false;
    protected boolean mInverted = false;

    /**
     * bar 在 x轴上的宽度，是values而不是pixels
     */
    protected float mBarWidth = 1f;

    public BarBuffer(int size, int dataSetCount, boolean containsStacks) {
        super(size);
        this.mDataSetCount = dataSetCount;
        this.mContainsStacks = containsStacks;

    }

    public void setBarWidth(float barWidth) {
        this.mBarWidth = barWidth;
    }

    public void setDataSet(int index) {
        this.mDataSetIndex = index;
    }

    public void setInverted(boolean inverted) {
        this.mInverted = inverted;
    }


    protected void addBar(float left, float top, float right, float bottom) {

        buffer[index++] = left;
        buffer[index++] = top;
        buffer[index++] = right;
        buffer[index++] = bottom;
    }


    @Override
    public void feed(IDataSet data) {

        float size = data.getEntryCount() * phaseX;
        float barWidthHalf = mBarWidth / 2f;

        for (int i = 0; i < size; i++) {
            BarEntry e = (BarEntry) data.getEntryForIndex(i);
            if (e == null) {
                continue;
            }

            float x = e.getX();
            float y = e.getY();
            float[] vals = e.getYVals();

            if (!mContainsStacks || vals == null) {

                float left = x - barWidthHalf;
                float right = x + barWidthHalf;

                float bottom, top;

                if (mInverted) {
                    bottom = y >= 0 ? y : 0;
                    top = y <= 0 ? y : 0;

                } else {
                    top = y >= 0 ? y : 0;
                    bottom = y <= 0 ? y : 0;
                }

                if (top > 0)
                    top *= phaseY;
                else
                    bottom *= phaseY;

                addBar(left, top, right, bottom);

            } else {
                //TODO 堆叠图暂不处理


            }


        }
        //将index重置为1
        reset();
    }
}
