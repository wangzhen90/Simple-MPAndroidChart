package com.wangzhen.simplechart;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wangzhen.simplechartlib.charts.BarChart;
import com.wangzhen.simplechartlib.component.XAxis;
import com.wangzhen.simplechartlib.component.YAxis;
import com.wangzhen.simplechartlib.data.chartData.BarData;
import com.wangzhen.simplechartlib.data.dataSet.BarDataSet;
import com.wangzhen.simplechartlib.data.entry.BarEntry;
import com.wangzhen.simplechartlib.formatter.IndexAxisValueFormatter;
import com.wangzhen.simplechartlib.interfaces.dataSets.IBarDataSet;

import java.util.ArrayList;

public class GroupBarChartActivity extends AppCompatActivity {
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_bar_chart);

        barChart = findViewById(R.id.barChart);

        setData();
    }



    void setData() {

        float barWidth = 0.3f;
        float barSpace = 0.f;
        float groupBarSpace = 0.1f;

        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelCount(10);
        xAxis.setGranularity(1f); // only intervals of 1 day
        barChart.getXAxis().setAxisMinimum(0f);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(5);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        String[] xLabels = new String[20];

        for(int i = 0; i < 20; i++){
            xLabels[i] = "客户"+i;
        }


        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();

        BarDataSet set1 = getBarDataSet("Company A");
        BarDataSet set2 = getBarDataSet("Company B");
        BarDataSet set3 = getBarDataSet("Company C");

//        set1.setColor(Color.rgb(104, 241, 175));
        set1.setColor(Color.rgb(0, 0, 0));
        set2.setColor(Color.rgb(164, 228, 251));
        set3.setColor(Color.rgb(242, 247, 158));
//        set4.setColor(Color.rgb(255, 102, 0));

        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(barWidth);


        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));

        barChart.setData(data);


//        barChart.getXAxis().setAxisMaximum(barChart.getBarData().getGroupWidth(groupBarSpace, barSpace) * 20);

        barChart.getXAxis().setAxisMinimum(0f);

        barChart.groupBars(0,groupBarSpace,barSpace);
        barChart.invalidate();



    }





    BarDataSet getBarDataSet(String companyName){

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < 20; i++) {
            float mult = 20;

            float val = (float) (Math.random() * mult);
//            float val = i+1;

            yVals1.add(new BarEntry(i, val));
        }

        BarDataSet dataSet = new BarDataSet(yVals1,companyName);


        return dataSet;

    }


}
