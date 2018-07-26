package com.wangzhen.tableChart.component;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;


import com.wangzhen.tableChart.data.Cell;
import com.wangzhen.tableChart.data.CellType;
import com.wangzhen.tableChart.data.Column;
import com.wangzhen.tableChart.data.EmptyCell;
import com.wangzhen.tableChart.data.Sheet;
import com.wangzhen.tableChart.highlight.Highlight;
import com.wangzhen.tableChart.interfaces.ICell;
import com.wangzhen.tableChart.interfaces.ISheet;
import com.wangzhen.tableChart.interfaces.ITableOnClickListener;
import com.wangzhen.tableChart.listener.ChartTouchListener;
import com.wangzhen.tableChart.renderder.DataRenderer;
import com.wangzhen.tableChart.renderder.SimpleRenderer;
import com.wangzhen.tableChart.utils.Transformer;
import com.wangzhen.tableChart.utils.Utils;
import com.wangzhen.tableChart.utils.ViewPortHandler;

import java.util.List;

/**
 * Created by wangzhen on 2018/6/28.
 */

public class TableChart extends ViewGroup {

    private ISheet sheet;

    protected DataRenderer mDataRenderer;

    protected ViewPortHandler mViewPortHandler = new ViewPortHandler();

    protected Transformer mTransformer;

    private boolean dragEnable = true;
    private boolean scaleXEnable = true;
    private boolean scaleYEnable = true;

    private boolean mDragXEnabled = true;
    private boolean mDragYEnabled = true;

    private boolean mDragOnlySigleDirection = true;

    protected boolean mPinchZoomEnabled = true;

    protected ChartTouchListener mChartTouchListener;

    protected boolean mTouchEnable = true;
    //惯性减速系数
    private float mDragDecelerationFrictionCoef = 0.9f;

    private boolean isTitleFixed = true;

    private int titleFontSize = 9;
    private int contentFontSize = 9;

    private float highlightBorderWidth = Utils.convertDpToPixel(5f);


    private Highlight mHighlight;

    private ITableOnClickListener onClickListener;


    public TableChart(Context context) {
        super(context);
        init();
    }

    public TableChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TableChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setWillNotDraw(false);

        Utils.init(this.getContext());
        mDataRenderer = new SimpleRenderer(mViewPortHandler, this);
        mTransformer = new Transformer(mViewPortHandler);


        mChartTouchListener = new ChartTouchListener(this, mViewPortHandler.getMatrixTouch(), 5f);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        if (sheet == null)
            return;

        if (mDataRenderer != null) {
            mDataRenderer.drawHighlighted(canvas,mHighlight);

            mDataRenderer.drawTitle(canvas);
            mDataRenderer.drawData(canvas);
        }
    }

    public void setSheet(ISheet sheet) {
        this.sheet = sheet;
        mViewPortHandler.setMaxTransY(sheet.getHeight());
        mViewPortHandler.setMaxTransX(sheet.getWidth());

    }

    public ISheet getSheet(){
        return sheet;
    }


    public void notifyDataSetChanged() {

        if (mDataRenderer != null) {
            mDataRenderer.initBuffers();
        }

        calcMinMax();


    }

    protected void calcMinMax() {

        prepareOffsetMatrix();
    }


    public void calculateOffsets() {


    }

    protected void prepareOffsetMatrix() {

        //TODO 表格的默认scaleX = 1, scaleY = 1,这个地方只需要做偏移量的处理
    }
