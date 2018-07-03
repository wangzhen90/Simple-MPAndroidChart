package com.wangzhen.simplechartlib.tableChart.renderder;

import android.graphics.Canvas;

import com.wangzhen.simplechartlib.data.entry.Entry;
import com.wangzhen.simplechartlib.formatter.IValueFormatter;
import com.wangzhen.simplechartlib.highlight.Highlight;
import com.wangzhen.simplechartlib.utils.ViewPortHandler;

/**
 * Created by wangzhen on 2018/6/28.
 */

public abstract class DataRenderer extends Renderer {
    public DataRenderer(ViewPortHandler viewPortHandler) {
        super(viewPortHandler);
    }


    public abstract void initBuffers();

    public abstract void drawData(Canvas c);
    public abstract void drawValues(Canvas c);


    public abstract void drawExtras(Canvas c);


    public void drawValue(Canvas c, IValueFormatter formatter, float value, Entry entry, int dataSetIndex, float x, float y, int color) {

    }

    public abstract void drawHighlighted(Canvas c, Highlight[] indices);


}
