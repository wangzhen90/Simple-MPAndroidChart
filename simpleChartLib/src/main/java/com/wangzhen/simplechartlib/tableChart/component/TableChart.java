package com.wangzhen.simplechartlib.tableChart.component;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.wangzhen.simplechartlib.tableChart.data.Cell;
import com.wangzhen.simplechartlib.tableChart.data.Column;
import com.wangzhen.simplechartlib.tableChart.data.Sheet;
import com.wangzhen.simplechartlib.tableChart.interfaces.ISheet;
import com.wangzhen.simplechartlib.tableChart.renderder.DataRenderer;
import com.wangzhen.simplechartlib.tableChart.renderder.SimpleRenderer;
import com.wangzhen.simplechartlib.utils.Transformer;
import com.wangzhen.simplechartlib.utils.Utils;
import com.wangzhen.simplechartlib.utils.ViewPortHandler;

import java.util.ArrayList;
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


    private void init(){
        setWillNotDraw(false);
        mDataRenderer = new SimpleRenderer(mViewPortHandler,this);
        mTransformer = new Transformer(mViewPortHandler);
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

        if(mDataRenderer != null){
            mDataRenderer.drawTitle(canvas);
            mDataRenderer.drawData(canvas);
        }


    }

    public void setSheet(ISheet sheet){
        this.sheet = sheet;
    }


    public void notifyDataSetChanged(){

        if(mDataRenderer != null){
            mDataRenderer.initBuffers();
        }

        calcMinMax();


    }

    protected void calcMinMax(){

        //TODO 貌似不需要
    }


    public void calculateOffsets() {



    }

//    protected void prepareOffsetMatrix() {
//
//
//    }
//
//
//    protected void prepareValuePxMatrix() {
//
//
//    }




        @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        if(w > 0 && h > 0 && w < 10000 && h < 10000){

            mViewPortHandler.setChartDimens(w,h);
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

    public int getColumnCount(){

        return sheet.getColumns();
    }

    public List<Column> getColumnList(){

        return sheet.getColumnList();
    }

    public boolean isDragEnabled(){

        return dragEnable;
    }

    public boolean isScaleXEnabled(){
        return  scaleXEnable;
    }

    public boolean isScaleYEnabled(){
        return scaleYEnable;
    }

    public void disableScroll() {

        ViewParent parent = getParent();

        if(parent != null){
            parent.requestDisallowInterceptTouchEvent(true);
        }

    }

}
