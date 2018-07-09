package com.wangzhen.simplechartlib.tableChart.interfaces;

import com.wangzhen.simplechartlib.tableChart.data.Column;

import java.util.List;

/**
 * Created by wangzhen on 2018/7/5.
 */

public interface ISheet<T> {

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

}
