package com.wangzhen.simplechartlib.tableChart.data;

import com.wangzhen.simplechartlib.tableChart.interfaces.ICell;

/**
 * Created by wangzhen on 2018/7/6.
 */

public class Cell implements ICell {

    private int row;
    private int column;
    private String contents;
    //TODO 添加formatter


    public Cell(int row, int column,String contents){
        this.row = row;
        this.column = column;
        this.contents = contents;
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
        return contents;
    }

    @Override
    public CellType getType() {
        return CellType.LABEL;
    }
}
