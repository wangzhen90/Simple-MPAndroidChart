package com.wangzhen.simplechartlib.component;

import android.graphics.Color;
import android.graphics.Paint;

import com.wangzhen.simplechartlib.utils.Utils;

/**
 * Created by wangzhen on 2018/3/18.
 */

public class YAxis extends AxisBase {


    protected boolean mDrawZeroLine = false;

    protected int mZeroLineColor = Color.GRAY;

    protected float mZeroLineWidth = 1f;

    /**
     * 未看懂
     */
    protected float mSpacePercentTop = 10f;

    protected float mSpacePercentBottom = 10f;

    protected float mMinWidth = 0.f;

    protected float mMaxWidth = Float.POSITIVE_INFINITY;
    protected boolean mInverted = false;


    public YAxis() {
        super();

        // default left
        this.mYOffset = 0f;
    }

    public float getMinWidth() {
        return mMinWidth;
    }

    public void setMinWidth(float minWidth) {
        mMinWidth = minWidth;
    }

    public float getMaxWidth() {
        return mMaxWidth;
    }

    public void setMaxWidth(float maxWidth) {
        mMaxWidth = maxWidth;
    }

    /**
     * 暂时不用，控制label是否在chart内
     */

//    public YAxisLabelPosition getLabelPosition() {
//        return mPosition;
//    }

    public void setInverted(boolean enabled) {
        mInverted = enabled;
    }

    public void setSpaceTop(float percent) {
        mSpacePercentTop = percent;
    }

    public float getSpaceTop() {
        return mSpacePercentTop;
    }

    public void setSpaceBottom(float percent) {
        mSpacePercentBottom = percent;
    }


    public float getSpaceBottom() {
        return mSpacePercentBottom;
    }

    /**
     * 正常模式，不是水平chart,这个width是根据label计算出来的
     * @param p
     * @return
     */
    public float getRequiredWidthSpace(Paint p){
        p.setTextSize(mTextSize);

        String label = getLongestLabel();

        float width = (float) Utils.calcTextWidth(p,label) + getXOffset() *2f;

        float minWidth = getMinWidth();
        float maxWidth = getMaxWidth();

        if (minWidth > 0.f)
            minWidth = Utils.convertDpToPixel(minWidth);

        if (maxWidth > 0.f && maxWidth != Float.POSITIVE_INFINITY)
            maxWidth = Utils.convertDpToPixel(maxWidth);

        width = Math.max(minWidth, Math.min(width, maxWidth > 0.0 ? maxWidth : width));

        return width;

    }

    /**
     * HorizontalBarChart vertical spacing
     * @param p
     * @return
     */
    public float getRequireHeightSpace(Paint p){

        p.setTextSize(mTextSize);

        String label = getLongestLabel();

        return (float) Utils.calcTextHeight(p, label) + getYOffset() * 2f;

    }


//    public boolean needsOffset() {
//        if (isEnabled() && isDrawLabelsEnabled() && getLabelPosition() == YAxisLabelPosition
//                .OUTSIDE_CHART)
//            return true;
//        else
//            return false;
//    }


    @Override
    public void calculate(float dataMin, float dataMax) {

        float min =  dataMin;
        float max =  dataMax;
        float range = Math.abs(max - min);

        if (range == 0f) {
            max = max + 1f;
            min = min - 1f;
        }

        float bottomSpace = range / 100f * getSpaceBottom();
        this.mAxisMinimum = (min - bottomSpace);

        float topSpace = range / 100f * getSpaceTop();
        this.mAxisMaximum = (max + topSpace);

        this.mAxisRange = Math.abs(this.mAxisMaximum - this.mAxisMinimum);

    }
}
