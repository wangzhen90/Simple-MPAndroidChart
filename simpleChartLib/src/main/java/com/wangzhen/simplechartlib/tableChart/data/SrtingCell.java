package com.wangzhen.simplechartlib.tableChart.data;

/**
 * Created by wangzhen on 2018/6/28.
 */

public class SrtingCell extends Cell {

    public String content;

    public SrtingCell(int row, int column) {
        super(row, column);
    }

    public SrtingCell(int row, int column,String content) {
        super(row, column);
        this.content = content;
    }


}
