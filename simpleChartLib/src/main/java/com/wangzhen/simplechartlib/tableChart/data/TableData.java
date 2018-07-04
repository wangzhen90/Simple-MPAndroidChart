package com.wangzhen.simplechartlib.tableChart.data;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by wangzhen on 2018/7/2.
 */

public class TableData<T> {

    private  T[][] data;
    public String tableName;
    private List<Column> columns;
    private List<Column> childColumns;


    //TODO 2.解析转化后的数据，填充childColumns (参考 TableParser)

    /**
     * [column][row] => [row][column]
     *
     * @param rowArray
     * @param <T>
     * @return
     */
    public static <T> T[][] transformColumnArray(T[][] rowArray) {
        T[][] columnArray = null;

        T[] row = null;

        if (rowArray != null) {
            int maxLength = 0;

            for (T[] childRow : rowArray) {
                if (childRow != null && childRow.length > maxLength) {
                    maxLength = childRow.length;
                    row = childRow;
                }
            }

            if (row != null) {

                columnArray = (T[][]) Array.newInstance(row.getClass().getComponentType(), maxLength);

                for (int i = 0; i < rowArray.length; i++) {

                    for (int j = 0; j < rowArray[i].length; j++) {
                        if (columnArray[j] == null) {
                            columnArray[j] = (T[]) Array.newInstance(row.getClass().getComponentType(), rowArray.length);
                        }
                        columnArray[j][i] = rowArray[i][j];
                    }
                }
            }
        }
        return columnArray;
    }


    public void setData(T[][] data) {
        this.data = data;
    }

    public T[][] getData() {
        return data;
    }





}
