package com.wangzhen.simplechartlib.formatter;

import com.wangzhen.simplechartlib.component.AxisBase;

import java.text.DecimalFormat;

/**
 * Created by wangzhen on 2018/3/16.
 */

public class DefaultAxisValueFormatter implements IAxisValueFormatter {


    protected DecimalFormat mFormat;

    protected int digits = 0;


    /**
     * 格式化精确到几位小数
     * @param digits
     */
    public DefaultAxisValueFormatter(int digits) {
        this.digits = digits;

        StringBuffer b = new StringBuffer();
        for (int i = 0; i < digits; i++) {
            if (i == 0)
                b.append(".");
            b.append("0");
        }

        mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value);
    }


    public int getDecimalDigits(){
        return  digits;
    }
}
