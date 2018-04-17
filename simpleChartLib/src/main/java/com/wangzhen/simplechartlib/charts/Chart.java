package com.wangzhen.simplechartlib.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.wangzhen.simplechartlib.component.XAxis;
import com.wangzhen.simplechartlib.data.chartData.ChartData;
import com.wangzhen.simplechartlib.data.entry.Entry;
import com.wangzhen.simplechartlib.formatter.DefaultValueFormatter;
import com.wangzhen.simplechartlib.formatter.IValueFormatter;
import com.wangzhen.simplechartlib.interfaces.charts.ChartInterface;
import com.wangzhen.simplechartlib.interfaces.dataSets.IDataSet;
import com.wangzhen.simplechartlib.utils.MPPointF;
import com.wangzhen.simplechartlib.utils.Utils;
import com.wangzhen.simplechartlib.utils.ViewPortHandler;

import java.util.ArrayList;

/**
 * Created by wangzhen on 2018/3/16.
 */

public abstract class Chart<T extends ChartData<? extends IDataSet<? extends Entry>>> extends ViewGroup implements ChartInterface {

    public static final String LOG_TAG = "SIMPLE-MPAndroidChart";
    protected boolean mLogEnabled = false;
    public final String TAG = this.getClass().getSimpleName();

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


    protected XAxis mXAxis;

    protected boolean mTouchEnable = true;


    //暂时不用
//    protected Description mDescription;

    /**
     * 手势相关和动画暂时不处理，绘制完图再说
     *
     */

//    private OnChartGestureListener mGestureListener;
//
//    protected LegendRenderer mLegendRenderer;

//    protected ChartAnimator mAnimator;

    /**
     * 绘制Data 暂不处理
     */

//    protected DataRenderer mRenderer;

//    protected IHighlighter mHighlighter;

    protected ViewPortHandler mViewPortHandler = new ViewPortHandler();


    private String mNoDataText = "No chart data available.";

    /**
     * 对ViewPort而言的额外的偏移值，Viewport 是可是区域？
     */
    private float mExtraTopOffset = 0.f,
            mExtraRightOffset = 0.f,
            mExtraBottomOffset = 0.f,
            mExtraLeftOffset = 0.f;

    /**
     * offset是否已经计算过的标志
     *
     */
    private boolean mOffsetsCalculated = false;

    protected Paint mInfoPaint;



    public Chart(Context context) {
        super(context);
        init();

    }

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Chart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    protected void init() {

        setWillNotDraw(false);

        /**
         * TODO 动画相关
         */

//        if (android.os.Build.VERSION.SDK_INT < 11)
//            mAnimator = new ChartAnimator();
//        else
//            mAnimator = new ChartAnimator(new ValueAnimator.AnimatorUpdateListener() {
//
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    // ViewCompat.postInvalidateOnAnimation(Chart.this);
//                    postInvalidate();
//                }
//            });


        Utils.init(getContext());

        //TODO 初始化点击空白区域的时候，触发点击区域的最大值
//        mMaxHighlightDistance = Utils.convertDpToPixel(500f);

        mXAxis = new XAxis();

        mInfoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInfoPaint.setColor(Color.rgb(247, 189, 51)); // orange
        mInfoPaint.setTextAlign(Paint.Align.CENTER);
        mInfoPaint.setTextSize(Utils.convertDpToPixel(12f));
    }

    public void setData(T data) {
        mData = data;

        if (data == null) {
            return;
        }

        setupDefaultFormatter(data.getYMin(), data.getYMax());

        for (IDataSet set : mData.getDataSets()) {

            if (set.needsFormatter() || set.getValueFormatter() == mDefaultValueFormatter)
                set.setValueFormatter(mDefaultValueFormatter);
        }

        Log.e(TAG,"1.调用+"+TAG+"+的setData方法，调用notifyDataSetChanged，开始计算各种offset，最大最小值");

        notifyDataSetChanged();
    }


    /**
     * 计算格式化需要保留几位小数
     *
     * @param min
     * @param max
     */
    protected void setupDefaultFormatter(float min, float max) {

        float reference = 0f;

        if (mData == null || mData.getEntryCount() < 2) {

            reference = Math.max(Math.abs(min), Math.abs(max));
        } else {
            reference = Math.abs(max - min);
        }

        int digits = Utils.getDecimals(reference);

        // setup the formatter with a new number of digits
        mDefaultValueFormatter.setup(digits);
    }

