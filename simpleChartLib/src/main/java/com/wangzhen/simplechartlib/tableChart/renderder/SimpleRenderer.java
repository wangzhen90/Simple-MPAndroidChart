package com.wangzhen.simplechartlib.tableChart.renderder;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.wangzhen.simplechartlib.highlight.Highlight;
import com.wangzhen.simplechartlib.tableChart.buffer.ColumnBuffer;
import com.wangzhen.simplechartlib.tableChart.buffer.TitleBuffer;
import com.wangzhen.simplechartlib.tableChart.component.TableChart;
import com.wangzhen.simplechartlib.tableChart.data.Cell;
import com.wangzhen.simplechartlib.tableChart.data.Column;
import com.wangzhen.simplechartlib.utils.Transformer;
import com.wangzhen.simplechartlib.utils.Utils;
import com.wangzhen.simplechartlib.utils.ViewPortHandler;

import java.util.List;

/**
 * Created by wangzhen on 2018/7/7.
 */

public class SimpleRenderer extends DataRenderer {


    private TableChart mChart;

    private TitleBuffer mTitleBuffer;
    private Transformer transformer;

    private ColumnBuffer[] mBuffers;

    public SimpleRenderer(ViewPortHandler viewPortHandler, TableChart chart) {
        super(viewPortHandler);
        this.mChart = chart;

    }

    @Override
    public void initBuffers() {

        List<Column> columns = mChart.getColumnList();
        mBuffers = new ColumnBuffer[mChart.getColumnCount()];
        mTitleBuffer = new TitleBuffer(mChart.getColumnCount() * 4, mChart.getColumnCount());

        for (int i = 0; i < mChart.getColumnCount(); i++) {
            mBuffers[i] = new ColumnBuffer(columns.get(i).getData().size() * 4);
        }


    }
    private RectF mValuesRect = new RectF();

    @Override
    public void drawData(Canvas c) {
        List<Column> columns = mChart.getColumnList();


        mValuesRect.set(mViewPortHandler.getContentRect());

        mValuesRect.top += mChart.getTitleHeight() * mViewPortHandler.getScaleY();
        int clipRestoreCount = c.save();
        c.clipRect(mValuesRect);
        c.translate(0,mChart.getTitleHeight() * mViewPortHandler.getScaleY());

        for (int i = 0; i < mChart.getColumnCount(); i++) {
            drawColumn(c, columns.get(i), i);
        }

        c.restoreToCount(clipRestoreCount);

    }



    private void drawColumn(Canvas c, Column<Cell> column, int index) {

        if (transformer == null) {
            transformer = mChart.getTransformer();
        }
        if (transformer == null) return;




        ColumnBuffer columnBuffer = mBuffers[index];

        columnBuffer.feed(column);

        transformer.pointValuesToPixel(columnBuffer.buffer);

        for (int i = 0; i < columnBuffer.size(); i += 4) {
            if ((columnBuffer.buffer[i] > mViewPortHandler.contentRight()) || (columnBuffer.buffer[i + 2] < mViewPortHandler.contentLeft())) {
                continue;
            }
            c.drawRect(columnBuffer.buffer[i], columnBuffer.buffer[i + 1], columnBuffer.buffer[i + 2],
                    columnBuffer.buffer[i + 3], mGridPaint);
            Utils.drawSingleText(c, mValuePaint,
                    Utils.getTextCenterX(columnBuffer.buffer[i], columnBuffer.buffer[i + 2], mValuePaint),
                    Utils.getTextCenterY((columnBuffer.buffer[i + 1] + columnBuffer.buffer[i + 3]) / 2, mValuePaint),
                    column.getData().get(i / 4).getContents()
            );
        }

    }


    @Override
    public void drawValues(Canvas c) {

    }

    @Override
    public void drawTitle(Canvas c) {
        transformer = mChart.getTransformer();
        if (transformer == null) return;

        mTitleBuffer.feed(mChart.getColumnList());
        transformer.pointValuesToPixel(mTitleBuffer.buffer);

        for (int i = 0; i < mTitleBuffer.size(); i += 4) {

            float left = mTitleBuffer.buffer[i];
            float top = mTitleBuffer.buffer[i+1];
            float right = mTitleBuffer.buffer[i+2];
            float bottom = mTitleBuffer.buffer[i+3];
            float height = bottom - top;

            if ((left > mViewPortHandler.contentRight()) || (right < mViewPortHandler.contentLeft())) {
                continue;
            }


            if(mChart.isTitleFixed()){
                if(top < 0){
                    top = 0;
                    bottom = height;
                }

            }

            c.drawRect(left,top,right,bottom, mGridPaint);
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
    public void drawHighlighted(Canvas c, Highlight[] indices) {

    }
}
