package com.wangzhen.simplechartlib.data;


import com.wangzhen.simplechartlib.highlight.Range;

/**
 * Created by wangzhen on 2018/3/12.
 */

public class BarEntry extends Entry {


    /**
     * 只有堆叠图才有值
     */
    private float[] mYVals;

    private Range[] mRanges;



    public float[] getYVals() {
        return mYVals;
    }

    public void setYVals(float[] mYVals) {
        this.mYVals = mYVals;
    }

    public BarEntry(float x, float[] vals, Object data) {
        super(x, calcSum(vals), data);

        this.mYVals = vals;
        calcPosNegSum();
        calcRanges();
    }


    /**
     * 计算堆叠之和
     * @param vals
     * @return
     */
    private static float calcSum(float[] vals) {

        if (vals == null)
            return 0f;

        float sum = 0f;

        for (float f : vals)
            sum += f;

        return sum;
    }

    protected void calcRanges(){

        float[] values = getYVals();

        if(values == null || values.length == 0){
            return;
        }


        //TODO



    }



}
