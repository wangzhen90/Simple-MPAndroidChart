package com.wangzhen.tabelchart.tableView;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.wangzhen.tabelchart.R;

import java.util.ArrayList;

/**
 * Created by wangzhen on 2017/11/27.
 */

public class ContentListAdapter extends RecyclerView.Adapter<ContentListAdapter.ContentViewHolder> {

    private ArrayList<String> mDatas;
    private int textHeight;
    private int textWidth;
    private int textSize;
    RecyclerView.LayoutParams layoutParams;
    private int columnCounts;
    private  int rowCounts;

    public void setDatas(ArrayList<String> datas) {
        this.mDatas = datas;
    }

    public ContentListAdapter(Context context) {
        textHeight = DensityUtils.dip2px(context, 50);
        textWidth = DensityUtils.dip2px(context, 79);
        textSize = 13;
    }

    public void setTextHeight(int textHeight) {
        this.textHeight = textHeight;
    }

    public void setTextWidth(int textWidth) {
        this.textWidth = textWidth;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setRowCounts(int rowCounts){
        this.rowCounts = rowCounts;
    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_layout, parent, false);
        TextView v = new TextView(parent.getContext());
        return new ContentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, int position) {
        layoutParams = new RecyclerView.LayoutParams( textWidth, textHeight);
        layoutParams.setMargins(1,1,1,1);

        if(position/rowCounts == 1){
            layoutParams.width = textWidth+100;
        }

        holder.textView.setLayoutParams(layoutParams);
        String dataStr = mDatas.get(position);
        if (TextUtils.isEmpty(dataStr) || dataStr.equals("null") || dataStr.equals(("undefined"))) {
            dataStr = "--";
        }
        String ellipsizeStr = (String) TextUtils.ellipsize(dataStr, holder.textView.getPaint(), textWidth - 10, TextUtils.TruncateAt.END);

        holder.textView.setText(ellipsizeStr);
//        int count = position / columnCounts;
//        if(count%2!=0){
//            holder.textView.setBackgroundColor(holder.textView.getContext().getResources().getColor(R.color.table_view_header_bg));
//        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setColumnCounts(int columnCounts) {
        this.columnCounts = columnCounts;
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        ContentViewHolder(View view) {
            super(view);
            textView = (TextView) view;

            textView.setGravity(Gravity.CENTER);
//            textView.setSingleLine(true);
            textView.setTextSize(textSize);
            textView.setBackgroundColor(Color.parseColor("#ffffff"));
//            textView.setBackgroundResource(R.drawable.drawable_table_item);
//            textView.setEllipsize(TextUtils.TruncateAt.END);
        }
    }
}
