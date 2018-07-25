package com.wangzhen.tableChart.renderder;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.wangzhen.tableChart.formatter.IValueFormatter;

import com.wangzhen.tableChart.highlight.Highlight;
import com.wangzhen.tableChart.utils.Utils;
import com.wangzhen.tableChart.utils.ViewPortHandler;

/**
 * Created by wangzhen on 2018/6/28.
 */

public abstract class DataRenderer extends Renderer {


    protected Paint mValuePaint;

    protected Paint mGridPaint;

    protected Paint mTitleValuePaint;

    protected Paint mHighlightPaint;



    public DataRenderer(ViewPortHandler viewPortHandler) {


        super(viewPortHandler);

        mValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValuePaint.setTextAlign(Paint.Align.CENTER);
        mValuePaint.setTextSize(Utils.convertDpToPixel(9f));
        mValuePaint.setColor(Color.parseColor("#000000"));

        mTitleValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitleValuePaint.setTextAlign(Paint.Align.CENTER);
        mTitleValuePaint.setTextSize(Utils.convertDpToPixel(9f));
        mTitleValuePaint.setColor(Color.parseColor("#000000"));


        mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGridPaint.setStrokeWidth(1);
        mGridPaint.setColor(Color.parseColor("#d0d0d0"));
        mGridPaint.setStyle(Paint.Style.STROKE);

        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint.setStrokeWidth(Utils.convertDpToPixel(3f));
        mHighlightPaint.setStyle(Paint.Style.STROKE);
        mHighlightPaint.setColor(Color.parseColor("#2ca9e1"));



    }


    public abstract void initBuffers();

    public abstract void drawData(Canvas c);
    public abstract void drawValues(Canvas c);
    public abstract void drawTitle(Canvas c);


    public abstract void drawExtras(Canvas c);


//    public void drawValue(Canvas c, IValueFormatter formatter, float value, Entry entry, int dataSetIndex, float x, float y, int color) {
//
//    }

    public abstract void drawHighlighted(Canvas c, Highlight highlight);


}
