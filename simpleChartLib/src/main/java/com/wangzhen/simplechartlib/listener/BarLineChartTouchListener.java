package com.wangzhen.simplechartlib.listener;

import android.graphics.Matrix;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.wangzhen.simplechartlib.charts.BarLineChartBase;
import com.wangzhen.simplechartlib.data.chartData.BarLineScatterCandleBubbleData;
import com.wangzhen.simplechartlib.data.entry.Entry;
import com.wangzhen.simplechartlib.interfaces.dataSets.IBarLineScatterCandleBubbleDataSet;
import com.wangzhen.simplechartlib.interfaces.dataSets.IDataSet;
import com.wangzhen.simplechartlib.utils.MPPointF;
import com.wangzhen.simplechartlib.utils.Utils;
import com.wangzhen.simplechartlib.utils.ViewPortHandler;

/**
 * Created by wangzhen on 2018/4/18.
 */

public class BarLineChartTouchListener extends ChartTouchListener<BarLineChartBase<? extends BarLineScatterCandleBubbleData<?
        extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>>> {
    /**
     * the original touch-matrix from the chart
     */
    private Matrix mMatrix = new Matrix();

    /**
     * matrix for saving the original matrix state
     */
    private Matrix mSavedMatrix = new Matrix();
    /**
     * 点击起始点
     */
    private MPPointF mTouchStartPoint = MPPointF.getInstance(0, 0);
    /**
     * 两个点击点的中心点
     */
    private MPPointF mTouchPointCenter = MPPointF.getInstance(0, 0);
    /**
     * 两指间的x距离
     */
    private float mSavedXDist = 1f;
    /**
     * 两指间的y距离
     */
    private float mSavedYDist = 1f;
    /**
     * 两指间的距离
     */
    private float mSavedDist = 1f;
    /**
     * 拖拽最小触发距离
     */
    private float mDragTriggerDist;

    /**
     * 触发最小缩放的距离
     */
    private float mMinScalePointerDistance;

    /**
     * 点击点最接近的dataset
     */
    private IDataSet mClosestDataSetToTouch;

    private MPPointF mDecelerationVelocity = MPPointF.getInstance(0, 0);

    private VelocityTracker mVelocityTracker;


    public BarLineChartTouchListener(BarLineChartBase<? extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>> chart
            , Matrix touchMatrix, float dragTriggerDistance) {
        super(chart);
        this.mMatrix = touchMatrix;
        this.mDragTriggerDist = dragTriggerDistance;
        this.mMinScalePointerDistance = Utils.convertDpToPixel(3.5f);

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);

        if (event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
        }

        if (mTouchMode == NONE) {
            mGestureDetector.onTouchEvent(event);
        }

        if (!mChart.isDragEnabled() && (!mChart.isScaleXEnabled() && !mChart.isScaleYEnabled()))
            return true;


        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                startAction(event);

                stopDeceleration();

                saveTouchStart(event);

                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                if (event.getPointerCount() >= 2) {
                    mChart.disableScroll();

                    saveTouchStart(event);
                    mSavedXDist = getXDist(event);
                    mSavedYDist = getYDist(event);
                    mSavedDist = spacing(event);

                    if (mSavedDist > 10f) {
                        if (mChart.isPinchZoomEnabled()) {
                            mTouchMode = PINCH_ZOOM;
                        } else {
                            if (mChart.isScaleXEnabled() != mChart.isScaleYEnabled()) {
                                mTouchMode = mChart.isScaleXEnabled() ? X_ZOOM : Y_ZOOM;
                            } else {
                                mTouchMode = mSavedXDist > mSavedYDist ? X_ZOOM : Y_ZOOM;
                            }
                        }

                    }
                    //确定缩放中心点
                    midPoint(mTouchPointCenter, event);

                }

                break;

            case MotionEvent.ACTION_MOVE:

                if (mTouchMode == DRAG) {
                    mChart.disableScroll();

                    float x = mChart.isDragXEnabled() ? event.getX() - mTouchStartPoint.x : 0.f;
                    float y = mChart.isDragYEnabled() ? event.getY() - mTouchStartPoint.y : 0.f;

                    performDrag(event, x, y);


                } else if (mTouchMode == X_ZOOM || mTouchMode == Y_ZOOM || mTouchMode == PINCH_ZOOM) {
                    mChart.disableScroll();
                    if (mChart.isScaleXEnabled() || mChart.isScaleYEnabled()) {

                    }


                } else if (mTouchMode == NONE && Math.abs(distance(event.getX(), mTouchStartPoint.x, event.getY(),
                        mTouchStartPoint.y)) > mDragTriggerDist) {

                }


                break;


            case MotionEvent.ACTION_UP:

                final VelocityTracker  velocityTracker = mVelocityTracker;
                final int pointerId = event.getPointerId(0);
                //获取瞬时速度
                velocityTracker.computeCurrentVelocity(1000, Utils.getMaximumFlingVelocity());

                final float velocityX = velocityTracker.getXVelocity(pointerId);
                final float velocityY = velocityTracker.getYVelocity(pointerId);

                //TODO 惯性滑动，涉及到动画，先不处理
//                if (Math.abs(velocityX) > Utils.getMinimumFlingVelocity() ||
//                        Math.abs(velocityY) > Utils.getMinimumFlingVelocity()) {
//
//                    if (mTouchMode == DRAG && mChart.isDragDecelerationEnabled()) {
//
//                        stopDeceleration();
//
//                        mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis();
//
//                        mDecelerationCurrentPoint.x = event.getX();
//                        mDecelerationCurrentPoint.y = event.getY();
//
//                        mDecelerationVelocity.x = velocityX;
//                        mDecelerationVelocity.y = velocityY;
//
//                        Utils.postInvalidateOnAnimation(mChart); // This causes computeScroll to fire, recommended for this by
//                        // Google
//                    }
//                }

                if (mTouchMode == X_ZOOM ||
                        mTouchMode == Y_ZOOM ||
                        mTouchMode == PINCH_ZOOM ||
                        mTouchMode == POST_ZOOM) {

                    // Range might have changed, which means that Y-axis labels
                    // could have changed in size, affecting Y-axis size.
                    // So we need to recalculate offsets.
                    mChart.calculateOffsets();
                    mChart.postInvalidate();
                }


                mTouchMode = NONE;
                mChart.enableScroll();

                if(mVelocityTracker != null){
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }

                endAction(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Utils.velocityTrackerPointerUpCleanUpIfNecessary(event, mVelocityTracker);
                //TODO 未看懂
                mTouchMode = POST_ZOOM;

                break;

            case MotionEvent.ACTION_CANCEL:

                mTouchMode = NONE;
                endAction(event);

                break;


        }
        /**
         * 刷新界面
         */
        mMatrix = mChart.getViewPortHandler().refresh(mMatrix,mChart,false);

        return true;
    }

    private void performDrag(MotionEvent event, float distanceX, float distanceY) {

        mLastGesture = ChartGesture.DRAG;

        mMatrix.set(mSavedMatrix);

        OnChartGestureListener l = mChart.getOnChartGestureListener();

        mMatrix.postTranslate(distanceX, distanceY);

        if (l != null)
            l.onChartTranslate(event, distanceX, distanceY);

    }

    private void performZoom(MotionEvent event) {
        if (event.getPointerCount() >= 2) {

            OnChartGestureListener l = mChart.getOnChartGestureListener();
            float totalDist = spacing(event);
            //获得总共的移动
            MPPointF t = getTrans(mTouchPointCenter.x, mTouchPointCenter.y);

            ViewPortHandler h = mChart.getViewPortHandler();

            if (mTouchMode == PINCH_ZOOM) {

            }else if(mTouchMode == X_ZOOM && mChart.isScaleXEnabled()){

            }else if(mTouchMode == Y_ZOOM && mChart.isScaleYEnabled()){

            }

        }


    }

    public MPPointF getTrans(float x, float y) {

        ViewPortHandler vph = mChart.getViewPortHandler();

        float xTrans = x - vph.offsetLeft();
        float yTrans = -(mChart.getMeasuredHeight() - y - vph.offsetBottom());

        return MPPointF.getInstance(xTrans, yTrans);
    }


    public void stopDeceleration() {
        mDecelerationVelocity.x = 0;
        mDecelerationVelocity.y = 0;
    }

    private void saveTouchStart(MotionEvent event) {

        mSavedMatrix.set(mMatrix);
        mTouchStartPoint.x = event.getX();
        mTouchStartPoint.y = event.getY();

        //TODO 点击事件暂不处理
//        mClosestDataSetToTouch = mChart.getDataSetByTouchPoint(event.getX(), event.getY());
    }

    /**
     * 计算两指间X的距离
     *
     * @param e
     * @return
     */
    private static float getXDist(MotionEvent e) {
        float x = Math.abs(e.getX(0) - e.getX(1));
        return x;
    }

    /**
     * 计算两指间Y的距离
     *
     * @param e
     * @return
     */
    private static float getYDist(MotionEvent e) {
        float y = Math.abs(e.getY(0) - e.getY(1));
        return y;
    }

    private static float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private static void midPoint(MPPointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.x = (x / 2f);
        point.y = (y / 2f);
    }


    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //TODO highlight暂不处理
        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {

        mLastGesture = ChartGesture.DOUBLE_TAP;
        OnChartGestureListener l = mChart.getOnChartGestureListener();

        if(l != null){
            l.onChartDoubleTapped(e);
        }

        //双击缩放
        if(mChart.isDoubleTapToZoomEnabled() && mChart.getData().getEntryCount() > 0){
            /**
             * TODO 不知道是否理解的正确 应该是错了。。。。后面再看
             * 计算缩放的中心点，比如一个以（0，-n） n > 0 点作为缩放中心点，x，y缩放就只向一个方向缩放了
             * 现在的需求是沿着x轴正方向和y轴负方向进行缩放
             */
            MPPointF trans = getTrans(e.getX(),e.getY());
            mChart.zoom(mChart.isScaleXEnabled() ? 1.1f : 1f, mChart.isScaleYEnabled() ? 1.1f : 1f, trans.x, trans.y);

            MPPointF.recycleInstance(trans);
        }



        return super.onDoubleTap(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return super.onFling(e1, e2, velocityX, velocityY);
    }
}
