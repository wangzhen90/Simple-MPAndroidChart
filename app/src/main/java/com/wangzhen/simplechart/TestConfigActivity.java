package com.wangzhen.simplechart;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_config);
        System.out.println("Activity1----->onCreate");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Activity1----->onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("Activity1----->onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("Activity1----->onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Activity1----->onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("Activity1----->onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Activity1----->onStop");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("Activity1----->onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("Activity1----->onSaveInstanceState");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            System.out.println("现在是横屏转竖屏");
        }else if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            System.out.println("现在是竖屏转横屏");
        }
    }
}
