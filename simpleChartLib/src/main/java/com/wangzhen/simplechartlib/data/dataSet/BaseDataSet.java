package com.wangzhen.simplechartlib.data.dataSet;

import android.graphics.Color;

import com.wangzhen.simplechartlib.data.entry.Entry;
import com.wangzhen.simplechartlib.formatter.IValueFormatter;
import com.wangzhen.simplechartlib.interfaces.dataSets.IDataSet;
import com.wangzhen.simplechartlib.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhen on 2018/3/13.
 */

public abstract class BaseDataSet<T extends Entry> implements IDataSet<T> {


    protected List<Integer> mColors = null;

    protected List<Integer> mValueColors = null;

    protected boolean mHighlightEnabled = true;
    /**
     * transient 修饰，该字段不需要序列化
     */
    protected transient IValueFormatter mValueFormatter;


    protected float mValueTextSize = 17f;

    protected boolean mVisible = true;

    protected boolean mDrawValues = true;



    public BaseDataSet() {
        mColors = new ArrayList<Integer>();
        mValueColors = new ArrayList<Integer>();

        // default color
        mColors.add(Color.rgb(140, 234, 255));
        mValueColors.add(Color.BLACK);
    }

    /**
     * 如果数据改变需要重新计算
     */
    public void notifyDataSetChanged() {
        calcMinMax();
    }


    @Override
    public List<Integer> getColors() {
        return mColors;
    }

    public List<Integer> getValueColors() {
        return mValueColors;
    }


    @Override
    public int getColor() {
        return mColors.get(0);
    }

    @Override
    public int getColor(int index) {
        return mColors.get(index % mColors.size());
    }

    public void setColors(List<Integer> colors) {
        this.mColors = colors;
    }


    @Override
    public void setHighlightEnabled(boolean enabled) {
        mHighlightEnabled = enabled;
    }

    @Override
    public boolean isHighlightEnabled() {
        return mHighlightEnabled;
    }

    @Override
    public void setValueFormatter(IValueFormatter f) {

        if (f == null)
            return;
        else
            mValueFormatter = f;
    }

    @Override
    public IValueFormatter getValueFormatter() {
        if (needsFormatter())
            return Utils.getDefaultValueFormatter();
        return mValueFormatter;
    }

    @Override
    public boolean needsFormatter() {
        return mValueFormatter == null;
    }

    @Override
    public void setValueTextColor(int color) {
        mValueColors.clear();
        mValueColors.add(color);
    }

    @Override
    public void setValueTextSize(float size) {
        mValueTextSize = Utils.convertDpToPixel(size);
    }


    @Override
    public int getIndexInEntries(int xIndex) {

        for (int i = 0; i < getEntryCount(); i++) {
            if (xIndex == getEntryForIndex(i).getX())
                return i;
        }

        return -1;
    }


    @Override
    public boolean removeFirst() {

        if (getEntryCount() > 0) {

            T entry = getEntryForIndex(0);
            return removeEntry(entry);
        } else
            return false;
    }

    @Override
    public boolean removeLast() {

        if (getEntryCount() > 0) {

            T e = getEntryForIndex(getEntryCount() - 1);
            return removeEntry(e);
        } else
            return false;
    }

//    @Override
//    public boolean removeEntryByXValue(float xValue) {
//
//        T e = getEntryForXValue(xValue, Float.NaN);
//        return removeEntry(e);
//    }

    @Override
    public boolean removeEntry(int index) {

        T e = getEntryForIndex(index);
        return removeEntry(e);
    }

    @Override
    public boolean contains(T e) {

        for (int i = 0; i < getEntryCount(); i++) {
            if (getEntryForIndex(i).equals(e))
                return true;
        }

        return false;
    }


    @Override
    public void setDrawValues(boolean enabled) {
        this.mDrawValues = enabled;
    }

}
