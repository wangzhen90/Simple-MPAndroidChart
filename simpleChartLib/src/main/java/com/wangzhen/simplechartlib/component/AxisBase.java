package com.wangzhen.simplechartlib.component;

import android.graphics.Color;
import android.graphics.DashPathEffect;

import com.wangzhen.simplechartlib.formatter.DefaultAxisValueFormatter;
import com.wangzhen.simplechartlib.formatter.IAxisValueFormatter;
import com.wangzhen.simplechartlib.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhen on 2018/3/16.
 */

public abstract class AxisBase extends ComponentBase {


    protected IAxisValueFormatter mAxisValueFormatter;


    private int mGridColor = Color.GRAY;


    private float mGridLineWidth = 1f;

    private int mAxisLineColor = Color.GRAY;

    private float mAxisLineWidth = 1f;


    public float[] mEntries = new float[]{};

    /**
     * TODO 暂时不知道作用
     */
    public float[] mCenteredEntries = new float[]{};

    public int mEntryCount;

    /**
     * 小数的位数
     */
    public int mDecimals;

    /**
     * //TODO 暂时不知道作用
     * 这条轴上label的数量
     */
    private int mLabelCount = 6;
    /**
     * 轴值之间的最小间隔
     */
    protected float mGranularity = 1.0f;


    /**
     * 当正确时，轴标签由“粒度”属性控制。
     * 当错误时，轴值可能会重复。
     * 如果两个相邻的轴值是四舍五入到相同的值，就会发生这种情况。
     * 如果使用粒度，可以通过减少可见的轴值来避免。
     */
    protected boolean mGranularityEnabled = false;


    //TODO 暂时不用
//    protected boolean mForceLabels = false;

    /**
     * 是否绘制每个组之间的分割线
     */
    protected boolean mDrawGridLines = true;

    /**
     * 是否绘制坐标轴线
     */
    protected boolean mDrawAxisLine = true;

    protected boolean mDrawLabels = true;


    /**
     * TODO 暂时不知道作用
     */
    protected boolean mCenterAxisLabels = false;


    /**
     * TODO 虚线效果暂不考虑
     */
//    private DashPathEffect mAxisLineDashPathEffect = null;

    /**
     * 这个分割线的虚线可以有
     */
    private DashPathEffect mGridDashPathEffect = null;


/**
 *  TODO 类似有及格线，暂不需要
 */
//    protected List<LimitLine> mLimitLines;
//protected boolean mDrawLimitLineBehindData = false;


    /**
     * `axisMinimum` 的额外值，会被自动加上
     */

    protected float mSpaceMin = 0.f;

    protected float mSpaceMax = 0.f;


    /**
     * TODO 自定义坐标轴的最大最小值相关，暂不需要
     */
//protected boolean mCustomAxisMin = false;
//protected boolean mCustomAxisMax = false;


    public float mAxisMaximum = 0f;
    public float mAxisMinimum = 0f;


    public float mAxisRange = 0f;


    public AxisBase() {
        this.mTextSize = Utils.convertDpToPixel(10f);
        this.mXOffset = Utils.convertDpToPixel(5f);
        this.mYOffset = Utils.convertDpToPixel(5f);
//        this.mLimitLines = new ArrayList<LimitLine>();
    }

    public void setDrawGridLines(boolean enabled) {
        mDrawGridLines = enabled;
    }

    public boolean isDrawGridLinesEnabled() {
        return mDrawGridLines;
    }

    public void setDrawAxisLine(boolean enabled) {
        mDrawAxisLine = enabled;
    }

    public boolean isDrawAxisLineEnabled() {
        return mDrawAxisLine;
    }


    /**
     * 将轴心标签置于中心位置，而不是将其画到原来的位置。对分组的条形图这是有用的。
     */

    public void setCenterAxisLabels(boolean enabled) {
        mCenterAxisLabels = enabled;
    }

    public boolean isCenterAxisLabelsEnabled() {
        return mCenterAxisLabels && mEntryCount > 0;
    }


    public void setGridColor(int color) {
        mGridColor = color;
    }

    public int getGridColor() {
        return mGridColor;
    }

    public void setAxisLineWidth(float width) {
        mAxisLineWidth = Utils.convertDpToPixel(width);
    }

    public float getAxisLineWidth() {
        return mAxisLineWidth;
    }


    public void setGridLineWidth(float width) {
        mGridLineWidth = Utils.convertDpToPixel(width);
    }

    public float getGridLineWidth() {
        return mGridLineWidth;
    }


    public void setAxisLineColor(int color) {
        mAxisLineColor = color;
    }

