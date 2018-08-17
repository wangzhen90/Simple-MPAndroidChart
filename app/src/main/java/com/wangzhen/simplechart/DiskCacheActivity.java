package com.wangzhen.simplechart;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lib.simpleimageloader.disklrucache.DiskLruCache;

public class DiskCacheActivity extends AppCompatActivity {

    DiskLruCache mDiskLruCache = null;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disk_cache);
        imageView = findViewById(R.id.image_view);
        openDiskCache();
        downloadImage();

    }

    private void openDiskCache(){

        try {
            File cacheDir = getDiskCacheDir(this,"imageCache");
            Log.e("LRUPath",cacheDir.getAbsolutePath());

            if(!cacheDir.exists()){
                cacheDir.mkdir();
            }
            //缓存目录，版本，同一个key可以对应多少个缓存文件，缓存目录上限
            mDiskLruCache = DiskLruCache.open(cacheDir,getAppVersion(this),1,50 * 1024 *1024);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void downloadImage(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String imageUrl = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
                    //key一般取url的md5值
                    String key = hashKeyForDisk(imageUrl);

                    //获取editor
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if(editor != null){
                        //由于DiskLruCache.open的时候，设置的一个key只对应一个缓存文件，所以此处传0
                        OutputStream outputStream = editor.newOutputStream(0);
                        if(downloadUrlToStream(imageUrl,outputStream)){
                            //写入生效
                            editor.commit();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DiskCacheActivity.this,"缓存成功",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            //放弃此次写入
                            editor.abort();
                        }
                    }
                    /**
                     * 将内存中的操作记录同步到日志文件（journal文件）中，不要频繁的调用这个方法，选择一个适当的时机进行调用
                     */
                    mDiskLruCache.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


    private boolean downloadUrlToStream(String urlString,OutputStream outputStream){

        HttpURLConnection urlConnection = null;

        BufferedOutputStream out = null;
        BufferedInputStream in = null;


        try {
            final URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();

            in = new BufferedInputStream(urlConnection.getInputStream(),8 * 1024);
            out = new BufferedOutputStream(outputStream,8 * 1024);

            int b;

            while((b = in.read()) != -1){
                out.write(b);
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            if(urlConnection != null){
                urlConnection.disconnect();
            }

            try {
                if(out != null){
                    out.close();
                }

                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return false;
    }

    public void readCache(View view){

        try {
            String imageUrl = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
            String key = hashKeyForDisk(imageUrl);

            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);

            if(snapshot != null){

                InputStream is = snapshot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imageView.setImageBitmap(bitmap);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
