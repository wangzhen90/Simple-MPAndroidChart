package com.wangzhen.simplechartlib.component;

import com.wangzhen.simplechartlib.utils.Utils;

/**
 * Created by wangzhen on 2018/3/17.
 */

public class XAxis extends AxisBase {
    /**
     * label的宽度，单位是像素，是自动计算的
     */
    public int mLabelWidth = 1;

    public int mLabelHeight = 1;

    /**
     * 旋转后的label的宽高
     */
    public int mLabelRotatedWidth =1;
    public int mLabelRotatedHeight = 1;

    protected float mLabelRotationAngle = 0f;

    /**
     * 第一个或者最后一个label是否要被切割掉，比如柱状图左右两侧的柱子已经部分移出了屏幕
     *
     */

    private boolean mAvoidFirstLastClipping = false;


    public XAxis() {
        super();

        mYOffset = Utils.convertDpToPixel(4.f); // -3
    }

    public float getLabelRotationAngle() {
        return mLabelRotationAngle;
    }

    public void setLabelRotationAngle(float angle) {
        mLabelRotationAngle = angle;
    }


    public void setAvoidFirstLastClipping(boolean enabled) {
        mAvoidFirstLastClipping = enabled;
    }

    public boolean isAvoidFirstLastClippingEnabled() {
        return mAvoidFirstLastClipping;
    }


}
