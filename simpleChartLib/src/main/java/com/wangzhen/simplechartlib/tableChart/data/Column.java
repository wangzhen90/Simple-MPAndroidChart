package com.wangzhen.simplechartlib.tableChart.data;

import java.util.List;

/**
 * Created by wangzhen on 2018/6/11.
 */

public abstract class Column<T extends Cell> {


    public String columnName;

    public int columnIndex;


    public int width;

    public int maxWidth;

    public int minWidth = 30;

    private List<T> datas;

    /**
     *  子列
     */
    private List<Column> children;

    private int level;

    private boolean isParent;


    //TODO 列计算宽度：除掉单元格外，计算最长的文字作为其宽度
    public abstract int computeWidth();




}
