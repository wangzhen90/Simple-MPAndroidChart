package com.wangzhen.simplechartlib.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.wangzhen.simplechartlib.component.YAxis;
import com.wangzhen.simplechartlib.data.chartData.BarLineScatterCandleBubbleData;
import com.wangzhen.simplechartlib.data.entry.Entry;
import com.wangzhen.simplechartlib.interfaces.charts.BarLineScatterCandleBubbleDataProvider;
import com.wangzhen.simplechartlib.interfaces.dataSets.IBarLineScatterCandleBubbleDataSet;

/**
 * Created by wangzhen on 2018/3/18.
 */

public abstract class BarLineChartBase<T extends BarLineScatterCandleBubbleData<? extends
        IBarLineScatterCandleBubbleDataSet<? extends Entry>>> extends Chart<T> implements BarLineScatterCandleBubbleDataProvider {

    /**
     * 绘制entity的个数
     */
    protected int mMaxVisibleCount = 100;
    protected boolean mAutoScaleMinMaxEnable = false;

    /**
     * x,y 同时缩放
     */
    protected boolean mPinchZoomEnable = false;
    /**
     * 双击缩放
     */
    protected boolean mDoubleTapToZoomEnabled = true;

    private boolean mDragXEnabled = true;
    private boolean mDragYEnabled = true;

    private boolean mScaleXEnabled = true;
    private boolean mScaleYEnabled = true;

    /**
     * TODO 暂时不知道怎么用
     */
    protected boolean mClipValuesToContent = false;
    /**
     * 整个chart的padding
     */
    protected float mMinOffset = 15.f;

    /**
     * TODO 暂时不知道怎么用
     */
//    protected OnDrawListener mDrawListener;

    protected YAxis mYAxis;




    public BarLineChartBase(Context context) {
        super(context);
    }

    public BarLineChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarLineChartBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    


}
