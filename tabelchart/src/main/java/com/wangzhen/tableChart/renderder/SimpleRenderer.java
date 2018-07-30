package com.wangzhen.tableChart.renderder;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.Log;

import com.wangzhen.tableChart.buffer.ColumnBuffer;
import com.wangzhen.tableChart.buffer.TitleBuffer;
import com.wangzhen.tableChart.component.TableChart;
import com.wangzhen.tableChart.data.Column;
import com.wangzhen.tableChart.highlight.Highlight;
import com.wangzhen.tableChart.interfaces.ICell;
import com.wangzhen.tableChart.utils.Transformer;
import com.wangzhen.tableChart.utils.Utils;
import com.wangzhen.tableChart.utils.ViewPortHandler;

import java.util.List;

/**
 * Created by wangzhen on 2018/7/7.
 */

public class SimpleRenderer extends DataRenderer {


    private TableChart mChart;

    private TitleBuffer mTitleBuffer;
    private Transformer transformer;

    private ColumnBuffer[] mBuffers;

//    private RectF checkCanDrawBuffer;

    public SimpleRenderer(ViewPortHandler viewPortHandler, TableChart chart) {
        super(viewPortHandler);
        this.mChart = chart;
        mHighlightPaint.setStrokeWidth(mChart.getHighlightBorderWidth());
    }

    @Override
    public void initBuffers() {

        List<Column<ICell>> columns = mChart.getColumnList();
        mBuffers = new ColumnBuffer[mChart.getColumnCount()];
        mTitleBuffer = new TitleBuffer(mChart.getColumnCount() * 4, mChart.getColumnCount());

//        mTitleBuffer.feed(mChart.getColumnList());
        for (int i = 0; i < mChart.getColumnCount(); i++) {
            mBuffers[i] = new ColumnBuffer(columns.get(i).getData().size() * 4);
//            mBuffers[i].feed(columns.get(i));
        }
    }

    private RectF mValuesRect = new RectF();

    @Override
    public void drawData(Canvas c) {
        List<Column<ICell>> columns = mChart.getColumnList();


        mValuesRect.set(mViewPortHandler.getContentRect());
        mValuesRect.top += mChart.getTitleHeight() * mViewPortHandler.getScaleY();
        int clipRestoreCount = c.save();
        c.clipRect(mValuesRect);
//        c.translate(0, mChart.getTitleHeight() * mViewPortHandler.getScaleY());
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < mChart.getColumnCount(); i++) {
            drawColumn(c, columns.get(i), i, mValuesRect, columns);
        }

//        Log.e("1------绘制所有column的耗费时间：", (System.currentTimeMillis() - startTime) + "");

        c.restoreToCount(clipRestoreCount);

    }

