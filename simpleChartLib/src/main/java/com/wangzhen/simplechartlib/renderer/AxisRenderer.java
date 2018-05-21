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
        //这里只判断y的缩放是因为XAxisRenderer会对这个方法进行覆写
        //如果y轴的缩放比例不是最小，就重新计算最大最小值
        if (mViewPortHandler != null && mViewPortHandler.contentWidth() > 10 && !mViewPortHandler.isFullyZoomedOutY()) {
            //y轴上获得当前显示top点和bottom点，然后获取这两个坐标点（px值）所代表的实际的value值
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
        //计算索要绘制的标签
        computeAxisValues(min, max);
    }

/**
 * 计算最大值和最小值之间所需的标签数量
 * */
    protected void computeAxisValues(float min,float max){

         float yMin = min;
         float yMax = max;

         int labelCount = mAxis.getLabelCount();
        //1.计算所要绘制的数据区间
         double range = Math.abs(yMax - yMin);

         if(labelCount == 0 || range <= 0 || Double.isInfinite(range)){
             mAxis.mEntries = new float[]{};
             mAxis.mCenteredEntries = new float[]{};
             mAxis.mEntryCount = 0;
             return;
         }
        //2.根据设置的显示label的个数和数值区间,初步获得间隔值
         double rawInterval = range/labelCount;
         //向下取整，间隔当然得是整数
         double interval = Utils.roundToNextSignificant(rawInterval);

        if (mAxis.isGranularityEnabled())
            interval = interval < mAxis.getGranularity() ? mAxis.getGranularity() : interval;
        //一个间隔值中内部的间隔值，比如groupbar这种情况
        double intervalMagnitude = Utils.roundToNextSignificant(Math.pow(10,(int)Math.log10(interval)));

        int intervalSigDigit = (int)(interval / intervalMagnitude);
        //TODO 暂时未看懂，不影响整体逻辑
        if (intervalSigDigit > 5) {
            // Use one order of magnitude higher, to avoid intervals like 0.9 or
            // 90
            interval = Math.floor(10 * intervalMagnitude);
        }

        //3.根据间隔值，起始值和终点值计算出具体需要好绘制的value，比如x轴的mEntries 包含的是 2，3，4 表示要绘制的是2，3，4的值
        int n = mAxis.isCenterAxisLabelsEnabled() ? 1 : 0;
        //TODO
//      if (mAxis.isForceLabelsEnabled()) {}

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
