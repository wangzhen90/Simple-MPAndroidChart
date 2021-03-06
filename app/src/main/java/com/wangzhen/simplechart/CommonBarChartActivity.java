package com.wangzhen.simplechart;

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

public class CommonBarChartActivity extends AppCompatActivity {
    BarChart barChart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_bar_chart);

        barChart = findViewById(R.id.barChart);

        setData();
    }

    void setData() {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelCount(10);
        xAxis.setGranularity(1f);



        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(5);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        String[] xLabels = new String[20];
        for (int i = 0; i < 20; i++) {
            float mult = 20;

            float val = (float) (Math.random() * mult);

            yVals1.add(new BarEntry(i, val));

            xLabels[i] = "客户"+i;

        }

        BarDataSet set1;
        set1 = new BarDataSet(yVals1);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        final BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        barChart.setData(data);

    }
}
