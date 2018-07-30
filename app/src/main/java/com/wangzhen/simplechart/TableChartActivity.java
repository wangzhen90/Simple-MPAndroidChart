package com.wangzhen.simplechart;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.Toast;

import com.wangzhen.tableChart.component.TableChart;
import com.wangzhen.tableChart.data.Cell;
import com.wangzhen.tableChart.data.Column;
import com.wangzhen.tableChart.data.Sheet;
import com.wangzhen.tableChart.formatter.ITextFormatter;
import com.wangzhen.tableChart.interfaces.ICell;
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
                Toast.makeText(TableChartActivity.this, "点击了" + column.columnName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCellClick(ICell cell) {
                Toast.makeText(TableChartActivity.this, "点击了" + cell.getContents(), Toast.LENGTH_SHORT).show();
            }
        });


        initData();
    }


    void initData() {

        List<Column> columns = new ArrayList<>();
        for (int i = 0; i < 20; i++) {

            columns.add(new Column(i == 1 ? "标题比较长比较长比较长" + i : "标题" + i));
        }


        for (int i = 0; i < columns.size(); i++) {
            List<Cell> cells = new ArrayList<>();
            for (int j = 0; j < 1000; j++) {
                cells.add(new Cell(j, i, "内容" + i + "-" + j));
            }

            columns.get(i).setData(cells);
        }

        Sheet<Cell> sheet = new Sheet<>(columns, null);

        sheet.merge(0, 0, 2, 2);
        sheet.merge(5, 0, 5, 1);
        sheet.merge(6, 2, 7, 3);

        sheet.setTextFormatter(new ITextFormatter() {
            @Override
            public int getTextSize(ICell cell, Column<ICell> column, List<Column<ICell>> columns) {

                return 9;
            }

            @Override
            public TextPaint.Align getTextAlign(ICell cell, Column<ICell> column, List<Column<ICell>> columns) {
                if (cell.getColumn() == 1) {
                    return TextPaint.Align.RIGHT;
                } else {
                    return TextPaint.Align.CENTER;
                }
            }
        });


        tableChart.setSheet(sheet);

    }

}
