package com.wangzhen.simplechartlib.tableChart.renderder;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.wangzhen.simplechartlib.highlight.Highlight;
import com.wangzhen.simplechartlib.tableChart.buffer.ColumnBuffer;
import com.wangzhen.simplechartlib.tableChart.component.TableChart;
import com.wangzhen.simplechartlib.utils.Transformer;
import com.wangzhen.simplechartlib.utils.Utils;
import com.wangzhen.simplechartlib.utils.ViewPortHandler;

/**
 * Created by wangzhen on 2018/7/7.
 */

public class SimpleRenderer extends DataRenderer {


    private TableChart mChart;

    private ColumnBuffer mTitleBuffer;
    private ColumnBuffer mContentBuffer;
    private Transformer transformer;

    public SimpleRenderer(ViewPortHandler viewPortHandler, TableChart chart) {
        super(viewPortHandler);
        this.mChart = chart;

    }

    @Override
    public void initBuffers() {
        mTitleBuffer = new ColumnBuffer(mChart.getColumnCount() * 4,mChart.getColumnCount());
    }

    @Override
    public void drawData(Canvas c) {

    }

    @Override
    public void drawValues(Canvas c) {

    }

    @Override
    public void drawTitle(Canvas c) {
        transformer = mChart.getTransformer();
        if(transformer == null) return;

        mTitleBuffer.feed(mChart.getColumnList());
        transformer.pointValuesToPixel(mTitleBuffer.buffer);

        for(int i = 0; i < mTitleBuffer.size(); i+=4){
            if((mTitleBuffer.buffer[i] > mViewPortHandler.contentRight()) || (mTitleBuffer.buffer[i+2] < mViewPortHandler.contentLeft())){
                continue;
            }
            c.drawRect(mTitleBuffer.buffer[i], mTitleBuffer.buffer[i + 1], mTitleBuffer.buffer[i + 2],
                    mTitleBuffer.buffer[i + 3],mGridPaint);
            Utils.drawSingleText(c,mValuePaint,
                    Utils.getTextCenterX(mTitleBuffer.buffer[i], mTitleBuffer.buffer[i+2], mValuePaint),
                    Utils.getTextCenterY((mTitleBuffer.buffer[i+1] + mTitleBuffer.buffer[i+3])/2, mValuePaint),
                    mTitleBuffer.columnNames[i/4]
            );
        }


    }

    @Override
    public void drawExtras(Canvas c) {

    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

    }
}
