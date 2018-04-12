package com.wangzhen.simplechartlib.renderer;

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

public class AxisRenderer extends Renderer {

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

            MPPointD p1 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop());
            MPPointD p2 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom());

            if(invert){
                min = (float)p2.x;
                max = (float)p1.x;
            }else{
                min = (float)p1.x;
                max = (float)p2.x;
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

//        if (mAxis.isGranularityEnabled())
//            interval = interval < mAxis.getGranularity() ? mAxis.getGranularity() : interval;

        double intervalMagnitude = Utils.roundToNextSignificant(Math.pow(10,(int)Math.log10(interval)));




    }

}
