package com.wangzhen.tabelchart.tableView;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wangzhen.tabelchart.R;

import java.util.ArrayList;

/**
 * Created by wangzhen on 2017/11/27.
 */

public class TableView {
    View mTableView;
    ContentListAdapter contentListAdapter;
    CustomRecycleView contentList;
    LinearLayout titleLinearLayout;
    LinearLayout.LayoutParams layoutParams;
    LinearLayout.LayoutParams linerLayoutParams;
    Context mContext;
    private int titleHeight;
    private int titleWidth;
    private int titleSize;

    private ArrayList<String> titleDatas;
    private ArrayList<String> contentDatas;

    private int titleBackgroudColor;

    public TableView(View view) {
        this.mTableView = view;
        this.mContext = view.getContext();
        init();
    }

    private void init() {
        contentListAdapter = new ContentListAdapter(mContext);
        titleWidth = DensityUtils.dip2px(mContext, 79);
        titleHeight = DensityUtils.dip2px(mContext, 40);

        titleLinearLayout = (LinearLayout) mTableView.findViewById(R.id.title_linearLayout);
        contentList = (CustomRecycleView) mTableView.findViewById(R.id.content_list);
        titleBackgroudColor = mContext.getResources().getColor(R.color.light_gray);


        titleSize = 15;
    }


    public void show() {

        if (titleDatas != null && contentDatas != null) {

            renderTitle();
            renderContent();
        }
    }

    private void renderTitle() {
        layoutParams = new LinearLayout.LayoutParams(titleWidth, titleHeight);
        linerLayoutParams = new LinearLayout.LayoutParams(1, titleHeight);
//        titleLinearLayout.setBackgroundColor(Color.parseColor("#e6e6e6"));
//        layoutParams.setMargins(0, 0, 1, 0);
        titleLinearLayout.removeAllViews();
        for (int i = 0; i < titleDatas.size(); i++) {
            TextView textView = new TextView(mContext);
            textView.setLayoutParams(layoutParams);
            textView.setTextSize((float) titleSize);
            textView.setGravity(Gravity.CENTER);
//            textView.setBackgroundColor(Color.parseColor("#e6e6e6"));
            titleLinearLayout.addView(textView);

            String ellipsizeStr = (String) TextUtils.ellipsize(titleDatas.get(i), textView.getPaint(), titleWidth - 10, TextUtils.TruncateAt.END);
            textView.setText(ellipsizeStr);

            if (i < titleDatas.size() - 1) {
                View liner = new View(mContext);
                liner.setLayoutParams(linerLayoutParams);
                liner.setBackgroundColor(Color.parseColor("#000000"));
                titleLinearLayout.addView(liner);
            }
        }
    }


    private void renderContent() {
        int contentWidth = titleDatas.size() * (titleWidth + 1);
        ViewGroup.LayoutParams layoutParams = contentList.getLayoutParams();
        layoutParams.width = contentWidth;

        contentList.setLayoutParams(layoutParams);

        contentListAdapter.setDatas(contentDatas);
        contentListAdapter.setColumnCounts(titleDatas.size());
        contentListAdapter.setRowCounts(contentDatas.size() / titleDatas.size());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, titleDatas.size(), GridLayoutManager.VERTICAL, false);
        contentList.setLayoutManager(gridLayoutManager);
        contentList.setAdapter(contentListAdapter);
    }


    public View getTableView() {

        return mTableView;
    }

    public TableView setTextHeight(int textHeight) {
        contentListAdapter.setTextHeight(textHeight);
        return this;
    }

    public TableView setTextWidth(int textWidth) {
        contentListAdapter.setTextWidth(textWidth);
        return this;
    }

    public TableView setTextSize(int textSize) {
        contentListAdapter.setTextSize(textSize);
        return this;
    }


    public TableView setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
        return this;
    }

    public TableView setTitleWidth(int titleWidth) {
        this.titleWidth = titleWidth;
        return this;
    }

    public TableView setTitleSize(int titleSize) {
        this.titleSize = titleSize;
        return this;
    }


    public TableView setTitleDatas(ArrayList<String> titleDatas) {
        this.titleDatas = titleDatas;
        return this;
    }

    public TableView setContentDatas(ArrayList<String> contentDatas) {
        this.contentDatas = contentDatas;
        return this;
    }

    public TableView setTitleBackGroundColor(int color) {
        this.titleBackgroudColor = color;
        return this;
    }

}
