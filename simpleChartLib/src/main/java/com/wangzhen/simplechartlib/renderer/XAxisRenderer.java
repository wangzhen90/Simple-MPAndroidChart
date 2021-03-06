package com.wangzhen.simplechartlib.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;

import com.wangzhen.simplechartlib.component.AxisBase;
import com.wangzhen.simplechartlib.component.XAxis;
import com.wangzhen.simplechartlib.utils.FSize;
import com.wangzhen.simplechartlib.utils.MPPointD;
import com.wangzhen.simplechartlib.utils.MPPointF;
import com.wangzhen.simplechartlib.utils.Transformer;
import com.wangzhen.simplechartlib.utils.Utils;
import com.wangzhen.simplechartlib.utils.ViewPortHandler;

/**
 * Created by wangzhen on 2018/3/20.
 */

public class XAxisRenderer extends AxisRenderer {

    protected XAxis mXAxis;


    public XAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer transformer) {
        super(viewPortHandler,xAxis,transformer);

        this.mXAxis = xAxis;
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
    @Override
    public void computeAxis(float min, float max, boolean invert) {
        /**
         * 如果chart没有被缩小到最小，获取chart缩放后最大最小值得对应的实际的值
         * 使用的场景是：chart进行了放大之后，可见的最大最小值改变了，这里就需要给最大最小值重新赋值，然后x轴，data等会重新进行计算和绘制，这种情况还包括放大之后的拖拽场景
         */
        if (mViewPortHandler != null && mViewPortHandler.contentWidth() > 10 && !mViewPortHandler.isFullyZoomedOutX()) {

            MPPointD p1 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop());
            MPPointD p2 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentRight(), mViewPortHandler.contentTop());

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

        Log.e("compute:","min:"+min+",max"+max);

        Log.e("BarChart","1.2 notifyDataSetChanged/onDraw(数据未变，进行了手势操作) :根据传入的最大最小值以及设置的labelCount，计算X轴labels的position和size");

    }

    @Override
    protected void computeAxisValues(float min, float max) {
        super.computeAxisValues(min, max);

        computeSize();
    }

    protected void computeSize() {

        String longest = mXAxis.getLongestLabel();

//        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());

        final FSize labelSize = Utils.calcTextSize(mAxisLabelPaint, longest);


        final float labelWidth = labelSize.width;
        final float labelHeight = Utils.calcTextHeight(mAxisLabelPaint, "Q");



        final FSize labelRotatedSize = Utils.getSizeOfRotatedRectangleByDegrees(
                labelWidth,
                labelHeight,
                mXAxis.getLabelRotationAngle());


        mXAxis.mLabelWidth = Math.round(labelWidth);
        mXAxis.mLabelHeight = Math.round(labelHeight);
        mXAxis.mLabelRotatedWidth = Math.round(labelRotatedSize.width);
        mXAxis.mLabelRotatedHeight = Math.round(labelRotatedSize.height);

        FSize.recycleInstance(labelRotatedSize);
        FSize.recycleInstance(labelSize);
    }

    @Override
    public void renderAxisLine(Canvas c) {
        if (!mXAxis.isDrawAxisLineEnabled() || !mXAxis.isEnabled())
            return;
        mAxisLinePaint.setColor(mXAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mXAxis.getAxisLineWidth());
//        mAxisLinePaint.setPathEffect(mXAxis.getAxisLineDashPathEffect());

        c.drawLine(mViewPortHandler.contentLeft(),
                mViewPortHandler.contentBottom(), mViewPortHandler.contentRight(),
                mViewPortHandler.contentBottom(), mAxisLinePaint);
        Log.e("BarChart","2.3 绘制x轴水平线");

    }

    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled())
            return;

        float yoffset = mXAxis.getYOffset();
//        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());
        MPPointF pointF = MPPointF.getInstance(0,0);

        pointF.x = 0.5f;
        pointF.y = 0.0f;

        drawLabels(c, mViewPortHandler.contentBottom() + yoffset, pointF);
        MPPointF.recycleInstance(pointF);
        Log.e("BarChart","2.5 绘制x轴labels");

    }


    public void drawLabels(Canvas c, float pos, MPPointF anchor){

        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();

        float[] positions = new float[mXAxis.mEntryCount *2];

        for(int i = 0; i < positions.length; i+=2){
            if (centeringEnabled) {
                positions[i] = mXAxis.mCenteredEntries[i / 2];
            } else {
                positions[i] = mXAxis.mEntries[i / 2];
            }
        }
        //value => px
        mTrans.pointValuesToPixel(positions);

        for(int i =0; i < positions.length; i+=2){

            float x = positions[i];
            String label = mXAxis.getValueFormatter().getFormattedValue(mXAxis.mEntries[i / 2], mXAxis);

            if (mXAxis.isAvoidFirstLastClippingEnabled()) {

                // avoid clipping of the last
                if (i == mXAxis.mEntryCount - 1 && mXAxis.mEntryCount > 1) {
                    float width = Utils.calcTextWidth(mAxisLabelPaint, label);

                    if (width > mViewPortHandler.offsetRight() * 2
                            && x + width > mViewPortHandler.getChartWidth())
                        x -= width / 2;

                    // avoid clipping of the first
                } else if (i == 0) {
                    float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                    x += width / 2;
                }
            }

            drawLabel(c, label, x, pos, anchor, labelRotationAngleDegrees);
        }
    }

    protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
        Utils.drawXAxisValue(c, formattedLabel, x, y, mAxisLabelPaint, anchor, angleDegrees);
    }
    protected float[] mRenderGridLinesBuffer = new float[2];
    protected Path mRenderGridLinesPath = new Path();
    @Override
    public void renderGridLines(Canvas c) {
        Log.e("BarChart","2.4 绘制x轴栅格线");

        if(!mXAxis.isEnabled() || !mXAxis.isDrawGridLinesEnabled()){
            return;
        }

        int clipRestoreCount = c.save();
        c.clipRect(getGridClippingRect());

        if(mRenderGridLinesBuffer.length != mAxis.mEntryCount * 2){
            mRenderGridLinesBuffer = new float[mXAxis.mEntryCount * 2];
        }
        float[] positions = mRenderGridLinesBuffer;

        for (int i = 0; i < positions.length; i += 2) {
            positions[i] = mXAxis.mEntries[i / 2];
            positions[i + 1] = mXAxis.mEntries[i / 2];
        }

        mTrans.pointValuesToPixel(positions);
        mGridPaint.setColor(mXAxis.getGridColor());
        mGridPaint.setStrokeWidth(mXAxis.getGridLineWidth());


        Path gridLinePath = mRenderGridLinesPath;
        gridLinePath.reset();

        for (int i = 0; i < positions.length; i += 2) {

            gridLinePath.moveTo(positions[i], mViewPortHandler.contentBottom());
            gridLinePath.lineTo(positions[i], mViewPortHandler.contentTop());
            c.drawPath(gridLinePath, mGridPaint);
            gridLinePath.reset();
        }


        c.restoreToCount(clipRestoreCount);

    }

    protected RectF mGridClippingRect = new RectF();

    public RectF getGridClippingRect() {
        mGridClippingRect.set(mViewPortHandler.getContentRect());
        mGridClippingRect.inset(-mAxis.getGridLineWidth(), 0.f);
        return mGridClippingRect;
    }



    @Override
    public void renderLimitLines(Canvas c) {

    }


}
