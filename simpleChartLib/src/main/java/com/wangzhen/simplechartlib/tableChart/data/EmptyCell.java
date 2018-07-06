package com.wangzhen.simplechartlib.tableChart.data;

import com.wangzhen.simplechartlib.tableChart.interfaces.ICell;

/**
 * Created by wangzhen on 2018/7/6.
 */

public class EmptyCell implements ICell {

    private int row;
    private int column;


    public EmptyCell(int row, int column){

        this.row = row;
        this.column = column;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public String getContents() {
        return null;
    }

    @Override
    public CellType getType() {
        return CellType.EMPTY;
    }
}
