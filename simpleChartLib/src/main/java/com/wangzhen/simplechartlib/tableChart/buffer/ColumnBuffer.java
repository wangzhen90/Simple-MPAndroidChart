package com.wangzhen.simplechartlib.tableChart.buffer;

import com.wangzhen.simplechartlib.tableChart.data.Cell;
import com.wangzhen.simplechartlib.tableChart.data.Column;

import java.util.List;

/**
 * Created by wangzhen on 2018/7/10.
 */

public class ColumnBuffer extends AbstractBuffer<Column<Cell>> {

    public ColumnBuffer(int size) {
        super(size);
    }


    private void addCell(int left, int top, int right, int bottom){
        buffer[index++] = left;
        buffer[index++] = top;
        buffer[index++] = right;
        buffer[index++] = bottom;

    }

    @Override
    public void feed(Column<Cell> column) {
        int cellSize = column.getData().size();
        List<Cell> cells = column.getData();
        Cell cell;
        for(int i = 0; i < cellSize; i++){
            cell = cells.get(i);

            int left = column.getPreColumnsWidth();
            int top = cell.getRow() * column.getRowHeight();
            int right = left + column.getWidth();
            int bottom = top + column.getRowHeight();

            addCell(left,top,right,bottom);
        }
        reset();
    }
}
