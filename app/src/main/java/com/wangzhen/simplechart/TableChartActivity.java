package com.wangzhen.simplechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wangzhen.tabelchart.tableView.DensityUtils;
import com.wangzhen.tabelchart.tableView.TableView;

import java.util.ArrayList;

public class TableChartActivity extends AppCompatActivity {
    TableView tableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_chart);

        tableView = new TableView(findViewById(R.id.tableView));

        initData();
    }


    void initData() {

        ArrayList<String> titleDatas = new ArrayList<>();
        ArrayList<String> tableDatas = new ArrayList<>();

        int itemWidth = DensityUtils.dip2px(this,75);


        for (int i = 0; i < 10; i++) {
            titleDatas.add("标题" + i);
        }
        for (int j = 0; j < 500; j++) {
            tableDatas.add("内容" + j);
        }


        tableView
                .setTitleDatas(titleDatas)
                .setContentDatas(tableDatas)
                .setTitleSize(15)
                .setTextSize(13)
                .setTitleBackGroundColor(this.getResources().getColor(R.color.table_view_divider_color))
                .setTextHeight(DensityUtils.dip2px(this, 26))
                .setTextWidth(itemWidth)
                .setTitleWidth(itemWidth)
                .setTitleHeight(DensityUtils.dip2px(this, 36))
                .show();
    }

}
