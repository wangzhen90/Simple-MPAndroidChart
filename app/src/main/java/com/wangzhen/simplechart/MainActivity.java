package com.wangzhen.simplechart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<ChartItem> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        initData();

        listView.setAdapter(new DataAdapter());
    }

    void initData(){

        items.add(new ChartItem("普通柱状图",CommonBarChartActivity.class));
        items.add(new ChartItem("簇型图",GroupBarChartActivity.class));
        items.add(new ChartItem("表格",TableChartActivity.class));

    }


    class DataAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public ChartItem getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View contextView, ViewGroup viewGroup) {

            if(contextView == null){
                contextView = View.inflate(MainActivity.this,R.layout.layout_chart_item,null);
            }
            final ChartItem item = getItem(i);
            ((TextView)contextView.findViewById(R.id.textView)).setText(item.name);
            contextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,item.clazz);
                    startActivity(intent);
                }
            });

            return contextView;
        }
    }








}

