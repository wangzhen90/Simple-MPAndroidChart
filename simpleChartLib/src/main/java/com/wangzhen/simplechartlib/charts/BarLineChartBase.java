package com.wangzhen.simplechartlib.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.wangzhen.simplechartlib.component.YAxis;
import com.wangzhen.simplechartlib.data.chartData.BarData;
import com.wangzhen.simplechartlib.data.chartData.BarLineScatterCandleBubbleData;
import com.wangzhen.simplechartlib.data.entry.Entry;
import com.wangzhen.simplechartlib.interfaces.charts.BarLineScatterCandleBubbleDataProvider;
import com.wangzhen.simplechartlib.interfaces.dataSets.IBarLineScatterCandleBubbleDataSet;
import com.wangzhen.simplechartlib.renderer.XAxisRenderer;
import com.wangzhen.simplechartlib.utils.MPPointD;
import com.wangzhen.simplechartlib.utils.Transformer;
import com.wangzhen.simplechartlib.utils.Utils;

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

    protected YAxis mAxisLeft;
    //    protected YAxisRenderer mAxisRendererLeft;
    protected YAxis mAxisRight;
//    protected YAxisRenderer mAxisRendererRight;

    protected Transformer mLeftAxisTransformer;
    //    protected Transformer mRightAxisTransformer;
    protected XAxisRenderer mXAxisRenderer;


    public BarLineChartBase(Context context) {
        super(context);
    }

    public BarLineChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarLineChartBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void init() {
        super.init();

        mAxisLeft = new YAxis();

        mLeftAxisTransformer = new Transformer(mViewPortHandler);

//        mAxisRendererLeft = new YAxisRenderer(mViewPortHandler, mAxisLeft, mLeftAxisTransformer);
        mXAxisRenderer = new XAxisRenderer(mViewPortHandler, mXAxis, mLeftAxisTransformer);

//        mGridBackgroundPaint = new Paint();
//        mGridBackgroundPaint.setStyle(Paint.Style.FILL);
//        // mGridBackgroundPaint.setColor(Color.WHITE);
//        mGridBackgroundPaint.setColor(Color.rgb(240, 240, 240)); // light
//        // grey
//
//        mBorderPaint = new Paint();
//        mBorderPaint.setStyle(Paint.Style.STROKE);
//        mBorderPaint.setColor(Color.BLACK);
//        mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(1f));


    }


    private long totalTime = 0;
    private long drawCycles = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mData == null) return;

        long startTime = System.currentTimeMillis();


        if (mXAxis.isEnabled()) {
            mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);
        }

        mXAxisRenderer.renderAxisLine(canvas);
        mXAxisRenderer.renderGridLines(canvas);
