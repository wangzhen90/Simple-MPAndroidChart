package com.wangzhen.simplechartlib.tableChart.interfaces.cell;

/**
 * Created by wangzhen on 2018/6/11.
 */

public interface Cell {
    int getRow();

    int getColumn();

    boolean isHidden();

    String getContents();

//    CellType getType();
//
//    CellFormat getCellFormat();
//
//    CellFeatures getCellFeatures();
}
