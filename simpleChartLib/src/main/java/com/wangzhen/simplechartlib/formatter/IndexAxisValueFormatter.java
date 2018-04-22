package com.wangzhen.simplechartlib.formatter;

import com.wangzhen.simplechartlib.component.AxisBase;

/**
 * Created by wangzhen on 2018/4/22.
 */

public class IndexAxisValueFormatter implements IAxisValueFormatter {

    private String[] mValues = new String[] {};
    private int mValueCount = 0;

    public IndexAxisValueFormatter(String[] values) {
        if (values != null)
            setValues(values);
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int index = Math.round(value);

        if (index < 0 || index >= mValueCount || index != (int)value)
            return "";

        return mValues[index];
    }

    public String[] getValues()
    {
        return mValues;
    }

    public void setValues(String[] values)
    {
        if (values == null)
            values = new String[] {};

        this.mValues = values;
        this.mValueCount = values.length;
    }
}
