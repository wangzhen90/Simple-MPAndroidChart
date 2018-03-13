package com.wangzhen.simplechartlib.data.entry;

/**
 * Created by wangzhen on 2018/3/12.
 */

public class Entry {

    private float y = 0f;
    private float x = 0f;

    private Object mData = null;


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


}
