package com.wangzhen.simplechartlib.tableChart.interfaces;

import com.wangzhen.simplechartlib.tableChart.interfaces.cell.Cell;

/**
 * Created by wangzhen on 2018/7/5.
 */

public interface Sheet {

    int getRows();

    int getColumns();

    Cell[] getRow(int rowIndex);

    Cell[] getColumn(int columnIndex);

    String getName();

    boolean isHidden();

    CellRange[] getMergedCells();

    int getColumnWidth(int var1);

    int getRowHeight(int var1);

}
