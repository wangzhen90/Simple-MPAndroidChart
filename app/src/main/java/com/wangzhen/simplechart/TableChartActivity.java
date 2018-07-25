package com.wangzhen.simplechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.wangzhen.tableChart.component.TableChart;
import com.wangzhen.tableChart.data.Cell;
import com.wangzhen.tableChart.data.Column;
import com.wangzhen.tableChart.data.Sheet;
import com.wangzhen.tableChart.interfaces.ITableOnClickListener;

import java.util.ArrayList;
import java.util.List;

public class TableChartActivity extends AppCompatActivity {
    TableChart tableChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_chart);

        tableChart = findViewById(R.id.tableView);
        tableChart.setOnClickListener(new ITableOnClickListener() {
            @Override
            public void onColumnClick(Column column) {
                Toast.makeText(TableChartActivity.this,"点击了"+column.columnName,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCellClick(Cell cell) {
                Toast.makeText(TableChartActivity.this,"点击了"+cell.getContents(),Toast.LENGTH_SHORT).show();
            }
        });


        initData();
    }


    void initData() {

        List<Column> columns = new ArrayList<>();
        for(int i = 0; i<20;i++){

            columns.add( new Column(i == 1 ? "标题比较长比较长比较长"+i : "标题"+i));
        }


        for(int i = 0; i < columns.size();i++){
            List<Cell> cells = new ArrayList<>();
           for(int j = 0; j < 3000;j++){
               cells.add(new Cell(j,i,"内容"+i+"-"+j));
           }

           columns.get(i).setData(cells);
        }

        Sheet<Cell> sheet = new Sheet<>(columns,null);

        tableChart.setSheet(sheet);

    }

}
