package com.wangzhen.simplechartlib.tableChart.renderder;

import com.wangzhen.simplechartlib.utils.ViewPortHandler;

/**
 * Created by wangzhen on 2018/3/20.
 */

public abstract class Renderer {
    protected ViewPortHandler mViewPortHandler;

    public Renderer(ViewPortHandler viewPortHandler){
        this.mViewPortHandler = viewPortHandler;
    }

}