//
//
//    protected void prepareValuePxMatrix() {
//
//
//    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        if (w > 0 && h > 0 && w < 10000 && h < 10000) {

            mViewPortHandler.setChartDimens(w, h);
        }

        notifyDataSetChanged();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = (int) Utils.convertDpToPixel(50f);
        setMeasuredDimension(
                Math.max(getSuggestedMinimumWidth(),
                        resolveSize(size,
                                widthMeasureSpec)),
                Math.max(getSuggestedMinimumHeight(),
                        resolveSize(size,
                                heightMeasureSpec)));
    }

    public Transformer getTransformer() {
        return mTransformer;
    }

    public int getColumnCount() {

        return sheet.getColumns();
    }

    public List<Column> getColumnList() {

        return sheet.getColumnList();
    }

    public boolean isScaleXEnabled() {
        return scaleXEnable;
    }

    public boolean isScaleYEnabled() {
        return scaleYEnable;
    }

    public void disableScroll() {

        ViewParent parent = getParent();

        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }

    }

    public void setPinchZoom(boolean enabled) {
        mPinchZoomEnabled = enabled;
    }

    public boolean isPinchZoomEnabled() {
        return mPinchZoomEnabled;
    }

    public void setDragEnabled(boolean enabled) {
        this.mDragXEnabled = enabled;
        this.mDragYEnabled = enabled;
    }

    public boolean isDragEnabled() {
        return mDragXEnabled || mDragYEnabled;
    }

    public void setDragXEnabled(boolean enabled) {
        this.mDragXEnabled = enabled;
    }

    public boolean isDragXEnabled() {
        return mDragXEnabled;
    }

    public void setDragYEnabled(boolean enabled) {
        this.mDragYEnabled = enabled;
    }

    public boolean isDragYEnabled() {
        return mDragYEnabled;
    }

    public void enableScroll() {
        ViewParent parent = getParent();
        if (parent != null)
            parent.requestDisallowInterceptTouchEvent(false);
    }

    public void setTouchEnable(boolean touchEnable) {
        this.mTouchEnable = touchEnable;
    }

    public ViewPortHandler getViewPortHandler() {
        return mViewPortHandler;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (mChartTouchListener == null && sheet == null) {
            return false;
        }

        if (!mTouchEnable) {
            return false;
        } else {
            return mChartTouchListener.onTouch(this, event);
        }
    }

    public float getDragDecelerationFrictionCoef() {
        return mDragDecelerationFrictionCoef;
    }

    public void setDragDecelerationFrictionCoef(float newValue) {

        if (newValue < 0.f)
            newValue = 0.f;

        if (newValue >= 1f)
            newValue = 0.999f;

        mDragDecelerationFrictionCoef = newValue;
    }

    @Override
    public void computeScroll() {
        if (mChartTouchListener != null) {
            mChartTouchListener.computeScroll();
        }
    }

    public int getTitleHeight() {
        return ((Sheet) sheet).getTitleHeight();
    }

    public boolean isTitleFixed() {
        return isTitleFixed;
    }

    public void setTitleFixed(boolean titleFixed) {
        isTitleFixed = titleFixed;
    }

    public boolean hasNoDragOffset() {
        return mViewPortHandler.hasNoDragOffset();
    }

    public boolean isDragOnlySigleDirection() {
        return mDragOnlySigleDirection;
    }

    public void setDragOnlySigleDirection(boolean mDragOnlySigleDirection) {
        this.mDragOnlySigleDirection = mDragOnlySigleDirection;
    }


    public Column getColumnByXValue(double xValue) {

        return sheet.getColumnByXValue(xValue);
    }


    public ICell getCellByTouchPoint(double xValue, double yValue) {

        ICell virtualCell = sheet.getCellByTouchPoint(xValue, yValue);

        if(virtualCell != null){
            if(virtualCell.getType() == CellType.EMPTY){
                return ((EmptyCell)virtualCell).getRealCell();
            }else{
                return virtualCell;
            }
        }


        return sheet.getCellByTouchPoint(xValue, yValue);
    }

    public int getTitleFontSize() {
        return titleFontSize;
    }

    public int getContentFontSize() {
        return contentFontSize;
    }

    public void setTitleFontSize(int fontSize) {
        this.titleFontSize = fontSize;
    }

    public void setContentFontSize(int fontSize) {
        this.contentFontSize = fontSize;
    }

    public void highlightValue(Highlight h, boolean callListener) {

        mHighlight = h;

        if(h == null) return;

        if(callListener && onClickListener != null){
            if(h.isTitle())
                onClickListener.onColumnClick(h.getColumnData());
            else
                onClickListener.onCellClick(h.getCell());
        }


        invalidate();

    }


    public Highlight getHighlight() {
        return mHighlight;
    }

    public int getContentHeight(){

        return sheet.getHeight();
    }

    public float getHighlightBorderWidth() {
        return highlightBorderWidth;
    }

    public void setHighlightBorderWidth(float highlightBorderWidth) {
        this.highlightBorderWidth = highlightBorderWidth;
    }

    public ITableOnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(ITableOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
