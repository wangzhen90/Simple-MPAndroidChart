package com.wangzhen.tableChart.formatter;

import android.text.TextPaint;

import com.wangzhen.tableChart.data.Column;
import com.wangzhen.tableChart.interfaces.ICell;

import java.util.List;

/**
 * Created by wangzhen on 2018/7/27.
 */

public interface ITextFormatter {


    int getTextSize(ICell cell, Column<ICell> column, List<Column<ICell>> columns);

    TextPaint.Align getTextAlign(ICell cell, Column<ICell> column, List<Column<ICell>> columns);

    String getTextColor(ICell cell, Column<ICell> column, List<Column<ICell>> columns);

}
