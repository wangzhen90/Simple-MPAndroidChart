package com.wangzhen.simplechartlib.data.dataSet;

import com.wangzhen.simplechartlib.data.entry.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhen on 2018/3/13.
 */

public abstract class DataSet<T extends Entry> extends BaseDataSet<T> {
    /**
     * 所有的entry
     */
    protected List<T> mValues = null;

    protected float mYMax = -Float.MAX_VALUE;

    protected float mYMin = Float.MAX_VALUE;

    protected float mXMax = -Float.MAX_VALUE;

    protected float mXMin = Float.MAX_VALUE;

    public String dataSetName = "";

    public DataSet(List<T> values){
        super();
        this.mValues = values;

        if(mValues == null){
            mValues = new ArrayList<>();
        }

        calcMinMax();

    }

    /**
     * ==================================cal max min start==========================================
     */
    @Override
    public void calcMinMax() {

        if (mValues == null || mValues.isEmpty())
            return;

        mYMax = -Float.MAX_VALUE;
        mYMin = Float.MAX_VALUE;
        mXMax = -Float.MAX_VALUE;
        mXMin = Float.MAX_VALUE;

        for (T e : mValues) {
            calcMinMax(e);
        }
    }

    protected void calcMinMax(T e) {

        if (e == null)
            return;

        calcMinMaxX(e);

        calcMinMaxY(e);
    }

    protected void calcMinMaxX(T e) {

        if (e.getX() < mXMin)
            mXMin = e.getX();

        if (e.getX() > mXMax)
            mXMax = e.getX();
    }

    protected void calcMinMaxY(T e) {

        if (e.getY() < mYMin)
            mYMin = e.getY();

        if (e.getY() > mYMax)
            mYMax = e.getY();
    }


    @Override
    public void calcMinMaxY(float fromX, float toX) {

        if(mValues == null && mValues.size() == 0){
            return;
        }

        mYMax = -Float.MAX_VALUE;
        mYMin = -Float.MAX_VALUE;

        int indexFrom = getEntryIndex(fromX, Float.NaN, Rounding.DOWN);
        int indexTo = getEntryIndex(toX, Float.NaN, Rounding.UP);


        for (int i = indexFrom; i <= indexTo; i++) {
            calcMinMaxY(mValues.get(i));
        }

    }




    /**
     * 二分查找 最接近xValue 的 index
     */
    @Override
    public int getEntryIndex(float xValue, float closestToY, Rounding rounding) {

        if (mValues == null || mValues.isEmpty())
            return -1;


        int low = 0;
        int high= mValues.size() - 1;

        int closest = high;

        while(low < high){

            int m = (low + high)/2;

            final float d1 = mValues.get(m).getX() - xValue,
                    d2 = mValues.get(m+1).getX() - xValue,
                    ad1 = Math.abs(d1),ad2 = Math.abs(d2);

            if(ad2 < ad1){
                low = m;

            }else if(ad2 > ad1){
                high = m;
            }else{
                if(d1 >= 0){
                    high = m;
                }else if(d1 < 0.0){
                    low = m+1;
                }
            }
            closest = high;
        }

        if(closest != -1){
            /**
             * entry 的x坐标值
             */
            float closestXValue = mValues.get(closest).getX();

            if(rounding == Rounding.UP){
                if(closestXValue < xValue && closest < mValues.size() -1){
                    closest++;
                }

            }else if(rounding == Rounding.DOWN){
                if (closestXValue > xValue && closest > 0) {
                    --closest;
                }
            }
            //TODO 这段逻辑暂时没看
            if (!Float.isNaN(closestToY)) {
                while (closest > 0 && mValues.get(closest - 1).getX() == closestXValue)
                    closest -= 1;

                float closestYValue = mValues.get(closest).getY();
                int closestYIndex = closest;

                while (true) {
                    closest += 1;
                    if (closest >= mValues.size())
                        break;

                    final Entry value = mValues.get(closest);

                    if (value.getX() != closestXValue)
                        break;

                    if (Math.abs(value.getY() - closestToY) < Math.abs(closestYValue - closestToY)) {
                        closestYValue = closestToY;
                        closestYIndex = closest;
                    }
                }

                closest = closestYIndex;
            }
        }

        return closest;
    }


    /**
     * 四舍五入的类型
     */

    public enum Rounding {
        UP,
        DOWN,
        CLOSEST,
    }


    /**
     * ==================================cal max min end==========================================
     */

    @Override
    public int getEntryCount() {
        return mValues.size();
    }

    public List<T> getValues() {
        return mValues;
    }

    /**
     * 重新设置values，需要调用notifyDataSetChanged，重新计算最大最小值等
     * @param values
     */
    public void setValues(List<T> values) {
        mValues = values;
        notifyDataSetChanged();
    }


    public abstract DataSet<T> copy();

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(toSimpleString());
        for (int i = 0; i < mValues.size(); i++) {
            buffer.append(mValues.get(i).toString() + " ");
        }
        return buffer.toString();
    }

    public String toSimpleString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("DataSet, label: " +", entries: " + mValues.size() +
                "\n");
        return buffer.toString();
    }

    @Override
    public float getYMin() {
        return mYMin;
    }

    @Override
    public float getYMax() {
        return mYMax;
    }

    @Override
    public float getXMin() {
        return mXMin;
    }

    @Override
    public float getXMax() {
        return mXMax;
    }



    @Override
    public void clear() {
        mValues.clear();
        notifyDataSetChanged();
    }

    @Override
    public boolean addEntry(T e) {

        if (e == null)
            return false;

        List<T> values = getValues();
        if (values == null) {
            values = new ArrayList<T>();
        }

        calcMinMax(e);

        // add the entry
        return values.add(e);
    }

    @Override
    public boolean removeEntry(T e) {

        if (e == null)
            return false;

        if (mValues == null)
            return false;

        // remove the entry
        boolean removed = mValues.remove(e);

        if (removed) {
            calcMinMax();
        }

        return removed;
    }


    @Override
    public int getEntryIndex(Entry e) {
        return mValues.indexOf(e);
    }


    @Override
    public T getEntryForXValue(float xValue, float closestToY, Rounding rounding) {

        int index = getEntryIndex(xValue, closestToY, rounding);
        if (index > -1)
            return mValues.get(index);
        return null;
    }

    @Override
    public T getEntryForXValue(float xValue, float closestToY) {
        return getEntryForXValue(xValue, closestToY, Rounding.CLOSEST);
    }


    @Override
    public T getEntryForIndex(int index) {
        return mValues.get(index);
    }

    /**
     * 获取xValue对应的Entries，如果chart过于密集，同一个xValue会存在多个entry，xValue代表的坐标轴上的像素点的value，不是index
     * @param xValue
     * @return
     */
    @Override
    public List<T> getEntriesForXValue(float xValue) {

        List<T> entries = new ArrayList<T>();

        int low = 0;
        int high = mValues.size() - 1;

        while (low <= high) {
            int m = (high + low) / 2;
            T entry = mValues.get(m);

            // if we have a match
            if (xValue == entry.getX()) {
                while (m > 0 && mValues.get(m - 1).getX() == xValue)
                    m--;

                high = mValues.size();

                // loop over all "equal" entries
                for (; m < high; m++) {
                    entry = mValues.get(m);
                    if (entry.getX() == xValue) {
                        entries.add(entry);
                    } else {
                        break;
                    }
                }

                break;
            } else {
                if (xValue > entry.getX())
                    low = m + 1;
                else
                    high = m - 1;
            }
        }

        return entries;
    }


}
