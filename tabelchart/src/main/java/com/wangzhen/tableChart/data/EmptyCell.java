package com.wangzhen.tableChart.data;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import com.wangzhen.tableChart.interfaces.ICell;

import java.time.format.TextStyle;

/**
 * Created by wangzhen on 2018/7/6.
 */

public class EmptyCell implements ICell {

    private int row;
    private int column;
    private ICell realCell;


    public EmptyCell(int row, int column){

        this.row = row;
        this.column = column;
    }

    public EmptyCell(ICell oldCell,ICell realCell){
        this.row = oldCell.getRow();
        this.column = oldCell.getColumn();

        this.realCell =  realCell;
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
        return row;
    }

    @Override
    public int getLastColumn() {
        return column;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public String getContents() {
        if(realCell != null) return realCell.getContents();
        return null;
    }

    @Override
    public CellType getType() {
        return CellType.EMPTY;
    }
    @Override
    public ICell getRealCell() {
        return realCell;
    }
}
