package com.wangzhen.tableChart.interfaces;

import com.wangzhen.tableChart.data.Cell;
import com.wangzhen.tableChart.data.Column;
import com.wangzhen.tableChart.formatter.IBgFormatter;
import com.wangzhen.tableChart.formatter.ITextFormatter;

import java.util.List;

/**
 * Created by wangzhen on 2018/7/5.
 */

public interface ISheet<T extends ICell> {

    int getRows();

    int getColumns();

    List<Column<T>> getColumnList();

    ICell[] getRow(int rowIndex);

    ICell[] getColumn(int columnIndex);

    String getName();

    boolean isHidden();

    ICellRange[] getMergedCells();

    int getColumnWidth(int var1);

    int getRowHeight(int var1);

    int getWidth();

    int getHeight();

    void calculate();

    boolean hasMergedCell();

    ITextFormatter getTextFormatter();
    IBgFormatter getBgFormatter();

    Column<T> getColumnByXValue(double xValue);
    T getCellByTouchPoint(double xValue, double yValue);


}
