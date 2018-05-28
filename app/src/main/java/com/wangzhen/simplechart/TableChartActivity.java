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

        int itemWidth = DensityUtils.dip2px(this,90);


        for (int i = 0; i < 10; i++) {
            titleDatas.add("标题" + i);
        }



        for (int j = 0; j < 500; j++) {
            if(j%10 == 2){
                tableDatas.add("8888888888888888888");

            }else if(j == 1){
                tableDatas.add("客户："+j);
            }
            else if(j%10 == 1){
                tableDatas.add("测试很长很长很长的汉字，测试很长很长很长很长的汉字");

            }else{
                tableDatas.add("客户："+j);

            }
        }


        tableView
                .setTitleDatas(titleDatas)
                .setContentDatas(tableDatas)
                .setTitleSize(15)
                .setTextSize(13)
                .setTitleBackGroundColor(this.getResources().getColor(R.color.table_view_divider_color))
                .setTextHeight(DensityUtils.dip2px(this, 30))
                .setTextWidth(itemWidth)
                .setTitleWidth(itemWidth)
                .setTitleHeight(DensityUtils.dip2px(this, 36))
                .show();
    }

}
