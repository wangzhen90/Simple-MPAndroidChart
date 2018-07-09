package com.wangzhen.simplechartlib.tableChart.data;

import android.util.Log;

import com.wangzhen.simplechartlib.tableChart.interfaces.ICellRange;
import com.wangzhen.simplechartlib.tableChart.interfaces.ISheet;
import com.wangzhen.simplechartlib.tableChart.interfaces.ICell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhen on 2018/7/2.
 */

public class Sheet<T> implements ISheet {

    private static final String TAG = Sheet.class.getSimpleName();
    private T[][] data;
    private List<T> dataList;
    public String tableName;
    private List<Column<T>> columns;
    private List<Column<T>> childColumns;

    private int mHeight;
    private int mWidth;

    private int rowHeigt = 30;

    private ArrayList<ICellRange> mergedCells = new ArrayList();


    public Sheet(List columns, List data) {

        this.columns = columns;
        this.dataList = data;

        calculate();
    }


    public void setData(T[][] data) {
        this.data = data;
        calculate();
    }

    public T[][] getData() {
        return data;
    }


    public void setColumns(List<Column<T>> columns) {
        this.columns = columns;
    }

    @Override
    public int getRows() {
        return 0;
    }

    @Override
    public int getColumns() {
        return columns.size();
    }

    @Override
    public List<Column<T>> getColumnList() {
        return columns;
    }

    @Override
    public ICell[] getRow(int rowIndex) {
        return new ICell[0];
    }

    @Override
    public ICell[] getColumn(int columnIndex) {
        return new ICell[0];
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public ICellRange[] getMergedCells() {

        return new ICellRange[0];
    }

    @Override
    public int getColumnWidth(int var1) {
        return 0;
    }

    @Override
    public int getRowHeight(int var1) {
        return 0;
    }

    @Override
    public int getWidth() {
        return mWidth;
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    @Override
    public void calculate() {
        mHeight = 0;
        mWidth = 0;
        int rowsSum = 0;
        int titleheight = 0;
        for (Column column : columns) {
            mWidth += column.getWidth();
            titleheight = column.getTitleHeight();
            if (column.getData() != null) {
                if (column.getData().size() > rowsSum) {
                    rowsSum = column.getData().size();
                }
            }
        }

        mHeight = rowsSum * rowHeigt + titleheight;
    }


    public ICellRange mergeCells(int firstRow, int firstColumn, int lastRow, int lastColumn) {

        if (lastColumn < firstColumn || lastRow < firstRow) {
            Log.e(TAG, "Cannot merge cells - top left and bottom right incorrectly specified");
        }

        //TODO 如果超出了最大行数和列数，就添加这个cell
//        if(col2 >= this.numColumns || row2 >= this.numRows) {
//            this.addCell(new Blank(col2, row2));
//        }

        CellRange range = new CellRange(firstRow, firstColumn, lastRow, lastColumn);
        this.mergedCells.add(range);

        return range;

    }

}
