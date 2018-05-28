package com.wangzhen.simplechartlib.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.wangzhen.simplechartlib.component.YAxis;
import com.wangzhen.simplechartlib.data.chartData.BarData;
import com.wangzhen.simplechartlib.data.chartData.BarLineScatterCandleBubbleData;
import com.wangzhen.simplechartlib.data.entry.Entry;
import com.wangzhen.simplechartlib.highlight.ChartHighlighter;
import com.wangzhen.simplechartlib.interfaces.charts.BarLineScatterCandleBubbleDataProvider;
import com.wangzhen.simplechartlib.interfaces.dataSets.IBarLineScatterCandleBubbleDataSet;
import com.wangzhen.simplechartlib.listener.BarLineChartTouchListener;
import com.wangzhen.simplechartlib.renderer.XAxisRenderer;
import com.wangzhen.simplechartlib.renderer.YAxisRenderer;
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
    protected YAxisRenderer mAxisRendererLeft;
    protected YAxis mAxisRight;
//    protected YAxisRenderer mAxisRendererRight;

    protected Transformer mLeftAxisTransformer;
    //    protected Transformer mRightAxisTransformer;
    protected XAxisRenderer mXAxisRenderer;

    protected boolean mPinchZoomEnabled = false;

    protected boolean mHighlightPerDragEnabled = true;



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

        mAxisRendererLeft = new YAxisRenderer(mViewPortHandler, mAxisLeft, mLeftAxisTransformer);
        mXAxisRenderer = new XAxisRenderer(mViewPortHandler, mXAxis, mLeftAxisTransformer);

        mChartTouchListener = new BarLineChartTouchListener(this,mViewPortHandler.getMatrixTouch(),3f);

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

        setHighlighter(new ChartHighlighter(this));

    }


    private long totalTime = 0;
    private long drawCycles = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "2.begin onDraw...");


        if (mData == null) return;

        long startTime = System.currentTimeMillis();

        Log.e("BarChart", "2.1 开始绘制X轴...");
        if (mXAxis.isEnabled()) {
            mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);
        }


        mXAxisRenderer.renderAxisLine(canvas);
        mXAxisRenderer.renderGridLines(canvas);

        if (mAxisLeft.isEnabled()) {
            mAxisRendererLeft.computeAxis(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisMaximum, false);
        }

        mAxisRendererLeft.renderAxisLine(canvas);
        mAxisRendererLeft.renderGridLines(canvas);


        //裁剪一下，防止data绘制在了区域外边
        int clipRestoreCount = canvas.save();
        canvas.clipRect(mViewPortHandler.getContentRect());
        //绘制data
        mRenderer.drawData(canvas);
        if(valuesToHighlight()){
            mRenderer.drawHighlighted(canvas,mIndicesToHighlight);
        }


        canvas.restoreToCount(clipRestoreCount);

        mXAxisRenderer.renderAxisLabels(canvas);
        mAxisRendererLeft.renderAxisLabels(canvas);
        mRenderer.drawValues(canvas);
    }

    public void resetTracking() {
        totalTime = 0;
        drawCycles = 0;
    }

    private RectF mOffsetsBuffer = new RectF();

    @Override
    public void calculateOffsets() {
        float offsetLeft = 0f, offsetRight = 0f, offsetTop = 0f, offsetBottom = 0f;


        if (mXAxis.isEnabled() && mXAxis.isDrawLabelsEnabled()) {
            float xLabelHeight = mXAxis.mLabelRotatedHeight + mXAxis.getYOffset();
            offsetBottom += xLabelHeight;
        }

        if (mAxisLeft.isEnabled() && mAxisLeft.isDrawLabelsEnabled()) {
            float longestYLabelWidth = mAxisLeft.getRequiredWidthSpace(mAxisRendererLeft.getPaintAxisLabels());
            offsetLeft += longestYLabelWidth;
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

        Log.e(TAG, "1.3: notifyDataChanged :计算offsets，本质就是根据设置的offset重新设置mViewHandler的content区域");
        Log.e(TAG, "1.4:notifyDataChanged :调用prepareOffsetMatrix 和 prepareValuePxMatrix 初始化左右两个y轴的transformer的offsetMatrix和valueToPxMatrix,这一步很重要");


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
        Log.e(TAG, "1.1:notifyDataSetChanged : 计算x，y轴的最大最小值,X最大最小值分别为：" + mXAxis.getAxisMaximum() + "," + mXAxis.getAxisMinimum()

                + ",Y最大最小值分别为：" + mAxisLeft.getAxisMaximum() + "," + mAxisLeft.getAxisMinimum()
        );

    }

    @Override
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
        if (mData == null) {
            return;
        }
        if (mRenderer != null)
            mRenderer.initBuffers();
        calcMinMax();

        mAxisRendererLeft.computeAxis(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisMaximum, false);
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

    public YAxis getAxisLeft() {
        return mAxisLeft;
    }




    /**
     * *****************************手势相关******************************
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
         super.onTouchEvent(event);

        if(!mTouchEnable || mChartTouchListener == null || mData == null){
            return false;
        }

        return mChartTouchListener.onTouch(this,event);

    }


    public void setDragEnabled(boolean enabled) {
        this.mDragXEnabled = enabled;
        this.mDragYEnabled = enabled;
    }


    public boolean isDragEnabled() {
        return mDragXEnabled || mDragYEnabled;
    }

    public boolean isDragXEnabled() {
        return mDragXEnabled;
    }

    public void setDragXEnabled(boolean enabled) {
        this.mDragXEnabled = enabled;
    }

    public boolean isDragYEnabled() {
        return mDragYEnabled;
    }

    public void setDragYEnabled(boolean enabled) {
        this.mDragYEnabled = enabled;
    }

    public void setScaleEnabled(boolean enabled) {
        this.mScaleXEnabled = enabled;
        this.mScaleYEnabled = enabled;
    }

    public void setScaleXEnabled(boolean enabled) {
        mScaleXEnabled = enabled;
    }

    public void setScaleYEnabled(boolean enabled) {
        mScaleYEnabled = enabled;
    }

    public boolean isScaleXEnabled() {
        return mScaleXEnabled;
    }

    public boolean isScaleYEnabled() {
        return mScaleYEnabled;
    }

    public void setDoubleTapToZoomEnabled(boolean enabled) {
        mDoubleTapToZoomEnabled = enabled;
    }

    public boolean isDoubleTapToZoomEnabled() {
        return mDoubleTapToZoomEnabled;
    }

    public void setPinchZoom(boolean enabled) {
        mPinchZoomEnabled = enabled;
    }

    /**
     * 捏合手势
     *
     * @return
     */
    public boolean isPinchZoomEnabled() {
        return mPinchZoomEnabled;
    }


    protected Matrix mZoomMatrixBuffer = new Matrix();

    /**
     * 缩放
     */
    public void zoom(float scaleX, float scaleY, float x, float y) {
        //TODO 为什么y要取相反数
        mViewPortHandler.zoom(scaleX, scaleY, x, y, mZoomMatrixBuffer);
        mViewPortHandler.refresh(mZoomMatrixBuffer, this, false);


        // Range might have changed, which means that Y-axis labels
        // could have changed in size, affecting Y-axis size.
        // So we need to recalculate offsets.
        calculateOffsets();
        postInvalidate();
    }

    public boolean isFullyZoomedOut() {
        return mViewPortHandler.isFullyZoomedOut();
    }

    /**
     * 是否没有拖拽的位移
     * @return
     */
    public boolean hasNoDragOffset() {
        return mViewPortHandler.hasNoDragOffset();
    }

    @Override
    public void computeScroll() {

        if (mChartTouchListener instanceof BarLineChartTouchListener)
            ((BarLineChartTouchListener) mChartTouchListener).computeScroll();
    }

    public void setHighlightPerDragEnabled(boolean enabled) {
        mHighlightPerDragEnabled = enabled;
    }

    public boolean isHighlightPerDragEnabled() {
        return mHighlightPerDragEnabled;
    }

}
