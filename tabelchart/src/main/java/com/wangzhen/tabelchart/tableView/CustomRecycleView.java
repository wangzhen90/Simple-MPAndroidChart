package com.wangzhen.tabelchart.tableView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by wangzhen on 2017/11/27.
 */

public class CustomRecycleView extends RecyclerView {

    CustomHorizontalScrollView horizontalScrollView;

    public CustomRecycleView(Context context) {
        super(context);
    }

    public CustomRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setHorizontalScrollView(CustomHorizontalScrollView horizontalScrollView){
        this.horizontalScrollView = horizontalScrollView;

    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if(this.horizontalScrollView != null)
        this.horizontalScrollView.scrollBy(dx,dy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

//        getRefreshView(this);
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_MOVE:
//                if (swipeLayout != null && swipeLayout.isEnabled()) {
//                    swipeLayout.setEnabled(false);
//                }
//                break;
//
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                if (swipeLayout != null && !swipeLayout.isEnabled()) {
//                    swipeLayout.setEnabled(true);
//                }
//                break;
//        }


        return super.onTouchEvent(ev);
    }

    private View swipeLayout;

    private void getRefreshView(View childView) {

        if (swipeLayout != null) {
            return;
        }

        View parent = (View) childView.getParent();

        if (parent != null) {
            Log.e("========", parent.getClass().getSimpleName());

//            if("ReactScrollView".equals(parent.getClass().getSimpleName())){
//                scrollView = parent;
//            }

            if ("ReactSwipeRefreshLayout".equals(parent.getClass().getSimpleName())) {
                swipeLayout = parent;
                Log.e("========", "find refresh view");
            } else {
                getRefreshView(parent);
            }
        }

    }

}
