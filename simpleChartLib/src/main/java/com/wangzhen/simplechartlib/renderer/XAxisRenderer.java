package com.wangzhen.simplechartlib.renderer;

import android.graphics.Color;
import android.graphics.Paint;

import com.wangzhen.simplechartlib.component.AxisBase;
import com.wangzhen.simplechartlib.component.XAxis;
import com.wangzhen.simplechartlib.utils.MPPointD;
import com.wangzhen.simplechartlib.utils.Transformer;
import com.wangzhen.simplechartlib.utils.ViewPortHandler;

/**
 * Created by wangzhen on 2018/3/20.
 */

public class XAxisRenderer extends Renderer {

    protected AxisBase mAxis;
    protected Transformer mTrans;
    protected Paint mGridPaint;
    protected Paint mAxisLabelPaint;
    protected Paint mAxisLinePaint;


    public XAxisRenderer(ViewPortHandler viewPortHandler, AxisBase xAxis, Transformer transformer) {
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

    public void computeAxis(float min, float max, boolean invert) {
        /**
         * TODO 后面的两个条件不知为何，为啥只判断y的缩放
         */
        if (mViewPortHandler != null && mViewPortHandler.contentWidth() > 10 && !mViewPortHandler.isFullyZoomedOutY()) {

            MPPointD p1 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentTop());
            MPPointD p2 = mTrans.getValuesByTouchPoint(mViewPortHandler.contentLeft(), mViewPortHandler.contentBottom());
        }


    }

}
