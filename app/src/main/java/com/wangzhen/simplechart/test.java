package com.wangzhen.simplechart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by wangzhen on 2018/5/1.
 */

public class test {

    protected void onCreate() {

//        ImageView iv_1;
//        ImageView iv_2;
//        Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.mipmap.test_pic);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 10, bos);
//        byte[] bytes = bos.toByteArray();
//        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//
//
//        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//        //一般采用最大内存的1/8
//        int cacheSize = maxMemory /8;
//
//        LruCache<String,Bitmap> mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
//
//            //这个方法要保持和cacheSize的单位统一
//            @Override
//            protected int sizeOf(String key, Bitmap value) {
//                return value.getByteCount()/1024;
//            }
//        };



    }

}
