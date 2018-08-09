package com.wangzhen.tableChart.interfaces;

import com.wangzhen.tableChart.data.Cell;
import com.wangzhen.tableChart.data.Column;

/**
 * Created by wangzhen on 2018/7/18.
 */

public interface ITableOnClickListener {

    void onColumnClick(Column column);

    void onCellClick(ICell cell);

}
