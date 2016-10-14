package com.reactiveapps.chatty.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * 该类是图片加载的接口类
 * <p>
 * 用法:
 * ImageLoaderUtils.displayImage(mContext, image.path, this.imageView, 0);
 */
public class ImageLoaderUtils {

    /**
     * 如果不使用回调的话，就直接使用这个方法
     *
     * @param context
     * @param url
     * @param view
     * @param defaultImage
     */
    public static void displayImage(Context context, String url, ImageView view, int defaultImage) {
        displayImage(context, url, view, defaultImage, null);
    }

    /**
     * 如果要使用回调的话，就调用这个接口
     *
     * @param context
     * @param url
     * @param view
     * @param defaultImage
     * @param listener
     */
    public static void displayImage(Context context, String url, ImageView view, int defaultImage,
                                    RequestListener<String, GlideDrawable> listener) {
        displayImage(context, url, view, defaultImage, true, listener);
    }

    /**
     * @param context
     * @param url
     * @param view
     * @param defaultImage
     * @param useCache     缓存开关
     * @param listener
     */
    public static void displayImage(Context context, String url, ImageView view, int defaultImage, boolean useCache,
                                    RequestListener<String, GlideDrawable> listener) {
        Glide.with(context).load(url)
                .diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE).skipMemoryCache(!useCache)
                .error(defaultImage).placeholder(defaultImage).listener(listener).into(view);
    }

    public static void displayLocalImage(Context context, String url, ImageView view, int defaultImage) {
        boolean useCache = true;
        Glide.with(context).load(url)
                .diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE).skipMemoryCache(!useCache)
                .error(defaultImage).placeholder(defaultImage).listener(null).into(view);
    }

    public static void displayLocalImage(Context context, Uri uri, ImageView view, int defaultImage) {
        boolean useCache = true;
        Glide.with(context).load(uri)
                .diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE).skipMemoryCache(!useCache)
                .error(defaultImage).placeholder(defaultImage).listener(null).into(view);
    }

    public static void displayLocalImage(Context context, File file, ImageView view, int defaultImage) {
        boolean useCache = false;
        Glide.with(context).load(file)
                .diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE).skipMemoryCache(!useCache)
                .error(defaultImage).placeholder(defaultImage).listener(null).into(view);
    }

    public static void displayLocalImage(Context context, int resourceId, ImageView view, int defaultImage) {
        boolean useCache = false;
        Glide.with(context).load(resourceId)
                .diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE).skipMemoryCache(!useCache)
                .error(defaultImage).placeholder(defaultImage).listener(null).into(view);
    }

    public static void displayLocalImage(Context context, byte[] module, ImageView view, int defaultImage) {
        boolean useCache = false;
        Glide.with(context).load(module)
                .diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE).skipMemoryCache(!useCache)
                .error(defaultImage).placeholder(defaultImage).listener(null).into(view);
    }

    public static void displayLocalImage(Context context, String filePath, ImageView view) {
        boolean useCache = true;
        Glide.with(context).load(filePath)
                .diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE).skipMemoryCache(!useCache).listener(null).into(view);
    }


    /**
     * 加载网络图片, 如果图片没有加载出来则显示progressbar.
     *
     * @param context
     * @param url
     * @param glideDrawableImageViewTarget
     * @param defaultImage
     */
    public static void displayImageWithProgressBar(Context context, String url, GlideDrawableImageViewTarget glideDrawableImageViewTarget, int defaultImage) {
        boolean useCache = true;
        Glide.with(context).load(url)
                .diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE).skipMemoryCache(!useCache)
                .error(defaultImage).listener(null).into(glideDrawableImageViewTarget);
    }

    public static void displayImageWithProgressBar2(Context context, String url, GlideDrawableImageViewTarget glideDrawableImageViewTarget, int defaultImage) {
        Glide.with(context).load(url).into(glideDrawableImageViewTarget);
    }

    public static void displayImage2(Context context, String url, ImageView view, int defaultImage) {
        Glide.with(context).load(url).centerCrop().placeholder(defaultImage).into(view);
    }

    public static void displayCircleImage(Context context, String url, ImageView view, int defaultImage) {
        boolean useCache = true;
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                .skipMemoryCache(!useCache)
                .fitCenter()
                .bitmapTransform(new GrayscaleTransformation(context), new CropCircleTransformation(context))
                .into(view);
    }

    public static void displayCircleImage(Context context, int resId, ImageView view) {
        boolean useCache = true;
        Glide.with(context)
                .load(resId)
                .diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                .skipMemoryCache(!useCache)
                .fitCenter()
                .bitmapTransform(new GrayscaleTransformation(context), new CropCircleTransformation(context))
                .placeholder(resId)
                .into(view);
    }

    /**
     * 显示圆形灰色图片
     *
     * @param context
     * @param resId
     * @param view
     */
    public static void displayLocalCircleMaskImage(Context context, int resId, ImageView view) {
        boolean useCache = true;
        Glide.with(context).load(resId)
                .diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE).skipMemoryCache(!useCache)
//				.bitmapTransform(new BlurTransformation(context, 25), new CropCircleTransformation(context))
                .bitmapTransform(new GrayscaleTransformation(context), new CropCircleTransformation(context))
                .into((view));
    }

    public static void displayOnlineCircleMaskImage(Context context, String url, ImageView view, int defaultImage) {
        boolean useCache = true;
        Glide.with(context).load(url)
                .diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE).skipMemoryCache(!useCache)
//				.bitmapTransform(new BlurTransformation(context, 25), new CropCircleTransformation(context))
                .fitCenter()
                .bitmapTransform(new GrayscaleTransformation(context), new CropCircleTransformation(context))
//				.placeholder(defaultImage)
                .into((view));
    }


    public static void displayImage(Context context, String url, ImageView view) {
        boolean useCache = true;
        Glide.with(context).load(url).diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE).skipMemoryCache(!useCache).into(view);
    }

    public static void displayImage(Context context, int resId, ImageView view) {
        boolean useCache = true;
        Glide.with(context).load(resId).diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE).skipMemoryCache(!useCache).placeholder(resId).into(view);
    }

    public static void displayLocalMaskImage(Context context, int resId, ImageView view) {
        boolean useCache = true;
        Glide.with(context).load(resId)
                .diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE).skipMemoryCache(!useCache)
//				.bitmapTransform(new BlurTransformation(context, 25), new CropCircleTransformation(context))
                .bitmapTransform(new GrayscaleTransformation(context))
                .placeholder(resId)
                .into((view));
    }

    public static void displayOnlineMaskImage(Context context, String url, ImageView view, int defaultImage) {
        boolean useCache = true;
        Glide.with(context).load(url)
                .diskCacheStrategy(useCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE).skipMemoryCache(!useCache)
//				.bitmapTransform(new BlurTransformation(context, 25), new CropCircleTransformation(context))
                .bitmapTransform(new GrayscaleTransformation(context))
                .placeholder(defaultImage)
                .into((view));
    }

    public static void displayGifImg(ImageView imageView, int res, boolean isOriginal,
                                     RequestListener<Integer, GlideDrawable> callback) {
        try {
            DrawableTypeRequest<Integer> request = Glide.with(imageView.getContext()).load(res);
            request.listener(callback);
            if (isOriginal) {
                request.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(imageView);
            } else {
                request.into(imageView);
            }
        } catch (Exception e) {
        }
    }


}
