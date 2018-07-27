package com.wangzhen.tableChart.formatter;

import android.graphics.Paint;
import android.text.TextPaint;

import com.wangzhen.tableChart.data.Column;
import com.wangzhen.tableChart.interfaces.ICell;

import java.util.List;

/**
 * Created by wangzhen on 2018/7/27.
 */

public class DefaultTextFormatter implements ITextFormatter {
    @Override
    public int getTextSize(ICell cell, Column<ICell> column, List<Column<ICell>> columns) {
        return 9;
    }

    @Override
    public TextPaint.Align getTextAlign(ICell cell, Column<ICell> column, List<Column<ICell>> columns) {
        return TextPaint.Align.CENTER;
    }
}
