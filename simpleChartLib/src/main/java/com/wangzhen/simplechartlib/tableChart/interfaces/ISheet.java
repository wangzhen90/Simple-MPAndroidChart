package com.wangzhen.simplechartlib.tableChart.interfaces;

/**
 * Created by wangzhen on 2018/7/5.
 */

public interface ISheet {

    int getRows();

    int getColumns();

    ICell[] getRow(int rowIndex);

    ICell[] getColumn(int columnIndex);

    String getName();

    boolean isHidden();

    ICellRange[] getMergedCells();

    int getColumnWidth(int var1);

    int getRowHeight(int var1);

}
