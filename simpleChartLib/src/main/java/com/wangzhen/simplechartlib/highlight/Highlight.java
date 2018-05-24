package com.wangzhen.simplechartlib.highlight;

import com.wangzhen.simplechartlib.component.YAxis;

/**
 * Created by wangzhen on 2018/5/24.
 */

public class Highlight {

    /**
     * value值
     */
    private float mX = Float.NaN,mY = Float.NaN;
    /**
     * px值
     */
    private float mXPx,mYPx;


    private int mDataSetIndex;
    private int mDataIndex = -1;
    private int mStackIndex = -1;

    /**
     * highlight的绘制位置
     */
    private float mDrawX,mDrawY;

    public Highlight(float x, float y, int datasetIndex){
        this.mX = x;
        this.mY = y;
        this.mDataSetIndex = datasetIndex;

    }

    public Highlight(float x,int dataSetIndex,int stackIndex){
        this.mX = x;
        this.mDataSetIndex = dataSetIndex;
        this.mStackIndex = stackIndex;
    }

    public Highlight(float x, float y, float xPx, float yPx, int dataSetIndex) {
        this.mX = x;
        this.mY = y;
        this.mXPx = xPx;
        this.mYPx = yPx;
        this.mDataSetIndex = dataSetIndex;
    }

    public Highlight(float x, float y, float xPx, float yPx, int dataSetIndex, int stackIndex) {
        this(x, y, xPx, yPx, dataSetIndex);
        this.mStackIndex = stackIndex;
    }



    public float getX() {
        return mX;
    }
    public float getY() {
        return mY;
    }
    public float getXPx() {
        return mXPx;
    }
    public float getYPx() {
        return mYPx;
    }
    public int getDataIndex() {
        return mDataIndex;
    }
    public void setDataIndex(int mDataIndex) {
        this.mDataIndex = mDataIndex;
    }
    public int getDataSetIndex() {
        return mDataSetIndex;
    }
    public int getStackIndex() {
        return mStackIndex;
    }

    public boolean isStacked() {
        return mStackIndex >= 0;
    }

    public void setDraw(float x, float y) {
        this.mDrawX = x;
        this.mDrawY = y;
    }

    public float getDrawX() {
        return mDrawX;
    }

    public float getDrawY() {
        return mDrawY;
    }


    public boolean equalTo(Highlight h) {

        if (h == null)
            return false;
        else {
            if (this.mDataSetIndex == h.mDataSetIndex && this.mX == h.mX
                    && this.mStackIndex == h.mStackIndex && this.mDataIndex == h.mDataIndex)
                return true;
            else
                return false;
        }
    }

}
