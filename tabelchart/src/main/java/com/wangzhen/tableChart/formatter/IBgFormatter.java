package com.wangzhen.tableChart.formatter;

import com.wangzhen.tableChart.data.Column;
import com.wangzhen.tableChart.interfaces.ICell;

import java.util.List;

/**
 * Created by wangzhen on 2018/7/27.
 */

public interface IBgFormatter {

    String getBackgroundColor(ICell cell, Column<ICell> column, List<Column<ICell>> columns);

}
