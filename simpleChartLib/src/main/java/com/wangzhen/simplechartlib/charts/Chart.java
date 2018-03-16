package com.wangzhen.simplechartlib.charts;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.wangzhen.simplechartlib.data.ChartData;
import com.wangzhen.simplechartlib.data.entry.Entry;
import com.wangzhen.simplechartlib.formatter.DefaultValueFormatter;
import com.wangzhen.simplechartlib.interfaces.ChartInterface;
import com.wangzhen.simplechartlib.interfaces.dataSets.IDataSet;

/**
 * Created by wangzhen on 2018/3/16.
 */

public abstract class Chart<T extends ChartData<? extends IDataSet<? extends Entry>>> extends ViewGroup implements ChartInterface {

    public static final String LOG_TAG = "SIMPLE-MPAndroidChart";
    protected boolean mLogEnabled = false;

    protected T mData = null;
//TODO
//    protected boolean mHighLightPerTapEnabled = true;

    /**
     * 停止滑动后是否惯性滑动
     */
    private boolean mDragDecelerationEnabled = true;

    /**
     * 惯性滑动摩擦系数，取值范围[0,1],如果为0就没有惯性滑动
     */
    private float mDragDecelerationFrictionCoef = 0.9f;

    protected DefaultValueFormatter mDefaultValueFormatter = new DefaultValueFormatter(0);

//    暂时不需要
//    protected Paint mDescPaint;

// 如果没有值的时候提示信息的绘制画笔，暂时不需要
//    protected Paint mInfoPaint;







    public Chart(Context context) {
        super(context);
    }

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Chart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }








}
