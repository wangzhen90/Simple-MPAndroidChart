package com.wangzhen.simplechartlib.tableChart.interfaces;

import com.wangzhen.simplechartlib.tableChart.interfaces.cell.Cell;

/**
 * Created by wangzhen on 2018/7/5.
 */

public interface CellRange {

    Cell getTopLeft();

    Cell getBottomRight();
}