    /**
     * 设置数据或者数据变化的时候调用这个方法，会重新对数据进行计算
     */
    public abstract void notifyDataSetChanged();

    protected abstract void calculateOffsets();

    protected abstract void calcMinMax();



    public void clear() {
        mData = null;

        invalidate();
    }

    public void clearValues() {
        mData.clearValues();
        invalidate();
    }

    public boolean isEmpty() {

        if (mData == null)
            return true;
        else {

            if (mData.getEntryCount() <= 0)
                return true;
            else
                return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mData == null){
            boolean hasText = !TextUtils.isEmpty(mNoDataText);
            if (hasText) {
                MPPointF c = getCenter();
                canvas.drawText(mNoDataText, c.x, c.y, mInfoPaint);
            }

            if(!mOffsetsCalculated){
                calculateOffsets();
                mOffsetsCalculated = true;
            }
            return;
        }



    }


    public MPPointF getCenter() {
        return MPPointF.getInstance(getWidth() / 2f, getHeight() / 2f);
    }


    /**
     * ==========================highligtht begin===================================
     */


    /**
     * ==========================highligtht end===================================
     */



    /** ==========================marker begin===================================*/


    /**
     * ==========================marker end===================================
     */



    /** ==========================动画相关 begin===================================*/


    /**
     * ==========================动画闲相关 end===================================
     */


    //TODO 手势相关
//    public void setOnChartValueSelectedListener(OnChartValueSelectedListener l) {
//        this.mSelectionListener = l;
//    }
//
//    public void setOnChartGestureListener(OnChartGestureListener l) {
//        this.mGestureListener = l;
//    }
//
//    public OnChartGestureListener getOnChartGestureListener() {
//        return mGestureListener;
//    }


    public XAxis getXAxis() {
        return mXAxis;
    }

    public IValueFormatter getDefaultValueFormatter() {
        return mDefaultValueFormatter;
    }



    public float getYMax() {
        return mData.getYMax();
    }
    public float getYMin() {
        return mData.getYMin();
    }

    @Override
    public float getXChartMax() {
        return mXAxis.mAxisMaximum;
    }

    @Override
    public float getXChartMin() {
        return mXAxis.mAxisMinimum;
    }

    @Override
    public float getXRange() {
        return mXAxis.mAxisRange;
    }

    @Override
    public MPPointF getCenterOffsets() {
        return mViewPortHandler.getContentCenter();
    }


    public void setExtraOffsets(float left, float top, float right, float bottom) {
        setExtraLeftOffset(left);
        setExtraTopOffset(top);
        setExtraRightOffset(right);
        setExtraBottomOffset(bottom);
    }

    public void setExtraTopOffset(float offset) {
        mExtraTopOffset = Utils.convertDpToPixel(offset);
    }

    public float getExtraTopOffset() {
        return mExtraTopOffset;
    }

    public void setExtraRightOffset(float offset) {
        mExtraRightOffset = Utils.convertDpToPixel(offset);
    }

    public float getExtraRightOffset() {
        return mExtraRightOffset;
    }

    public void setExtraBottomOffset(float offset) {
        mExtraBottomOffset = Utils.convertDpToPixel(offset);
    }

    public float getExtraBottomOffset() {
        return mExtraBottomOffset;
    }

    public void setExtraLeftOffset(float offset) {
        mExtraLeftOffset = Utils.convertDpToPixel(offset);
    }

    public float getExtraLeftOffset() {
        return mExtraLeftOffset;
    }

    public void setLogEnabled(boolean enabled) {
        mLogEnabled = enabled;
    }

    public boolean isLogEnabled() {
        return mLogEnabled;
    }


    public void setNoDataText(String text) {
        mNoDataText = text;
    }

    public void setNoDataTextColor(int color) {
        mInfoPaint.setColor(color);
    }

    public void setNoDataTextTypeface(Typeface tf) {
        mInfoPaint.setTypeface(tf);
    }


    /**
     * 获取chartValue的可见区域
     * @return
     */
    @Override
    public RectF getContentRect() {
        return mViewPortHandler.getContentRect();
    }


    public void disableScroll() {
        ViewParent parent = getParent();
        if (parent != null)
            parent.requestDisallowInterceptTouchEvent(true);
    }


    public void enableScroll() {
        ViewParent parent = getParent();
        if (parent != null)
            parent.requestDisallowInterceptTouchEvent(false);
    }


    /**
     * 方便操作各个paint
     */
    public static final int PAINT_GRID_BACKGROUND = 4;
    public static final int PAINT_INFO = 7;
    public static final int PAINT_DESCRIPTION = 11;