//        if (mXAxis.isEnabled() && mXAxis.isDrawLimitLinesBehindDataEnabled())
//            mXAxisRenderer.renderLimitLines(canvas);

        //裁剪一下，防止data绘制在了区域外边
        int clipRestoreCount = canvas.save();
        canvas.clipRect(mViewPortHandler.getContentRect());
        //TODO 绘制data
        canvas.restoreToCount(clipRestoreCount);

        mXAxisRenderer.renderAxisLabels(canvas);
    }

    public void resetTracking() {
        totalTime = 0;
        drawCycles = 0;
    }

    private RectF mOffsetsBuffer = new RectF();

    @Override
    protected void calculateOffsets() {
        float offsetLeft = 0f, offsetRight = 0f, offsetTop = 0f, offsetBottom = 0f;


        if (mXAxis.isEnabled() && mXAxis.isDrawLabelsEnabled()) {

            float xLabelHeight = mXAxis.mLabelRotatedHeight + mXAxis.getYOffset();

            offsetBottom += xLabelHeight;


        }

        offsetTop += getExtraTopOffset();
        offsetRight += getExtraRightOffset();
        offsetBottom += getExtraBottomOffset();
        offsetLeft += getExtraLeftOffset();

        float minOffset = Utils.convertDpToPixel(mMinOffset);

        mViewPortHandler.restrainViewPort(
                Math.max(minOffset, offsetLeft),
                Math.max(minOffset, offsetTop),
                Math.max(minOffset, offsetRight),
                Math.max(minOffset, offsetBottom));

        prepareOffsetMatrix();
        prepareValuePxMatrix();

    }

    protected void prepareOffsetMatrix() {

        mLeftAxisTransformer.prepareMatrixOffset(false);
    }


    protected void prepareValuePxMatrix() {

        mLeftAxisTransformer.prepareMatrixValuePx(mXAxis.mAxisMinimum,
                mXAxis.mAxisRange,
                mAxisLeft.mAxisRange,
                mAxisLeft.mAxisMinimum);
    }

    @Override
    protected void calcMinMax() {

        mXAxis.calculate(mData.getXMin(), mData.getXMax());
        // calculate axis range (min / max) according to provided data
        mAxisLeft.calculate(mData.getYMin(), mData.getYMax());
        //mAxisRight.calculate(mData.getYMin(), mData.getYMax());
    }


    public Transformer getTransformer() {
            return mLeftAxisTransformer;
    }

    public void setViewPortOffsets(final float left, final float top,
                                   final float right, final float bottom) {

//        mCustomViewPortEnabled = true;
        post(new Runnable() {

            @Override
            public void run() {

                mViewPortHandler.restrainViewPort(left, top, right, bottom);
                prepareOffsetMatrix();
                prepareValuePxMatrix();
            }
        });
    }

    protected float[] mOnSizeChangedBuffer = new float[2];
    protected boolean mKeepPositionOnRotation = false;


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mOnSizeChangedBuffer[0] = mOnSizeChangedBuffer[1] = 0;
        /**
         * 没看懂这波操作
         */

        if (mKeepPositionOnRotation) {
            mOnSizeChangedBuffer[0] = mViewPortHandler.contentLeft();
            mOnSizeChangedBuffer[1] = mViewPortHandler.contentTop();
            getTransformer().pixelsToValue(mOnSizeChangedBuffer);
        }

        //Superclass transforms chart.
        super.onSizeChanged(w, h, oldw, oldh);

        if (mKeepPositionOnRotation) {

            //Restoring old position of chart.
            getTransformer().pointValuesToPixel(mOnSizeChangedBuffer);
            mViewPortHandler.centerViewPort(mOnSizeChangedBuffer, this);
        } else {
            mViewPortHandler.refresh(mViewPortHandler.getMatrixTouch(), this, true);
        }

    }


    @Override
    public void notifyDataSetChanged() {
        if(mData == null){
            return;
        }
//        if (mRenderer != null)
//            mRenderer.initBuffers();
        calcMinMax();

//        mAxisRendererLeft.computeAxis(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisMaximum, false);
//        mAxisRendererRight.computeAxis(mAxisRight.mAxisMinimum, mAxisRight.mAxisMaximum, false);
        mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);

//        if (mLegend != null)
//            mLegendRenderer.computeLegend(mData);

        calculateOffsets();


    }

    //x可以显示出来的最小的值
    protected MPPointD posForGetLowestVisibleX = MPPointD.getInstance(0, 0);
    @Override
    public float getLowestVisibleX() {
        getTransformer().getValuesByTouchPoint(mViewPortHandler.contentLeft(),
                mViewPortHandler.contentBottom(), posForGetLowestVisibleX);
        float result = (float) Math.max(mXAxis.mAxisMinimum, posForGetLowestVisibleX.x);
        return result;
    }

    protected MPPointD posForGetHighestVisibleX = MPPointD.getInstance(0, 0);

    /**
     * Returns the highest x-index (value on the x-axis) that is still visible
     * on the chart.
     *
     * @return
     */
    @Override
    public float getHighestVisibleX() {
        getTransformer().getValuesByTouchPoint(mViewPortHandler.contentRight(),
                mViewPortHandler.contentBottom(), posForGetHighestVisibleX);
        float result = (float) Math.min(mXAxis.mAxisMaximum, posForGetHighestVisibleX.x);
        return result;
    }

    @Override
    public float getYChartMin() {
        return Math.min(mAxisLeft.mAxisMinimum, mAxisRight.mAxisMinimum);
    }
    @Override
    public float getYChartMax() {
        return Math.max(mAxisLeft.mAxisMaximum, mAxisRight.mAxisMaximum);
    }

    public int getMaxVisibleCount() {
        return mMaxVisibleCount;
    }



}
