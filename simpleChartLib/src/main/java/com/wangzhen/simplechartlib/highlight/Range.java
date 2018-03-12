package com.wangzhen.simplechartlib.highlight;

/**
 * Created by wangzhen on 2018/3/12.
 * stack 的值如果是 -10，5，20  代表stack的区域是 -10到0，0-5，5-20
 */

public final class Range {

    public float from;
    public float to;

    public Range(float from, float to) {
        this.from = from;
        this.to = to;
    }

    public boolean contains(float value) {

        if (value > from && value <= to)
            return true;
        else
            return false;
    }

    public boolean isLarger(float value) {
        return value > to;
    }

    public boolean isSmaller(float value) {
        return value < from;
    }

}
