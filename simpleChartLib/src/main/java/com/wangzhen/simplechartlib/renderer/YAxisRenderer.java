package com.wangzhen.simplechartlib.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.wangzhen.simplechartlib.component.AxisBase;
import com.wangzhen.simplechartlib.component.YAxis;
import com.wangzhen.simplechartlib.utils.Transformer;
import com.wangzhen.simplechartlib.utils.Utils;
import com.wangzhen.simplechartlib.utils.ViewPortHandler;

/**
 * Created by wangzhen on 2018/4/17.
 */

public class YAxisRenderer extends AxisRenderer {

    protected YAxis mYAxis;

    protected Paint mZeroLinePaint;

    public YAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer transformer) {
        super(viewPortHandler, yAxis, transformer);

        this.mYAxis = yAxis;

        if (mViewPortHandler != null) {
            mAxisLabelPaint.setColor(Color.BLACK);
            mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10f));

            mZeroLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mZeroLinePaint.setColor(Color.GRAY);
            mZeroLinePaint.setStrokeWidth(1f);
            mZeroLinePaint.setStyle(Paint.Style.STROKE);
        }

    }

    protected float[] mGetTransformedPositionsBuffer = new float[2];

    protected float[] getTransformedPositions() {

        if (mGetTransformedPositionsBuffer.length != mYAxis.mEntryCount * 2) {
            mGetTransformedPositionsBuffer = new float[mYAxis.mEntryCount * 2];
        }

        float[] positions = mGetTransformedPositionsBuffer;

        for (int i = 0; i < positions.length; i += 2) {
            positions[i + 1] = mYAxis.mEntries[i / 2];
        }

        //映射
        mTrans.pointValuesToPixel(positions);

        return positions;
    }

    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        //不绘制第一个
        final int from = 0;
        //不绘制最后一个
        final int to = mYAxis.mEntryCount;

        for(int i = from; i < to; i++){

            String text = mYAxis.getFormattedLabel(i);
            c.drawText(text,fixedPosition,positions[i*2+1] + offset,mAxisLabelPaint);
        }

    }



    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mYAxis.isEnabled() || !mYAxis.isDrawLabelsEnabled())
            return;
        //1.获取position

        float[] positions = getTransformedPositions();

        //2.设置paint属性
        mAxisLabelPaint.setTextSize(mYAxis.getTextSize());
        mAxisLabelPaint.setColor(mYAxis.getTextColor());

        //3.获取xOffset yOffset，计算出xPos

        float xoffset = mYAxis.getXOffset();
        float yoffset = mYAxis.getYOffset() + Utils.calcTextHeight(mAxisLabelPaint, "A") / 2.5f;

        float xPos = 0f;

        mAxisLabelPaint.setTextAlign(Paint.Align.RIGHT);

        xPos =  mViewPortHandler.offsetLeft() - xoffset;

        //4.绘制labels

        drawYLabels(c,xPos,positions,yoffset);
    }
    protected Path mRenderGridLinesPath = new Path();
    @Override
    public void renderGridLines(Canvas c) {
        if(!mYAxis.isEnabled()){
            return;
        }

        if(mYAxis.isDrawGridLinesEnabled()){

            int clipRestoreCount = c.save();
            c.clipRect(getGridClippingReact());

            float[] positions = getTransformedPositions();
            mGridPaint.setColor(mYAxis.getGridColor());
            mGridPaint.setStrokeWidth(mYAxis.getGridLineWidth());
//            mGridPaint.setPathEffect(mYAxis.getGridDashPathEffect());

            Path gridLinePath = mRenderGridLinesPath;
            gridLinePath.reset();

            for(int i = 0; i < positions.length; i+=2){
                gridLinePath.moveTo(mViewPortHandler.offsetLeft(),positions[i+1]);
                gridLinePath.lineTo(mViewPortHandler.contentRight(),positions[i+1]);
                c.drawPath(gridLinePath,mGridPaint);
                gridLinePath.reset();
            }

            c.restoreToCount(clipRestoreCount);
        }
    }

    protected RectF mGridClippingRect = new RectF();
    public RectF getGridClippingReact(){
        mGridClippingRect.set(mViewPortHandler.getContentRect());
        mGridClippingRect.inset(0.f, -mAxis.getGridLineWidth());
        return mGridClippingRect;
    }


    @Override
    public void renderAxisLine(Canvas c) {
        if (!mYAxis.isEnabled() || !mYAxis.isDrawAxisLineEnabled()) {
            return;
        }
        c.drawLine(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop(), mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom(), mAxisLinePaint);

    }

    @Override
    public void renderLimitLines(Canvas c) {

    }
}
