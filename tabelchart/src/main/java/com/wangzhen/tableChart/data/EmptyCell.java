package com.wangzhen.tableChart.data;

import com.wangzhen.tableChart.interfaces.ICell;

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
    public int getLastRow() {
        return 0;
    }

    @Override
    public int getLastColumn() {
        return 0;
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
