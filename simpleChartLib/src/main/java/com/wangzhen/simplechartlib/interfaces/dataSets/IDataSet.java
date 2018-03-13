package com.wangzhen.simplechartlib.interfaces.dataSets;

import com.wangzhen.simplechartlib.data.entry.Entry;

/**
 * Created by wangzhen on 2018/3/13.
 */

public interface IDataSet<T extends Entry> {




    /**
     * 是否展示这一组数据
     * @return
     */
    boolean isVisible();

    void setVisible(boolean visible);



}
