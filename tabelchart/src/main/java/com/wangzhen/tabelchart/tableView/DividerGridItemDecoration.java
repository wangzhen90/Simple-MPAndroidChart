package com.wangzhen.tabelchart.tableView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

import com.wangzhen.tabelchart.R;


/**
 * 
 * @author zhy 
 * 
 */  
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration
{  
  
     private static final int[] ATTRS = new int[] { android.R.attr.listDivider };  
     private Drawable mDivider;
     private int columnCount;
     private int offset;
  
     public DividerGridItemDecoration(Context context, int columnCount)
     {  
            final TypedArray a = context.obtainStyledAttributes(ATTRS );
//            mDivider = a.getDrawable(0);
         mDivider = ContextCompat.getDrawable(context, R.drawable.locked_table_view_liner);
         this.columnCount = columnCount;
         offset = DensityUtils.dip2px(context,1);
           a.recycle();  
     }  
  
     @Override
     public void onDrawOver(Canvas c, RecyclerView parent, State state)
     {  
  
           drawHorizontal(c, parent);  
           drawVertical(c, parent);  
  
     }  
     public void drawHorizontal(Canvas c, RecyclerView parent)
     {  
            int childCount = parent.getChildCount();  
            for ( int i = 0; i < childCount; i++)
           {  
                 final View child = parent.getChildAt(i);
                 final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                           .getLayoutParams();  
                 final int left = child.getLeft() - params.leftMargin;  
                 final int right = child.getRight() + params.rightMargin  
                           + mDivider.getIntrinsicWidth();  
                 final int top = child.getBottom() + params.bottomMargin;  
                 final int bottom = top + mDivider.getIntrinsicHeight();
                 mDivider.setBounds(left, top, right, bottom);
                 mDivider.draw(c);

                 if( i <= childCount - 1 && i >= childCount - columnCount){
                     final int lastTop = top - offset;
                     final int lastBottom = bottom - offset;
                     mDivider.setBounds(left, lastTop, right, lastBottom);
                     mDivider.draw(c);
                 }
           }
     }  
  
     public void drawVertical(Canvas c, RecyclerView parent)
     {  
            final int childCount = parent.getChildCount();  
            for ( int i = 0; i < childCount; i++)  
           {  
                 final View child = parent.getChildAt(i);

                 final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                           .getLayoutParams();
                 final int top = child.getTop() - params.topMargin;
                 final int bottom = child.getBottom() + params.bottomMargin;
                 final int left = child.getRight() + params.rightMargin;
                 final int right = left + mDivider.getIntrinsicWidth();

                 mDivider.setBounds(left, top, right, bottom);
                 mDivider.draw(c);
           }  
     }  
  
//     @Override
//     public void getItemOffsets(Rect outRect, int itemPosition,
//                RecyclerView parent)
//     {
//            int spanCount = getSpanCount(parent);
//            int childCount = parent.getAdapter().getItemCount();
//            if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
//           {
//                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
//           } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
//           {
//                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
//           } else
//           {
//                outRect.set(0, 0, 1,
//                            mDivider.getIntrinsicHeight());
//               Log.e("divider",mDivider.getIntrinsicWidth()+"");
//           }
//     }

//上面那个废弃的旧方法会在横竖屏切换的时候偏移量错误
//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
//        int spanCount = getSpanCount(parent);
//        int childCount = parent.getAdapter().getItemCount();
//        if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
//        {
//            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
//        } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
//        {
//            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
//        } else
//        {
//            outRect.set(0, 0, mDivider.getIntrinsicWidth(),
//                    mDivider.getIntrinsicHeight());
//        }
//        Log.e("TableView",outRect.toString());
//        super.getItemOffsets(outRect, view, parent, state);
//    }
}