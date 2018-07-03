package com.wangzhen.simplechartlib.tableChart.data;

/**
 * Created by wangzhen on 2018/6/11.
 */

public class Cell {
    //所在行数
    public int row;
    //所在列数
    public int column;

    public int width;
    public int height;

    public Cell(int row, int column){

        this.row = row;
        this.column = column;
    }


}
