package com.wangzhen.tableChart.interfaces;

import com.wangzhen.tableChart.data.CellType;

/**
 * Created by wangzhen on 2018/6/11.
 */

public interface ICell {
    int getRow();

    int getColumn();

    int getLastRow();
    int getLastColumn();


    boolean isHidden();

    String getContents();

    CellType getType();
//
//    CellFormat getCellFormat();
//
//    CellFeatures getCellFeatures();
}
