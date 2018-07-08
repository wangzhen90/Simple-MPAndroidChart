package com.wangzhen.simplechartlib.tableChart.listener;

import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.wangzhen.simplechartlib.tableChart.component.TableChart;
import com.wangzhen.simplechartlib.utils.MPPointF;
import com.wangzhen.simplechartlib.utils.ViewPortHandler;

/**
 * Created by wangzhen on 2018/7/8.
 */

public class ChartTouchLisener implements View.OnTouchListener {


    public enum ChartGesture {
        NONE, DRAG, X_ZOOM, Y_ZOOM, PINCH_ZOOM, ROTATE, SINGLE_TAP, DOUBLE_TAP, LONG_PRESS, FLING
    }

    protected ChartGesture mLastGesture = ChartGesture.NONE;

    // states
    protected static final int NONE = 0;
    protected static final int DRAG = 1;
    protected static final int X_ZOOM = 2;
    protected static final int Y_ZOOM = 3;
    protected static final int PINCH_ZOOM = 4;
    protected static final int POST_ZOOM = 5;
    protected static final int ROTATE = 6;

    //chart原有的matrix
    private Matrix mOriginMatrix = new Matrix();
    //记录chart原有的matrix的matrix
    private Matrix mSavedMatrix = new Matrix();

    //开始的点
    private MPPointF mTouchStartPoint = MPPointF.getInstance(0,0);
    //双指间的中心点
    private MPPointF mTouchPointCenter = MPPointF.getInstance(0,0);

    private float mSavedXDist = 1f;
    private float mSavedYDist = 1f;
    private float mSavedDist = 1f;


    protected int mCurrentTouchMode = NONE;

    //速度跟踪器
    private VelocityTracker mVelocityTracker;

    //减速时间
    private long mDecelerationLastTime = 0;
    private MPPointF mDecelerationCurrentPoint = MPPointF.getInstance(0,0);
    private MPPointF mDecelerationVelocity = MPPointF.getInstance(0,0);

    protected TableChart mChart;

    //拖拽的触发位移
    private float mDragTriggerDist;
    //缩放的触发位移
    private float mMinScalePointerDistance;

    public ChartTouchLisener(TableChart chart,Matrix touchMatrix,float dragTiggerDistance){

        this.mChart = chart;
        this.mOriginMatrix = touchMatrix;
        this.mDragTriggerDist = dragTiggerDistance;

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        //初始化速度跟踪器
        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);

        if(event.getActionMasked() == MotionEvent.ACTION_CANCEL){
            if(mVelocityTracker != null){
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
        }

        if (!mChart.isDragEnabled() && (!mChart.isScaleXEnabled() && !mChart.isScaleYEnabled()))
            return true;



        switch (event.getAction() & MotionEvent.ACTION_MASK){

            case MotionEvent.ACTION_DOWN:

                stopDeceleration();
                saveTouchStart(event);

                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                if(event.getPointerCount() >= 2){

                    mChart.disableScroll();

                    saveTouchStart(event);



                }

                break;


            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_UP:


                break;


            case MotionEvent.ACTION_POINTER_UP:

                break;

            case MotionEvent.ACTION_CANCEL:

                break;


        }

        return false;
    }


    public void stopDeceleration() {
        mDecelerationVelocity.x = 0;
        mDecelerationVelocity.y = 0;
    }

    private void saveTouchStart(MotionEvent event) {

        mSavedMatrix.set(mOriginMatrix);

        mTouchStartPoint.x = event.getX();
        mTouchStartPoint.y = event.getY();

        //TODO
//        mClosestDataSetToTouch = mChart.getDataSetByTouchPoint(event.getX(), event.getY());


    }


}
