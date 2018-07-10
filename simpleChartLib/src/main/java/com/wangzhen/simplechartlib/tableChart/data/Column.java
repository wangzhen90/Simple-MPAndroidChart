package com.wangzhen.simplechartlib.tableChart.data;

import com.wangzhen.simplechartlib.tableChart.interfaces.ICell;

import java.util.List;

/**
 * Created by wangzhen on 2018/6/11.
 */

public class Column<T> {


    public String columnName;

    public int columnIndex;

    //通过计算获得
    public int width;
    public int titleHeight;

    public int maxWidth;

    public int minWidth = 30;

    private List<T> datas;

    /**
     * 子列
     */
    private List<Column> children;

    private int level;

    private boolean isParent;


    private boolean autoComputeSize;

    public String longestString;

    private boolean isFixed;

    private int preColumnsWidth;

    private int rowHeight;

    public Column() {

    }

    public Column(String columnName) {
        this.columnName = columnName;
    }


    public void setData(List<T> datas) {
        this.datas = datas;
    }

    public List<T> getData() {

        return datas;
    }


    //TODO 列计算宽度：除掉单元格外，计算最长的文字作为其宽度
    public int computeWidth() {
        return 0;
    }


    public int getWidth() {

        return 200;
    }

    public int getTitleHeight() {
        return titleHeight;
    }

    public void setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
    }


    public void setPreColumnsWidth(int width) {
        this.preColumnsWidth = width;
    }

    public int getPreColumnsWidth() {
        return preColumnsWidth;
    }


    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }
}
