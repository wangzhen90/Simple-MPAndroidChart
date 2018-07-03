package com.wangzhen.simplechartlib.tableChart.data;

import java.util.List;

/**
 * Created by wangzhen on 2018/7/2.
 */

public class TableData<T> {

    public String tableName;
    private List<Column> columns;
    private List<Column> childColumns;

    //TODO 1.[column][row] => [row][column]
    //TODO 2.解析转化后的数据，填充childColumns (参考 TableParser)

}
