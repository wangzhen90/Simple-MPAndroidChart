package com.wangzhen.simplechartlib.tableChart.data;

import android.graphics.Paint;

import com.wangzhen.simplechartlib.tableChart.interfaces.ICell;
import com.wangzhen.simplechartlib.utils.Utils;

import java.util.List;

/**
 * Created by wangzhen on 2018/6/11.
 */

public class Column<T extends Cell> {


    public String columnName;

    public int columnIndex;

    //通过计算获得
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

    public String longestString = "";

    private boolean isFixed;

    private int preColumnsWidth;

    private int rowHeight;

    private int columnWidth = 0;


    private int leftOffset;
    private int rightOffset;


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


    public int computeWidth() {

        columnWidth = Utils.calcTextWidth(Utils.paint,columnName);

        int cellWidth = 0;

        if(datas != null){
            String cellContent;
            for(int i = 0; i < datas.size(); i++){
                cellContent = datas.get(i).getContents();
                if(cellContent.length() > longestString.length()){
                    longestString = cellContent;
                    cellWidth = Utils.calcTextWidth(Utils.paint,cellContent);
                }
            }
        }

        columnWidth = Math.max(columnWidth,cellWidth) + leftOffset + rightOffset;

        return columnWidth;
    }


    public void fillPaint(Paint paint){
        paint.setTextSize(Utils.convertDpToPixel(9f));

    }


    public int getWidth() {

        return columnWidth;
//        return 200;
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

    public int getLeftOffset() {
        return leftOffset;
    }

    public void setLeftOffset(int leftOffset) {
        this.leftOffset = leftOffset;
    }

    public int getRightOffset() {
        return rightOffset;
    }

    public void setRightOffset(int rightOffset) {
        this.rightOffset = rightOffset;
    }
}
