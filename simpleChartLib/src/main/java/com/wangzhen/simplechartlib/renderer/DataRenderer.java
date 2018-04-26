package com.wangzhen.simplechartlib.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.wangzhen.simplechartlib.data.entry.Entry;
import com.wangzhen.simplechartlib.formatter.IValueFormatter;
import com.wangzhen.simplechartlib.interfaces.charts.ChartInterface;
import com.wangzhen.simplechartlib.interfaces.dataSets.IDataSet;
import com.wangzhen.simplechartlib.utils.Utils;
import com.wangzhen.simplechartlib.utils.ViewPortHandler;

/**
 * Created by wangzhen on 2018/4/26.
 */

public abstract class DataRenderer extends Renderer {


    protected Paint mRenderPaint;

    protected Paint mValuePaint;

    public DataRenderer(ViewPortHandler viewPortHandler) {
        super(viewPortHandler);

        mRenderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRenderPaint.setStyle(Paint.Style.FILL);

        mValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValuePaint.setColor(Color.rgb(63,63,63));
        mValuePaint.setTextAlign(Paint.Align.CENTER);
        mValuePaint.setTextSize(Utils.convertDpToPixel(9f));

    }

    /**
     * 判断是否要绘制value，如果条目太多的时候，绘制value会重叠
     * @param chart
     * @return
     */
    protected boolean isDrawingValuesAllowed(ChartInterface chart){

        return chart.getData().getEntryCount() < chart.getMaxVisibleCount() * mViewPortHandler.getScaleX();
    }


    //TODO 暂不设置，应该在添加个颜色
    protected void applyValueTextStyle(IDataSet set) {

//        mValuePaint.setTypeface(set.getValueTypeface());
//        mValuePaint.setTextSize(set.getValueTextSize());
    }

    /**
     * TODO 暂时未看懂
     * Initializes the buffers used for rendering with a new size. Since this
     * method performs memory allocations, it should only be called if
     * necessary.
     */
    public abstract void initBuffers();

    public abstract void drawData(Canvas c);
    public abstract void drawValues(Canvas c);

    /**
     * 绘制除了data和values之外的东西，比如背景色，折现图的原点等
     * @param c
     */
    public abstract void drawExtras(Canvas c);

    /**
     * Draws the value of the given entry by using the provided IValueFormatter.
     *
     * @param c            canvas
     * @param formatter    formatter for custom value-formatting
     * @param value        the value to be drawn
     * @param entry        the entry the value belongs to
     * @param dataSetIndex the index of the DataSet the drawn Entry belongs to
     * @param x            position
     * @param y            position
     * @param color
     */
    public void drawValue(Canvas c, IValueFormatter formatter, float value, Entry entry, int dataSetIndex, float x, float y, int color) {
        mValuePaint.setColor(color);
        c.drawText(formatter.getFormattedValue(value, entry, dataSetIndex, mViewPortHandler), x, y, mValuePaint);
    }





}
