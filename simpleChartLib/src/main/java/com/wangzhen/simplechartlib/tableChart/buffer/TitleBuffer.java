package com.wangzhen.simplechartlib.tableChart.buffer;

import com.wangzhen.simplechartlib.tableChart.data.Column;

import java.util.List;

/**
 * Created by wangzhen on 2018/7/7.
 */

public class TitleBuffer extends AbstractBuffer<List<Column>> {

    public String[] columnNames;

    public TitleBuffer(int size, int columnSize) {
        super(size);
        columnNames = new String[columnSize];
    }

    private void addTitle(float left, float top, float right, float bottom){
        buffer[index++] = left;
        buffer[index++] = top;
        buffer[index++] = right;
        buffer[index++] = bottom;
    }

    @Override
    public void feed(List<Column> data) {
        float size = data.size();
        int width;
        int height;
        for(int i = 0; i < size; i++){

            Column column = data.get(i);
            width = column.getWidth();
            height = column.getTitleHeight();

            float left = column.getPreColumnsWidth() ;
            float top = 0;
            float right = left + width;
            float bottom = top + height;

            addTitle(left,top,right,bottom);
            columnNames[i] = column.columnName;

        }
        reset();
    }
}
