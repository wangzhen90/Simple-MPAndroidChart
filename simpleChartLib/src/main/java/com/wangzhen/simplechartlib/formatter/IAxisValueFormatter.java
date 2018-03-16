package com.wangzhen.simplechartlib.formatter;

import com.wangzhen.simplechartlib.component.AxisBase;

/**
 * Created by wangzhen on 2018/3/16.
 */

public interface IAxisValueFormatter {

    /**
     *在绘制之前格式化这个值，考虑到性能的原因，不要过多的调用
     * @param value
     * @param axis
     * @return
     */
    String getFormattedValue(float value, AxisBase axis);

}
