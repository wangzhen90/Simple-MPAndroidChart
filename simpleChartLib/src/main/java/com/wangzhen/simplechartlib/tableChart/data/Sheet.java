package com.wangzhen.simplechartlib.tableChart.data;

import android.util.Log;

import com.wangzhen.simplechartlib.tableChart.interfaces.ICellRange;
import com.wangzhen.simplechartlib.tableChart.interfaces.ISheet;
import com.wangzhen.simplechartlib.tableChart.interfaces.ICell;
import com.wangzhen.simplechartlib.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhen on 2018/7/2.
 */

public class Sheet<T extends Cell> implements ISheet {

    private static final String TAG = Sheet.class.getSimpleName();
    private T[][] data;
    private List<T> dataList;
    public String tableName;
    private List<Column<T>> columns;
    private List<Column<T>> childColumns;

    private int mHeight;
    private int mWidth;
    private int cellCounts;

    private int rowHeight = 60;

    private int columnLeftOffset = 30;
    private int columnRightOffset= 30;


    private ArrayList<ICellRange> mergedCells = new ArrayList();

    private int mTitleHeight = 60;


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
        int maxRowCount = 0;
        for (int i = 0; i < columns.size(); i++) {

            Column<T> column = columns.get(i);

            column.columnIndex = i;
            column.fillPaint(Utils.paint);
            column.setLeftOffset(columnLeftOffset);
            column.setRightOffset(columnRightOffset);

            column.computeWidth();
            column.setPreColumnsWidth(mWidth);
            column.setRowHeight(rowHeight);
            column.setTitleHeight(mTitleHeight);

            mWidth += column.getWidth();

            if (column.getData() != null) {
                if (column.getData().size() > maxRowCount) {
                    maxRowCount = column.getData().size();
                }
            }
        }

        mHeight = maxRowCount * rowHeight + mTitleHeight;

        cellCounts = maxRowCount * getColumns();

    }


    public ICellRange mergeCells(int firstRow, int firstColumn, int lastRow, int lastColumn) {

        if (lastColumn < firstColumn || lastRow < firstRow) {
//            Log.e(TAG, "Cannot merge cells - top left and bottom right incorrectly specified");
        }

        //TODO 如果超出了最大行数和列数，就添加这个cell
//        if(col2 >= this.numColumns || row2 >= this.numRows) {
//            this.addCell(new Blank(col2, row2));
//        }

        CellRange range = new CellRange(firstRow, firstColumn, lastRow, lastColumn);
        this.mergedCells.add(range);

        return range;

    }

    public int getCellCounts() {
        return cellCounts;
    }

    public int getTitleHeight() {
        return mTitleHeight;
    }

    public void setTitleHeight(int mTitleHeight) {
        this.mTitleHeight = mTitleHeight;
    }


    public Column<T> getColumnByXValue(double xValue){

        if(columns == null || columns.isEmpty()){
            return null;
        }

        int low = 0;
        int high = columns.size();

        int mid;
        Column<T> targetColumn = null;

        while(low < high){
            mid = (low + high)/2;

            targetColumn = columns.get(mid);

            if(targetColumn.getLeft() < xValue && targetColumn.getRight() > xValue){
                return targetColumn;
            }else if(targetColumn.getLeft() > xValue){
                high = mid;
            }else if(targetColumn.getRight() < xValue){
                low = mid;
            }
        }

        return targetColumn;
    }
}
