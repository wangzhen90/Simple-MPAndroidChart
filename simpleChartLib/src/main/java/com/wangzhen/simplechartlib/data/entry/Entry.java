package com.wangzhen.simplechartlib.data.entry;

import com.wangzhen.simplechartlib.utils.Utils;

/**
 * Created by wangzhen on 2018/3/12.
 */

public class Entry {

    private float y = 0f;
    private float x = 0f;

     Object mData = null;


    public Entry(){

    }

    public Entry(float x,float y){
        this.x = x;
        this.y = y;
    }

    public Entry(float x,float y,Object data){
        this.x = x;
        this.y = y;
        this.mData = data;
    }


    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    /**
     * returns a string representation of the entry containing x-index and value
     */
    @Override
    public String toString() {
        return "Entry, x: " + x + " y: " + getY();
    }

    public Object getData() {
        return mData;
    }

    public void setData(Object data) {
        this.mData = data;
    }


    public boolean equalTo(Entry e) {

        if (e == null)
            return false;

        if (e.getData() != this.getData())
            return false;

        if (Math.abs(e.x - this.x) > Utils.FLOAT_EPSILON)
            return false;

        if (Math.abs(e.getY() - this.getY()) > Utils.FLOAT_EPSILON)
            return false;

        return true;
    }
}
