package com.wangzhen.simplechartlib.listener;

import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

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
                        performZoom(event);
                    }


                } else if (mTouchMode == NONE && Math.abs(distance(event.getX(), mTouchStartPoint.x, event.getY(),
                        mTouchStartPoint.y)) > mDragTriggerDist) {

                    if(mChart.isDragEnabled()){

                        //如果已经缩放到最小或者没有拖拽位移就弹出higelight 否则就什么都不做（因为就是你想拖动也滑不动了啊）
                        boolean shouldPan = !mChart.isFullyZoomedOut() ||
                                !mChart.hasNoDragOffset();

                        if (shouldPan) {

                            float distanceX = Math.abs(event.getX() - mTouchStartPoint.x);
                            float distanceY = Math.abs(event.getY() - mTouchStartPoint.y);

                            // Disable dragging in a direction that's disallowed
                            if ((mChart.isDragXEnabled() || distanceY >= distanceX) &&
                                    (mChart.isDragYEnabled() || distanceY <= distanceX)) {

                                mLastGesture = ChartGesture.DRAG;
                                mTouchMode = DRAG;
                            }

                        } else {
                            //TODO 弹框先不处理
//                            if (mChart.isHighlightPerDragEnabled()) {
//                                mLastGesture = ChartGesture.DRAG;
//
//                                if (mChart.isHighlightPerDragEnabled())
//                                    performHighlightDrag(event);
//                            }
                        }


                    }


                }
                break;
        }
        return false;
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
                mLastGesture = ChartGesture.PINCH_ZOOM;
                float scale = totalDist / mSavedDist;
                boolean isZoomingOut = (scale < 1);

                boolean canZoomMoreX = isZoomingOut ? h.canZoomOutMoreX() : h.canZoomInMoreX();
                boolean canZoomMoreY = isZoomingOut ? h.canZoomOutMoreY() : h.canZoomInMoreY();

                float scaleX = (mChart.isScaleXEnabled()) ? scale : 1f;
                float scaleY = (mChart.isScaleYEnabled()) ? scale : 1f;

                mMatrix.set(mSavedMatrix);
                //已中心点缩放
                mMatrix.postScale(scaleX, scaleY, t.x, t.y);

                if (l != null)
                    l.onChartScale(event, scaleX, scaleY);

            } else if (mTouchMode == X_ZOOM && mChart.isScaleXEnabled()) {
                mLastGesture = ChartGesture.X_ZOOM;

                float xDist = getXDist(event);
                float scaleX = xDist / mSavedXDist; // x-axis scale

                boolean isZoomingOut = (scaleX < 1);
                boolean canZoomMoreX = isZoomingOut ?
                        h.canZoomOutMoreX() :
                        h.canZoomInMoreX();

                if (canZoomMoreX) {

                    mMatrix.set(mSavedMatrix);
                    mMatrix.postScale(scaleX, 1f, t.x, t.y);

                    if (l != null)
                        l.onChartScale(event, scaleX, 1f);
                }


            } else if (mTouchMode == Y_ZOOM && mChart.isScaleYEnabled()) {


                float yDist = getYDist(event);

                float scaleY = yDist / mSavedYDist;

                boolean isZoomingOut = scaleY < 1;

                boolean canZoomMoreY = isZoomingOut ? h.canZoomOutMoreY() : h.canZoomInMoreY();

                if (canZoomMoreY) {
                    mMatrix.set(mSavedMatrix);
                    mMatrix.postScale(1f, scaleY, t.x, t.y);
                }

                if (l != null)
                    l.onChartScale(event, scaleY, 1f);


            }
            MPPointF.recycleInstance(t);
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


}
