package com.wangzhen.simplechartlib.interfaces.dataSets;

import com.wangzhen.simplechartlib.data.entry.Entry;
import com.wangzhen.simplechartlib.formatter.IValueFormatter;

import java.util.List;

/**
 * Created by wangzhen on 2018/3/13.
 */

public interface IDataSet<T extends Entry> {

    /**
     * 是否展示这一组数据
     * @return
     */
    boolean isVisible();

    float getYMin();
    float getYMax();

    float getXMin();
    float getXMax();
    /**
     * 计算 (mXMin, mXMax, mYMin, mYMax).
     */
    void calcMinMax();

    /**
     * 计算在fromX - toX 之间y值的最大最小值，只有设置autoScaleMinMax的时候才会使用
     * @param fromX
     * @param toX
     */
    void calcMinMaxY(float fromX, float toX);


    /**
     * Returns the first Entry object found at the given x-value with binary
     * search.
     * If the no Entry at the specified x-value is found, this method
     * returns the Entry at the closest x-value.
     * INFORMATION: This method does calculations at runtime. Do
     * not over-use in performance critical situations.
     *
     *
     * @param xValue the x-value
     * @param closestToY If there are multiple y-values for the specified x-value,
     * @return
     */
    T getEntryForXValue(float xValue, float closestToY);

    /**
     * TODO 暂时没有想到使用的场景
     * Returns all Entry objects found at the given x-value with binary
     * search. An empty array if no Entry object at that x-value.
     * INFORMATION: This method does calculations at runtime. Do
     * not over-use in performance critical situations.
     *
     * @param xValue
     * @return
     */
    List<T> getEntriesForXValue(float xValue);

    T getEntryForIndex(int index);

    int getEntryCount();

    int getIndexInEntries(int xIndex);

    /**
     * Returns the position of the provided entry in the DataSets Entry array.
     * Returns -1 if doesn't exist.
     *
     * @param e
     * @return
     */
    int getEntryIndex(T e);

    /**
     * Adds an Entry to the DataSet dynamically.
     * Entries are added to the end of the list.
     * This will also recalculate the current minimum and maximum
     * values of the DataSet and the value-sum.
     *
     * @param e
     */
    boolean addEntry(T e);

    /**
     * Adds an Entry to the DataSet dynamically.
     * Entries are added to their appropriate index in the values array respective to their x-position.
     * This will also recalculate the current minimum and maximum
     * values of the DataSet and the value-sum.
     *
     * @param e
     */
    void addEntryOrdered(T e);

    /**
     * Removes the first Entry (at index 0) of this DataSet from the entries array.
     * Returns true if successful, false if not.
     *
     * @return
     */
    boolean removeFirst();

    /**
     * Removes the last Entry (at index size-1) of this DataSet from the entries array.
     * Returns true if successful, false if not.
     *
     * @return
     */
    boolean removeLast();

    /**
     * Removes an Entry from the DataSets entries array. This will also
     * recalculate the current minimum and maximum values of the DataSet and the
     * value-sum. Returns true if an Entry was removed, false if no Entry could
     * be removed.
     *
     * @param e
     */
    boolean removeEntry(T e);

    boolean removeEntry(int index);

    boolean contains(T entry);

    void clear();

    List<Integer> getColors();
    int getColor();
    int getColor(int index);

    boolean isHighlightEnabled();
    void setHighlightEnabled(boolean enabled);

    void setValueFormatter(IValueFormatter f);
    IValueFormatter getValueFormatter();

    void setValueTextColor(int color);
    void setValueTextColors(List<Integer> colors);
    void setValueTextSize(float size);



    boolean needsFormatter();





}
