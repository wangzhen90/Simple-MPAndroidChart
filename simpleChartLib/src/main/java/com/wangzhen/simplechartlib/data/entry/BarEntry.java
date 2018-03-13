package com.wangzhen.simplechartlib.data.entry;


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

    private float mNegativeSum;

    private float mPositiveSum;

    public float[] getYVals() {
        return mYVals;
    }

    public void setYVals(float[] mYVals) {
        this.mYVals = mYVals;
    }


    public BarEntry(float x, float[] vals) {
        super(x, calcSum(vals));

        this.mYVals = vals;
        calcPosNegSum();
        calcRanges();

    }

    public BarEntry(float x, float[] vals, Object data) {
        super(x, calcSum(vals), data);

        this.mYVals = vals;
        calcPosNegSum();
        calcRanges();

    }

    public boolean isStacked() {
        return mYVals != null;
    }



    public float getNegativeSum() {
        return mNegativeSum;
    }

    public float getPositiveSum() {
        return mPositiveSum;
    }

    private void calcPosNegSum() {

        if (mYVals == null) {
            mNegativeSum = 0;
            mPositiveSum = 0;
            return;
        }

        float sumNeg = 0f;
        float sumPos = 0f;

        for (float f : mYVals) {
            if (f <= 0f)
                sumNeg += Math.abs(f);
            else
                sumPos += f;
        }

        mNegativeSum = sumNeg;
        mPositiveSum = sumPos;
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

    /**
     * stack 的值如果是 -10，5，20  代表stack的区域是 -10到0，0-5，5-20
     */
    protected void calcRanges(){

        float[] values = getYVals();

        if(values == null || values.length == 0){
            return;
        }

        mRanges = new Range[values.length];

        float negRemain = -getNegativeSum();
        float posRemain = 0f;


        for(int i = 0; i < values.length; i++){
            float value = values[i];
            if(value >= 0){
              mRanges[i] = new Range(value,value + posRemain);
              posRemain += value;

            }else{
                mRanges[i] = new Range(negRemain, negRemain - value);
                negRemain -= value;
            }
        }
    }
}
