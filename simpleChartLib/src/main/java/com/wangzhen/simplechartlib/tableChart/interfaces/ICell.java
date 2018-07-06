package com.wangzhen.simplechartlib.tableChart.interfaces;

import com.wangzhen.simplechartlib.tableChart.data.CellType;

/**
 * Created by wangzhen on 2018/6/11.
 */

public interface ICell {
    int getRow();

    int getColumn();

    boolean isHidden();

    String getContents();

    CellType getType();
//
//    CellFormat getCellFormat();
//
//    CellFeatures getCellFeatures();
}
