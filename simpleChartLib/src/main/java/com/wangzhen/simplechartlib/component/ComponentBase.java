package com.wangzhen.simplechartlib.component;

import android.graphics.Color;

import com.wangzhen.simplechartlib.utils.Utils;

/**
 * Created by wangzhen on 2018/3/16.
 */

public abstract class ComponentBase {


    protected boolean mEnabled = true;


    protected float mXOffset = 5f;

    protected float mYOffset = 5f;

    /**
     * label的字体大小
     */
    protected float mTextSize = Utils.convertDpToPixel(10f);

    protected int mTextColor = Color.BLACK;

    public ComponentBase() {

    }


    public float getXOffset() {
        return mXOffset;
    }

    public void setXOffset(float xOffset) {
        mXOffset = Utils.convertDpToPixel(xOffset);
    }

    public float getYOffset() {
        return mYOffset;
    }

    public void setYOffset(float yOffset) {
        mYOffset = Utils.convertDpToPixel(yOffset);
    }


    public void setTextSize(float size) {

        if (size > 24f)
            size = 24f;
        if (size < 6f)
            size = 6f;

        mTextSize = Utils.convertDpToPixel(size);
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextColor(int color) {
        mTextColor = color;
    }

    public int getTextColor() {
        return mTextColor;
    }


    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }


    public boolean isEnabled() {
        return mEnabled;
    }



}
