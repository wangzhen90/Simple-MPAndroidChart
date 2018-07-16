package com.wangzhen.simplechartlib.tableChart.highlight;

import android.graphics.Rect;
import android.graphics.RectF;

import com.wangzhen.simplechartlib.tableChart.component.TableChart;
import com.wangzhen.simplechartlib.tableChart.data.Cell;
import com.wangzhen.simplechartlib.tableChart.data.Column;

/**
 * Created by wangzhen on 2018/7/16.
 */

public class Highlight {


    protected TableChart mChart;
    private int columnIndex = -1;

//    private int row = -1;
//    private int column = -1;
//    private int lastRow = -1;
//    private int lastColumn = -1;

    private Cell mCell;
    private boolean isTitle;

    public Highlight(TableChart chart){
        this.mChart = chart;
    }

    public Highlight(TableChart chart,int columnIndex,Cell cell,boolean isTitle){
        this.mChart = chart;
        this.columnIndex = columnIndex;
        this.mCell = cell;
        this.isTitle = isTitle;
    }



    public boolean isTitle() {

        return isTitle;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getColumn() {
        return mCell == null ? -1 : mCell.getColumn();
    }

    public int getRow() {
        return mCell == null ? -1 : mCell.getRow();
    }

    public int getLastRow() {
        return mCell == null ? -1 : mCell.getLastRow();
    }

    public int getLastColumn() {
        return mCell == null ? -1 : mCell.getLastColumn();
    }

    public boolean equalTo(Highlight h) {

        if(h == null) return false;

        return h.getRect().equals(getRect());
    }


    public RectF getRect() {

        if (columnIndex < 0) return null;

        Column column = mChart.getColumnList().get(columnIndex);

        if (column == null) return null;

        RectF highlightPort = new RectF();

        if (isTitle) {
            highlightPort.set(column.getLeft(), 0, column.getRight(), mChart.getViewPortHandler().contentBottom());

        } else if (mCell != null) {

            int left = column.getLeft();
            int top = getRow() * column.getRowHeight();
            int right = left;

            for (int i = columnIndex; i < getLastColumn() + 1; i++) {
                right += mChart.getColumnList().get(i).getWidth();
            }

            int bottom = (getLastRow() + 1) * column.getRowHeight();


            highlightPort.set(left, top, right, bottom);
        }


        return highlightPort;
    }

}