    public int getAxisLineColor() {
        return mAxisLineColor;
    }

    public void setDrawLabels(boolean enabled) {
        mDrawLabels = enabled;
    }

    public boolean isDrawLabelsEnabled() {
        return mDrawLabels;
    }


    /**
     * TODO 暂时先不用
     * @param
     */
//    public void setLabelCount(int count) {
//
//        if (count > 25)
//            count = 25;
//        if (count < 2)
//            count = 2;
//
//        mLabelCount = count;
//    }
//
//    public int getLabelCount() {
//        return mLabelCount;
//    }

    /**
     * 暂时不用
     */
//    public boolean isGranularityEnabled() {
//        return mGranularityEnabled;
//    }
//
//    public void setGranularityEnabled(boolean enabled) {
//        mGranularityEnabled = enabled;
//    }

//    public float getGranularity() {
//        return mGranularity;
//    }

//    public void setGranularity(float granularity) {
//        mGranularity = granularity;
//        // set this to true if it was disabled, as it makes no sense to call this method with granularity disabled
//        mGranularityEnabled = true;
//    }

    /**
     * 获取所有label中最长的，使用场景在于，当label设置了旋转角度，绘制chart的区域就要根据角度和这个长度来计算，
     * 感觉这种设定并不好，会造成chart的区域大小不一，不如添加设置label显示的最大最小长度
     * 这个可以没有
     *
     * @return
     */
    public String getLongestLabel() {

        String longest = "";

        for (int i = 0; i < mEntries.length; i++) {
            String text = getFormattedLabel(i);

            if (text != null && longest.length() < text.length())
                longest = text;
        }

        return longest;
    }

    public String getFormattedLabel(int index) {

        if (index < 0 || index >= mEntries.length)
            return "";
        else
            return getValueFormatter().getFormattedValue(mEntries[index], this);
    }


    public void setValueFormatter(IAxisValueFormatter f) {

        if (f == null)
            mAxisValueFormatter = new DefaultAxisValueFormatter(mDecimals);
        else
            mAxisValueFormatter = f;
    }

    public IAxisValueFormatter getValueFormatter() {

        if (mAxisValueFormatter == null ||
                (mAxisValueFormatter instanceof DefaultAxisValueFormatter &&
                        ((DefaultAxisValueFormatter) mAxisValueFormatter).getDecimalDigits() != mDecimals))
            mAxisValueFormatter = new DefaultAxisValueFormatter(mDecimals);

        return mAxisValueFormatter;
    }


    /**
     * 设置虚线
     *
     * @param lineLength  the length of the line pieces
     * @param spaceLength the length of space in between the pieces
     * @param phase       offset, in degrees (normally, use 0)
     */
    public void enableGridDashedLine(float lineLength, float spaceLength, float phase) {
        mGridDashPathEffect = new DashPathEffect(new float[]{
                lineLength, spaceLength
        }, phase);
    }

    public void setGridDashedLine(DashPathEffect effect) {
        mGridDashPathEffect = effect;
    }

    public void disableGridDashedLine() {
        mGridDashPathEffect = null;
    }

    public boolean isGridDashedLineEnabled() {
        return mGridDashPathEffect == null ? false : true;
    }

    public DashPathEffect getGridDashPathEffect() {
        return mGridDashPathEffect;
    }

    public float getAxisMaximum() {
        return mAxisMaximum;
    }

    public float getAxisMinimum(){
        return mAxisMinimum;
    }

    public void setAxisMinimum(float min){
        mAxisMinimum = min;

        this.mAxisRange = Math.abs(mAxisMaximum - mAxisMinimum);
    }


    public void setAxisMaximum(float max){
        this.mAxisMaximum = max;

        this.mAxisRange = Math.abs(mAxisMaximum - mAxisMinimum);
    }

    public void calculate(float dataMin,float dataMax){

        float min = dataMin - mSpaceMin;
        float max = dataMax - mSpaceMax;


        float range = Math.abs(max - min);

        //如果所有的值都相等
        if (range == 0f) {
            max = max + 1f;
            min = min - 1f;
            range = 2;
        }


        this.mAxisMaximum = max;
        this.mAxisMinimum = min;

        this.mAxisRange = range;
    }

    public float getSpaceMin(){
        return  mSpaceMin;
    }

    public float getSpaceMax(){
        return mSpaceMax;
    }

    public void setSpaceMin(float mSpaceMin)
    {
        this.mSpaceMin = mSpaceMin;
    }

    public void setSpaceMax(float mSpaceMax)
    {
        this.mSpaceMax = mSpaceMax;
    }




}
