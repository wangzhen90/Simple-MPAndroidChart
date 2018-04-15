package com.wangzhen.simplechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wangzhen.simplechartlib.charts.BarChart;
import com.wangzhen.simplechartlib.data.chartData.BarData;
import com.wangzhen.simplechartlib.data.dataSet.BarDataSet;
import com.wangzhen.simplechartlib.data.entry.BarEntry;
import com.wangzhen.simplechartlib.interfaces.dataSets.IBarDataSet;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        barChart = findViewById(R.id.barChart);
        setData();

    }


    void setData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < 20; i++) {
            float mult = 20;

            float val = (float) (Math.random() * mult);

            yVals1.add(new BarEntry(i, val));
        }

        BarDataSet set1;
        set1 = new BarDataSet(yVals1);
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);

        barChart.setData(data);

    }

}

