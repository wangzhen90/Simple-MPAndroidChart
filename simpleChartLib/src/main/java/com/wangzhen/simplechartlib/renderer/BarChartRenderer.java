package com.wangzhen.simplechartlib.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.wangzhen.simplechartlib.buffer.BarBuffer;
import com.wangzhen.simplechartlib.data.chartData.BarData;
import com.wangzhen.simplechartlib.data.entry.BarEntry;
import com.wangzhen.simplechartlib.highlight.Highlight;
import com.wangzhen.simplechartlib.interfaces.charts.BarDataProvider;
import com.wangzhen.simplechartlib.interfaces.dataSets.IBarDataSet;
import com.wangzhen.simplechartlib.utils.Transformer;
import com.wangzhen.simplechartlib.utils.Utils;
import com.wangzhen.simplechartlib.utils.ViewPortHandler;

import java.util.List;

/**
 * Created by wangzhen on 2018/4/30.
 */

public class BarChartRenderer extends BarLineScatterCandleBubbleRenderer {

    protected BarDataProvider mChart;

    /**
     * 用于绘制bars的rect
     */
    protected RectF mBarRect = new RectF();

    protected BarBuffer[] mBarBuffers;


    public BarChartRenderer(ViewPortHandler viewPortHandler, BarDataProvider chart) {
        super(viewPortHandler);
        this.mChart = chart;

        //TODO highlight 暂不处理


    }

    @Override
    public void initBuffers() {

        BarData barData = mChart.getBarData();
        mBarBuffers = new BarBuffer[barData.getDataSetCount()];
        for (int i = 0; i < mBarBuffers.length; i++) {
            IBarDataSet set = barData.getDataSetByIndex(i);
            mBarBuffers[i] = new BarBuffer(set.getEntryCount() * 4 * (set.isStacked() ? set.getStackSize() : 1),
                    barData.getDataSetCount(), set.isStacked());
        }


    }


    @Override
    public void drawData(Canvas c) {

        BarData barData = mChart.getBarData();

        for (int i = 0; i < barData.getDataSetCount(); i++) {

            IBarDataSet set = barData.getDataSetByIndex(i);

            if (set.isVisible()) {
                drawDataSet(c, set, i);
            }
        }

    }

    protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {

        Transformer trans = mChart.getTransformer();
        //TODO 动画暂不处理
//        float phaseX = mAnimator.getPhaseX();
//        float phaseY = mAnimator.getPhaseY();

        float phaseX = 1f;
        float phaseY = 1f;

        BarBuffer buffer = mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setDataSet(index);
        buffer.setInverted(false);
        buffer.setBarWidth(mChart.getBarData().getBarWidth());

        buffer.feed(dataSet);

        trans.pointValuesToPixel(buffer.buffer);

        final boolean isSingleColor = dataSet.getColors().size() == 1;

        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }

        for (int j = 0; j < buffer.size(); j += 4) {
            //如果这个bar的right 小于 content的left就不绘制
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;
            //如果这个bar的left大于content的right就不绘制
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;

            if (!isSingleColor) {
                mRenderPaint.setColor(dataSet.getColor(j / 4));
            }

            c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mRenderPaint);

        }

    }


    @Override
    public void drawValues(Canvas c) {

        if (isDrawingValuesAllowed(mChart)) {
            List<IBarDataSet> datasets = mChart.getBarData().getDataSets();

            final float valueOffsetPlus = Utils.convertDpToPixel(4.5f);
            float posOffset = 0f;
            float negOffset = 0f;
            //TODO
//            boolean drawValueAboveBar = mChart.isDrawValueAboveBarEnabled();
            boolean drawValueAboveBar = true;

            for (int i = 0; i < mChart.getBarData().getDataSetCount(); i++) {

                IBarDataSet dataSet = datasets.get(i);

                if (!shouldDrawValues(dataSet)) {
                    continue;
                }

//                mValuePaint.setTextSize(dataSet.getValueTextSize());

//                boolean isInverted = mChart.isInverted(dataSet.getAxisDependency());
                boolean isInverted = false;
                float valueTextHeight = Utils.calcTextHeight(mValuePaint, "8");
                //TODO 怎么感觉写反了？
                posOffset = (drawValueAboveBar ? -valueOffsetPlus : valueTextHeight + valueOffsetPlus);
                negOffset = (drawValueAboveBar ? valueTextHeight + valueOffsetPlus : -valueOffsetPlus);

                if (isInverted) {
                    posOffset = -posOffset - valueTextHeight;
                    negOffset = -negOffset - valueTextHeight;
                }
                //获取buffer
                BarBuffer buffer = mBarBuffers[i];
                //TODO 动画相关，暂不考虑
//                final float phaseY = mAnimator.getPhaseY();
                final float phaseY = 1;


                if (!dataSet.isStacked()) {
                    // TODO 动画相关，暂不考虑
//                    for (int j = 0; j < buffer.buffer.length * mAnimator.getPhaseX(); j += 4) {
                    for (int j = 0; j < buffer.buffer.length; j += 4) {
                        //x的位置在bar的中心
                        float x = (buffer.buffer[j] + buffer.buffer[j + 2]) / 2f;
                        //从左向右绘制，如果某个value的x超过了content的right，直接跳出循环
                        if (!mViewPortHandler.isInBoundsRight(x))
                            break;

                        if (!mViewPortHandler.isInBoundsY(buffer.buffer[j + 1])
                                || !mViewPortHandler.isInBoundsLeft(x))
                            continue;

                        BarEntry entry = dataSet.getEntryForIndex(j / 4);

                        float val = entry.getY();

                        if (dataSet.isDrawValuesEnabled()) {

                            drawValue(c, dataSet.getValueFormatter(), val, entry, i, x,
                                    val >= 0 ? (buffer.buffer[j + 1] + posOffset) : (buffer.buffer[j + 3] + negOffset),
                                    dataSet.getValueTextColor()
                            );
                        }


                    }
                } else {
                    //TODO 堆叠图暂不考虑
                }


            }

        }


    }

    @Override
    public void drawExtras(Canvas c) {

    }


    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        BarData barData = mChart.getBarData();

        for (Highlight high : indices) {

            IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());
            if (set == null || !set.isHighlightEnabled()) {
                continue;
            }

            BarEntry e = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(e, set)) {
                continue;
            }

            Transformer trans = mChart.getTransformer();
            mHighlightPaint.setColor(set.getHighLightColor());
            mHighlightPaint.setAlpha(set.getHighLightAlpha());

            boolean isStack = (high.getStackIndex() >= 0 && e.isStacked()) ? true : false;

            final float y1, y2;

            if (isStack) {
                //TODO 堆叠图highlight
                y1 = e.getY();
                y2 = 0;
            } else {
                y1 = e.getY();
                y2 = 0;
            }

            prepareBarHighlight(e.getX(),y1,y2,barData.getBarWidth()/2f,trans);
            setHighlightDrawPos(high,mBarRect);

            c.drawRect(mBarRect,mHighlightPaint);

        }

    }

    protected void prepareBarHighlight(float x, float y1, float y2, float barWidthHalf, Transformer trans) {

        float left = x - barWidthHalf;
        float right = x + barWidthHalf;
        float top = y1;
        float bottom = y2;

        mBarRect.set(left,top,right,bottom);

        //TODO 动画
        trans.rectToPixelPhase(mBarRect,1);

    }

    protected void setHighlightDrawPos(Highlight high,RectF bar){

        high.setDraw(bar.centerX(),bar.top);
    }


}
