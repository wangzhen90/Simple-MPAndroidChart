package com.wangzhen.tableChart.formatter;

import com.wangzhen.tableChart.data.Column;
import com.wangzhen.tableChart.interfaces.ICell;

import java.util.List;

/**
 * Created by wangzhen on 2018/7/30.
 */

public class DefaultBgFormatter implements IBgFormatter{

    @Override
    public String getBackgroundColor(ICell cell, Column<ICell> column, List<Column<ICell>> columns) {
        if(cell.getRow() % 2 == 0){
            return null;
        }else{
            return "#C8C2C6";
        }
    }
}
