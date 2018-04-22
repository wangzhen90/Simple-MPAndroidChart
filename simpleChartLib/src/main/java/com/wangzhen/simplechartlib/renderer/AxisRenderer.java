package com.wangzhen.simplechartlib.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.wangzhen.simplechartlib.component.AxisBase;
import com.wangzhen.simplechartlib.utils.MPPointD;
import com.wangzhen.simplechartlib.utils.Transformer;
import com.wangzhen.simplechartlib.utils.Utils;
import com.wangzhen.simplechartlib.utils.ViewPortHandler;

/**
 * Created by wangzhen on 2018/3/20.
 */

public abstract class AxisRenderer extends Renderer {

    protected AxisBase mAxis;
    protected Transformer mTrans;
    protected Paint mGridPaint;
    protected Paint mAxisLabelPaint;
    protected Paint mAxisLinePaint;


    public AxisRenderer(ViewPortHandler viewPortHandler, AxisBase xAxis, Transformer transformer) {
        super(viewPortHandler);

        this.mAxis = xAxis;
        this.mTrans = transformer;

        if (mViewPortHandler != null) {

            mAxisLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            mGridPaint = new Paint();
            mGridPaint.setColor(Color.GRAY);
            mGridPaint.setStrokeWidth(1f);
            mGridPaint.setStyle(Paint.Style.STROKE);
            mGridPaint.setAlpha(90);

            mAxisLinePaint = new Paint();
            mAxisLinePaint.setColor(Color.BLACK);
            mAxisLinePaint.setStrokeWidth(1f);
            mAxisLinePaint.setStyle(Paint.Style.STROKE);

        }
    }

    public Paint getPaintAxisLabels() {
        return mAxisLabelPaint;
    }

    public Paint getPaintGrid() {
        return mGridPaint;
    }

    public Paint getPaintAxisLine() {
        return mAxisLinePaint;
    }

    public Transformer getTransformer() {
        return mTrans;
    }

    /**
     * 计算坐标轴的值
     * @param min
     * @param max
     * @param invert
     */
    public void computeAxis(float min, float max, boolean invert) {
        /**
         * TODO 后面的两个条件不知为何，为啥只判断y的缩放
         */
        if (mViewPortHandler != null && mViewPortHandler.contentWidth() > 10 && !mViewPortHandler.isFullyZoomedOutY()) {
            //TODO 好好看一下根据point 寻找value，y是否翻转了？
            MPPointD p1 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop());
            MPPointD p2 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom());

            if(!invert){
                min = (float)p2.y;
                max = (float)p1.y;
            }else{
                min = (float)p1.y;
                max = (float)p2.y;
            }

            MPPointD.recycleInstance(p1);
            MPPointD.recycleInstance(p2);
        }
        computeAxisValues(min, max);
    }

/**
 * 计算最大值和最小值之间所需的标签数量
 * */
    protected void computeAxisValues(float min,float max){

         float yMin = min;
         float yMax = max;

         int labelCount = mAxis.getLabelCount();

         double range = Math.abs(yMax - yMin);

         if(labelCount == 0 || range <= 0 || Double.isInfinite(range)){
             mAxis.mEntries = new float[]{};
             mAxis.mCenteredEntries = new float[]{};
             mAxis.mEntryCount = 0;
             return;
         }

         double rawInterval = range/labelCount;
         double interval = Utils.roundToNextSignificant(rawInterval);

        if (mAxis.isGranularityEnabled())
            interval = interval < mAxis.getGranularity() ? mAxis.getGranularity() : interval;

        double intervalMagnitude = Utils.roundToNextSignificant(Math.pow(10,(int)Math.log10(interval)));

        int intervalSigDigit = (int)(interval / intervalMagnitude);
        //TODO 未看懂这个判断
        if (intervalSigDigit > 5) {
            // Use one order of magnitude higher, to avoid intervals like 0.9 or
            // 90
            interval = Math.floor(10 * intervalMagnitude);
        }

        int n = mAxis.isCenterAxisLabelsEnabled() ? 1 : 0;
        //TODO 这个判断暂时没有写
//                if (mAxis.isForceLabelsEnabled()) {}
//
        double first = interval == 0.0 ? 0.0 : Math.ceil(yMin / interval) * interval;
        if(mAxis.isCenterAxisLabelsEnabled()) {
            first -= interval;
        }

        double last = interval == 0.0 ? 0.0 : Utils.nextUp(Math.floor(yMax / interval) * interval);

        double f;
        int i;

        if (interval != 0.0) {
            for (f = first; f <= last; f += interval) {
                ++n;
            }
        }

        mAxis.mEntryCount = n;

        if (mAxis.mEntries.length < n) {
            // Ensure stops contains at least numStops elements.
            mAxis.mEntries = new float[n];
        }

        for(f = first, i = 0; i < n; f += interval, ++i) {

            if (f == 0.0) // Fix for negative zero case (Where value == -0.0, and 0.0 == -0.0)
                f = 0.0;

            mAxis.mEntries[i] = (float) f;
        }

        if (interval < 1) {
            //-Math.log10(interval) =》 0.001 = 100
            mAxis.mDecimals = (int) Math.ceil(-Math.log10(interval));
        } else {
            mAxis.mDecimals = 0;
        }

        if (mAxis.isCenterAxisLabelsEnabled()) {

            if (mAxis.mCenteredEntries.length < n) {
                mAxis.mCenteredEntries = new float[n];
            }

            float offset = (float)interval / 2f;

            for (int k = 0; k < n; k++) {
                //位于每一个区间的中间
                mAxis.mCenteredEntries[k] = mAxis.mEntries[k] + offset;
            }
        }

    }

    //绘制文本
    public abstract void renderAxisLabels(Canvas c);
    //绘制分组线
    public abstract void renderGridLines(Canvas c);
    //绘制坐标轴线
    public abstract void renderAxisLine(Canvas c);
    //绘制限制线
    public abstract void renderLimitLines(Canvas c);
}
