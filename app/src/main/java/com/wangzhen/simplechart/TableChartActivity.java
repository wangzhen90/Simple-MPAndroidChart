package com.wangzhen.simplechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wangzhen.simplechartlib.tableChart.component.TableChart;
import com.wangzhen.simplechartlib.tableChart.data.Cell;
import com.wangzhen.simplechartlib.tableChart.data.Column;
import com.wangzhen.simplechartlib.tableChart.data.Sheet;

import java.util.ArrayList;
import java.util.List;

public class TableChartActivity extends AppCompatActivity {
    TableChart tableChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_chart);

        tableChart = findViewById(R.id.tableView);

        initData();
    }


    void initData() {

        List<Column> columns = new ArrayList<>();
        for(int i = 0; i<50;i++){
            columns.add( new Column("标题"+i));
        }

        List<Cell> cells = new ArrayList<>();


        for(int i = 0; i < columns.size();i++){

           for(int j = 0; j < 20;j++){
               cells.add(new Cell(i,j,"标题"+j));
           }
        }

        Sheet<Cell> sheet = new Sheet<>(columns,cells);

        tableChart.setSheet(sheet);

    }

}
