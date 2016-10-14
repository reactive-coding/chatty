package com.reactiveapps.chatty.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;

public class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
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

    public static float getDescendantCoordRelativeToSelf(View descendant,
            int[] coord) {
        float scale = 1.0f;
        float[] pt = { coord[0], coord[1] };
        // 坐标值进行当前窗口的矩阵映射，比如View进行了旋转之类，它的坐标系会发生改变。map之后，会把点转换为改变之前的坐标。
        descendant.getMatrix().mapPoints(pt);
        // 转换为直接父窗口的坐标
        scale *= descendant.getScaleX();
        pt[0] = descendant.getLeft();
        pt[1] = descendant.getTop();
        ViewParent viewParent = descendant.getParent();
        // 循环获得父窗口的父窗口，并且依次计算在每个父窗口中的坐标
        while (viewParent != null && viewParent instanceof View) {
            final View view = (View) viewParent;
            view.getMatrix().mapPoints(pt);
            scale *= view.getScaleX();// 这个是计算X的缩放值。此处可以不管
            // 转换为相当于可视区左上角的坐标，scrollX，scollY是去掉滚动的影响
            pt[0] += view.getLeft() - view.getScrollX();
            pt[1] += view.getTop() - view.getScrollY();
            viewParent = view.getParent();
        }
        coord[0] = (int) Math.round(pt[0]);
        coord[1] = (int) Math.round(pt[1]);
        return scale;
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     * 
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        return new Point(width, height);
    }

    /**
     * 获取屏幕长宽比
     * 
     * @param context
     * @return
     */
    public static float getScreenRate(Context context) {
        Point P = getScreenMetrics(context);
        float height = P.y;
        float width = P.x;
        return (height / width);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    
	public static float getDensity(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
//		mScreenWidth = dm.widthPixels;
//		mScreenHeight = dm.heightPixels;
		return dm.density;
	}

	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
}