//    float left, right;

    //    float[] checkBuffer = new float[]{0,0,0,0};
    RectF checkRect = new RectF();
    private String bgColorBuffer;

    private void drawColumn(Canvas c, Column<ICell> column, int index, RectF visibleRect, List<Column<ICell>> columns) {

        if (transformer == null) {
            transformer = mChart.getTransformer();
        }
        if (transformer == null) return;

        ColumnBuffer columnBuffer = mBuffers[index];

        if (columnBuffer.size() >= 4) {
//            checkBuffer[0] = column.getPreColumnsWidth();
//            checkBuffer[3] = column.getPreColumnsWidth() + column.getWidth();

            checkRect.set(column.getPreColumnsWidth(), 0, column.getPreColumnsWidth() + column.getWidth(), 0);

            if (index < 1) {
//                Log.e("3=======:start","checkBuffer left +"+index+"+:"+checkBuffer[0] + ",right:"+checkBuffer[3]);
//                Log.e("3=======:start","checkRect left +"+index+"+:"+checkRect.left + ",right:"+checkRect.right);
            }
            transformer.rectValueToPixel(checkRect);
//            transformer.pointValuesToPixel(checkBuffer);
            if (index < 1) {
//                Log.e("3=======:","checkBuffer left +"+index+"+:"+checkBuffer[0] + ",right:"+checkBuffer[3]);
//                Log.e("3=======:","checkRect left +"+index+"+:"+checkRect.left + ",right:"+checkRect.right);
//                Log.e("3=======:","visibleRect left+"+index+"+:"+visibleRect.left + ",right:"+visibleRect.right);
            }


//            if ((checkBuffer[0] - 10> visibleRect.right) || (checkBuffer[3] + 10 < visibleRect.left)) {
            if ((checkRect.left - 10 > visibleRect.right) || (checkRect.right + 10 < visibleRect.left)) {
                return;
            }
        }


        long startTime = System.currentTimeMillis();

        if (mChart.hasMergedCell()) {
            columnBuffer.feed(column, columns);
        } else {
            columnBuffer.feed(column);
        }
        Log.e("2------", "column" + index + "的feed耗费时间：" + (System.currentTimeMillis() - startTime) + "");

        long startTimeTrans = System.currentTimeMillis();
        transformer.pointValuesToPixel(columnBuffer.buffer);

//        Log.e("2------", "column" + index + "的trans耗费时间：" + (System.currentTimeMillis() - startTimeTrans) + "");

        long startTimeDraw = System.currentTimeMillis();

        for (int i = 0; i < columnBuffer.size(); i += 4) {

            if (columnBuffer.buffer[i + 2] == columnBuffer.buffer[i]) continue;

            if ((columnBuffer.buffer[i] > mViewPortHandler.contentRight()) || (columnBuffer.buffer[i + 2] < mViewPortHandler.contentLeft())) {
                return;
            }

            if (columnBuffer.buffer[i + 3] < (visibleRect.top - (columnBuffer.buffer[i + 3] - columnBuffer.buffer[i + 1])) || columnBuffer.buffer[i + 1] > visibleRect.bottom) {
                continue;
            }

            c.drawRect(columnBuffer.buffer[i], columnBuffer.buffer[i + 1], columnBuffer.buffer[i + 2],
                    columnBuffer.buffer[i + 3], mGridPaint);

            if(mChart.getBgFormatter() != null){
                bgColorBuffer = mChart.getBgFormatter().getBackgroundColor(column.getData().get(i / 4).getRealCell(), column, columns);

                if(bgColorBuffer != null){
                    mBgPaint.setColor(Color.parseColor(bgColorBuffer));
                    c.drawRect(columnBuffer.buffer[i], columnBuffer.buffer[i + 1], columnBuffer.buffer[i + 2],
                            columnBuffer.buffer[i + 3], mBgPaint);
                }
            }


            fillValuePaint(column.getData().get(i / 4).getRealCell(), column, columns);

            Utils.drawSingleText(c, mValuePaint,
                     Utils.getTextCenterX(
                             columnBuffer.buffer[i] + column.getLeftOffset() * mChart.getViewPortHandler().getScaleX(),
                             columnBuffer.buffer[i + 2] - column.getRightOffset() * mChart.getViewPortHandler().getScaleX(),
//                             columnBuffer.buffer[i],
//                             columnBuffer.buffer[i + 2],
                             mValuePaint),
                    Utils.getTextCenterY((columnBuffer.buffer[i + 1] + columnBuffer.buffer[i + 3]) / 2, mValuePaint),
                    column.getData().get(i / 4).getContents()
            );
        }

//        Log.e("2------", "column" + index + "的draw耗费时间：" + (System.currentTimeMillis() - startTimeDraw) + "");

    }

    TextPaint.Align mValueTextAlignBuffer;

    private void fillValuePaint(ICell cell, Column<ICell> column, List<Column<ICell>> columns) {

        mValueTextAlignBuffer = mChart.getSheet().getTextFormatter().getTextAlign(cell, column, columns);

        mValuePaint.setTextSize(Utils.convertDpToPixel(mChart.getSheet().getTextFormatter().getTextSize(cell, column, columns) * mViewPortHandler.getScaleX()));
        mValuePaint.setTextAlign(mValueTextAlignBuffer);
    }


    @Override
    public void drawValues(Canvas c) {

    }

    @Override
    public void drawTitle(Canvas c) {
        transformer = mChart.getTransformer();
        if (transformer == null) return;
        //优化onDraw执行时间，只在initBuffer中做一次feed
        mTitleBuffer.feed(mChart.getColumnList());
        transformer.pointValuesToPixel(mTitleBuffer.buffer);

        for (int i = 0; i < mTitleBuffer.size(); i += 4) {

            float left = mTitleBuffer.buffer[i];
            float top = mTitleBuffer.buffer[i + 1];
            float right = mTitleBuffer.buffer[i + 2];
            float bottom = mTitleBuffer.buffer[i + 3];
            float height = bottom - top;

            if ((left > mViewPortHandler.contentRight()) || (right < mViewPortHandler.contentLeft())) {
                continue;
            }


            if (mChart.isTitleFixed()) {
                if (top < 0) {
                    top = 0;
                    bottom = height;
                }

            }


            c.drawRect(left, top, right, bottom, mGridPaint);
            mTitleValuePaint.setTextSize(Utils.convertDpToPixel(mChart.getContentFontSize() * mViewPortHandler.getScaleX()));
            Utils.drawSingleText(c, mTitleValuePaint,
                    Utils.getTextCenterX(left, right, mValuePaint),
                    Utils.getTextCenterY((top + bottom) / 2, mValuePaint),
                    mTitleBuffer.columnNames[i / 4]);


        }


    }

    @Override
    public void drawExtras(Canvas c) {

    }

    @Override
    public void drawHighlighted(Canvas c, Highlight highlight) {

        if (highlight != null) {
            transformer = mChart.getTransformer();
            if (transformer == null) return;
            RectF hRect = highlight.getRect();
            int clipRestoreCount = c.save();
            if (highlight.isTitle() && mChart.isTitleFixed()) {
                c.clipRect(mChart.getViewPortHandler().getContentRect());
            } else {
                mValuesRect.set(mViewPortHandler.getContentRect());
                mValuesRect.top += mChart.getTitleHeight() * mViewPortHandler.getScaleY();
                c.clipRect(mValuesRect);
            }

            float halfBorderWidth = mChart.getHighlightBorderWidth() / 2;

            transformer.rectValueToPixel(hRect);
            c.drawRect(hRect.left + halfBorderWidth,
                    hRect.top + halfBorderWidth,
                    hRect.right - halfBorderWidth,
                    hRect.bottom - halfBorderWidth,
                    mHighlightPaint);
            c.restoreToCount(clipRestoreCount);
        }
    }
}
