package com.wangzhen.simplechartlib.data.chartData;

import android.util.Log;

import com.wangzhen.simplechartlib.data.entry.Entry;
import com.wangzhen.simplechartlib.formatter.IValueFormatter;
import com.wangzhen.simplechartlib.interfaces.dataSets.IDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhen on 2018/3/15.
 *
 * 包含一个图标中所有的dataSet 和 x-values
 *
 */

public class ChartData <T extends IDataSet<? extends Entry>>{

    /**
     * 所有dataSets中的最大值
     */
    protected float mYMax = -Float.MAX_VALUE;

    protected float mYMin = Float.MAX_VALUE;


    protected float mXMax = -Float.MAX_VALUE;


    protected float mXMin = Float.MAX_VALUE;


    protected List<T> mDataSets;

    public ChartData() {
        mDataSets = new ArrayList<T>();
    }

    public ChartData(List<T> sets) {
        this.mDataSets = sets;
        notifyDataChanged();
        Log.e("BarChart","0.创建ChartData，调用notifyDataChanged，计算最大值和最小值，maxXValue："+getXMax()+",minXValue:"+getXMin()
        +",maxYValue:"+getYMax()+",minYValue:"+getYMin());
    }

    public ChartData(T... dataSets) {
        mDataSets = arrayToList(dataSets);
        notifyDataChanged();
    }


    private List<T> arrayToList(T[] array) {

        List<T> list = new ArrayList<>();

        for (T set : array) {
            list.add(set);
        }

        return list;
    }

    public void notifyDataChanged() {
        calcMinMax();
    }


    protected void calcMinMax(){

        if(mDataSets == null){
            return;
        }

        mYMax = -Float.MAX_VALUE;
        mYMin = Float.MAX_VALUE;
        mXMax = -Float.MAX_VALUE;
        mXMin = Float.MAX_VALUE;

        for (T set : mDataSets) {
            calcMinMax(set);
        }

    }

    protected void calcMinMax(T d) {

        if (mYMax < d.getYMax())
            mYMax = d.getYMax();
        if (mYMin > d.getYMin())
            mYMin = d.getYMin();

        if (mXMax < d.getXMax())
            mXMax = d.getXMax();
        if (mXMin > d.getXMin())
            mXMin = d.getXMin();
    }


    public int getDataSetCount() {
        if (mDataSets == null)
            return 0;
        return mDataSets.size();
    }


    public float getYMin() {
        return mYMin;
    }

    public float getYMax() {
        return mYMax;
    }

    public float getXMin() {
        return mXMin;
    }

    public float getXMax() {
        return mXMax;
    }

    public List<T> getDataSets() {
        return mDataSets;
    }

    public void addDataSet(T d) {

        if (d == null)
            return;

        calcMinMax(d);

        mDataSets.add(d);
    }

    public boolean removeDataSet(T d) {

        if (d == null)
            return false;

        boolean removed = mDataSets.remove(d);

        // if a DataSet was removed
        if (removed) {
            calcMinMax();
        }

        return removed;
    }

    public boolean removeDataSet(int index) {

        if (index >= mDataSets.size() || index < 0)
            return false;

        T set = mDataSets.get(index);
        return removeDataSet(set);
    }

    public void addEntry(Entry e, int dataSetIndex) {

        if (mDataSets.size() > dataSetIndex && dataSetIndex >= 0) {

            IDataSet set = mDataSets.get(dataSetIndex);
            // add the entry to the dataset
            if (!set.addEntry(e))
                return;

        } else {
            Log.e("addEntry", "Cannot add Entry because dataSetIndex too high or too low.");
        }
    }


    public boolean removeEntry(Entry e, int dataSetIndex) {

        // entry null, outofbounds
        if (e == null || dataSetIndex >= mDataSets.size())
            return false;

        IDataSet set = mDataSets.get(dataSetIndex);

        if (set != null) {
            // remove the entry from the dataset
            boolean removed = set.removeEntry(e);

            if (removed) {
                calcMinMax();
            }

            return removed;
        } else
            return false;
    }

    public T getDataSetForEntry(Entry e) {

        if (e == null)
            return null;

        for (int i = 0; i < mDataSets.size(); i++) {

            T set = mDataSets.get(i);

            for (int j = 0; j < set.getEntryCount(); j++) {
                if (e.equalTo(set.getEntryForXValue(e.getX(), e.getY())))
                    return set;
            }
        }

        return null;
    }


    public int[] getColors() {

        if (mDataSets == null)
            return null;

        int clrcnt = 0;

        for (int i = 0; i < mDataSets.size(); i++) {
            clrcnt += mDataSets.get(i).getColors().size();
        }

        int[] colors = new int[clrcnt];
        int cnt = 0;

        for (int i = 0; i < mDataSets.size(); i++) {

            List<Integer> clrs = mDataSets.get(i).getColors();

            for (Integer clr : clrs) {
                colors[cnt] = clr;
                cnt++;
            }
        }

        return colors;
    }


    public int getIndexOfDataSet(T dataSet) {
        return mDataSets.indexOf(dataSet);
    }

    public void setValueFormatter(IValueFormatter f) {
        if (f == null)
            return;
        else {
            for (IDataSet set : mDataSets) {
                set.setValueFormatter(f);
            }
        }
    }

    public void setValueTextColor(int color) {
        for (IDataSet set : mDataSets) {
            set.setValueTextColor(color);
        }
    }

    public void setValueTextColors(List<Integer> colors) {
        for (IDataSet set : mDataSets) {
            set.setValueTextColors(colors);
        }
    }

    public void setValueTextSize(float size) {
        for (IDataSet set : mDataSets) {
            set.setValueTextSize(size);
        }
    }

    public void setDrawValues(boolean enabled) {
        for (IDataSet set : mDataSets) {
            set.setDrawValues(enabled);
        }
    }




    //TODO
//    public Entry getEntryForHighlight(Highlight highlight)

//    public void setHighlightEnabled(boolean enabled)

//    public boolean isHighlightEnabled()

    public void clearValues() {
        if (mDataSets != null) {
            mDataSets.clear();
        }
        notifyDataChanged();
    }

    public boolean contains(T dataSet) {

        for (T set : mDataSets) {
            if (set.equals(dataSet))
                return true;
        }

        return false;
    }

    public int getEntryCount() {

        int count = 0;

        for (T set : mDataSets) {
            count += set.getEntryCount();
        }

        return count;
    }

    public T getMaxEntryCountSet() {

        if (mDataSets == null || mDataSets.isEmpty())
            return null;

        T max = mDataSets.get(0);

        for (T set : mDataSets) {

            if (set.getEntryCount() > max.getEntryCount())
                max = set;
        }

        return max;
    }

}