    public void setPaint(Paint p, int which) {

        switch (which) {
            case PAINT_INFO:
                mInfoPaint = p;
                break;
            case PAINT_DESCRIPTION:
//                mDescPaint = p;
                break;
        }
    }

    public Paint getPaint(int which) {
        switch (which) {
            case PAINT_INFO:
                return mInfoPaint;
            case PAINT_DESCRIPTION:
//                return mDescPaint;
                return null;
        }

        return null;
    }


    public T getData() {
        return mData;
    }
    public ViewPortHandler getViewPortHandler() {
        return mViewPortHandler;
    }


    /**
     * TODO 绘制data相关
     *
     *
     public DataRenderer getRenderer() {
     return mRenderer;
     }

     public void setRenderer(DataRenderer renderer) {

     if (renderer != null)
     mRenderer = renderer;
     }

     *
     *
     */

    @Override
    public MPPointF getCenterOfView() {
        return getCenter();
    }


    /**
     * TODO 截图相关
     *
     * public Bitmap getChartBitmap()
     *
     * public boolean saveToPath(String title, String pathOnSD)
     *
     * public boolean saveToGallery
     */


    /**
     * 加载完视图之后，需要进行的task
     */

    protected ArrayList<Runnable> mJobs = new ArrayList<Runnable>();

    public void removeViewportJob(Runnable job) {
        mJobs.remove(job);
    }

    public void clearAllViewportJobs() {
        mJobs.clear();
    }


    /**
     * 添加一个job，如果chart已经绘制完就立刻执行，否则就加到执行队列中
     * @param job
     */
    public void addViewportJob(Runnable job) {

        if (mViewPortHandler.hasChartDimens()) {
            post(job);
        } else {
            mJobs.add(job);
        }
    }

    /**
     * 返回所有的需要在onsizeChange里执行的job
     * @return
     */
    public ArrayList<Runnable> getJobs() {
        return mJobs;
    }

    /**
     * TODO感觉不需要复写这个方法，后面可以注释掉试试
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(left, top, right, bottom);
        }
    }

//TODO super.onMeasure应该是和onMeasure方法中参数：widthMeasureSpec和heightMeasureSpec，重新计算好width和height后，应该是使用setMeasuredDimension（width,height）

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 最小就是50 * 50
         */
        int size = (int) Utils.convertDpToPixel(50f);
        setMeasuredDimension(
                Math.max(getSuggestedMinimumWidth(),
                        resolveSize(size,
                                widthMeasureSpec)),
                Math.max(getSuggestedMinimumHeight(),
                        resolveSize(size,
                                heightMeasureSpec)));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mLogEnabled)
            Log.i(LOG_TAG, "OnSizeChanged()");

        if (w > 0 && h > 0 && w < 10000 && h < 10000) {
            if (mLogEnabled)
                Log.i(LOG_TAG, "Setting chart dimens, width: " + w + ", height: " + h);
            mViewPortHandler.setChartDimens(w, h);
            Log.e(TAG,"在onSizeChanged初始化mViewPortHandler,并重新调用了notifyDataSetChanged");
        } else {
            if (mLogEnabled)
                Log.w(LOG_TAG, "*Avoiding* setting chart dimens! width: " + w + ", height: " + h);
        }

        // This may cause the chart view to mutate properties affecting the view port --
        //   lets do this before we try to run any pending jobs on the view port itself
        notifyDataSetChanged();

        for (Runnable r : mJobs) {
            post(r);
        }

        mJobs.clear();

        super.onSizeChanged(w, h, oldw, oldh);
    }


    /**
     * 设置硬件加速
     * @param enabled
     */
    public void setHardwareAccelerationEnabled(boolean enabled) {

        if (android.os.Build.VERSION.SDK_INT >= 11) {

            if (enabled)
                setLayerType(View.LAYER_TYPE_HARDWARE, null);
            else
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } else {
            Log.e(LOG_TAG,
                    "Cannot enable/disable hardware acceleration for devices below API level 11.");
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        //Log.i(LOG_TAG, "Detaching...");

        if (mUnbind)
            unbindDrawables(this);
    }

    private boolean mUnbind = false;

    /**
     * Unbind all drawables to avoid memory leaks.
     * Link: http://stackoverflow.com/a/6779164/1590502
     *
     * @param view
     */
    private void unbindDrawables(View view) {

        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    public void setUnbindEnabled(boolean enabled) {
        this.mUnbind = enabled;
    }


}

