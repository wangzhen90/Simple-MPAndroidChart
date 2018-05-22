package com.wangzhen.tabelchart.tableView;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DensityUtils {


    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;

    private static double MULTIPLE = -1;

    public static float SCREEN_DENSITY = -1;


    private int width;
    private int height;

    private int[] margin;

    public DensityUtils(int width, int height, int[] margin, WindowManager wm) {
        this.width = width;
        this.height = height;
        this.margin = margin;
        if (wm != null) {
            getMultiple(wm);
        }
    }

    public DensityUtils(int width, int height) {
        this(width, height, null, null);
    }

    public int getWidth() {
        return (int) (MULTIPLE * width);
    }

    public int getHeight() {
        return (int) (MULTIPLE * height);
    }

    public int getMargin(int pos) {
        return (int) (MULTIPLE * margin[pos]);
    }

    public static double getMultiple(WindowManager wm) {
        if (MULTIPLE == -1) {
            MULTIPLE = getMultipleFromScreenSize(wm);
        }
        return MULTIPLE;
    }

    public static int getSize(int mSize) {
        return (int) (MULTIPLE * mSize);
    }

    public static double getMultipleFromScreenSize(WindowManager wm) {
        Display d = wm.getDefaultDisplay();
        int width = Math.min(d.getWidth(), d.getHeight());
        if (width == 240) {
            return 0.75;
        } else if (width == 320) {
            return 1;
        } else {
            return 1.5;
        }
    }

    public static double getScale(WindowManager wm) {
        Display d = wm.getDefaultDisplay();
        int width = Math.min(d.getWidth(), d.getHeight());
        return width / 320;
    }

    public static void setDensity(Context mContext) {
        SCREEN_DENSITY = mContext.getResources().getDisplayMetrics().density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int getScreenW(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenH(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

}
