package com.wangzhen.simplechartlib.tableChart.interfaces;

import com.wangzhen.simplechartlib.tableChart.data.Cell;
import com.wangzhen.simplechartlib.tableChart.data.Column;

/**
 * Created by wangzhen on 2018/7/18.
 */

public interface ITableOnClickListener {

    void onColumnClick(Column column);

    void onCellClick(Cell cell);

}
