package com.wangzhen.simplechartlib.tableChart.highlight;

/**
 * Created by wangzhen on 2018/7/16.
 */

public class Highlight {

    private int columnIndex = -1;

    private int row = -1;
    private int column = -1;
    private int lastRow = -1;
    private int lastColumn = -1;

    private boolean isTitle;


    public boolean isTitle() {

        return isTitle;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getLastRow() {
        return lastRow;
    }

    public int getLastColumn() {
        return lastColumn;
    }

    public boolean equalTo(Highlight h) {

        return true;
    }


}
