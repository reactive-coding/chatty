package com.reactiveapps.chatty.view.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.utils.ImageLoaderUtils;
import com.reactiveapps.chatty.view.activity.ActivityImageSelect;
import java.util.ArrayList;
import uk.co.senab.photoview.PhotoView;

public class ImageSelectViewPagerAdapter extends PagerAdapter {
    ArrayList<ActivityImageSelect.ImageState> mPaths;
    private Activity mContext;

    public ImageSelectViewPagerAdapter(Activity context, ArrayList<ActivityImageSelect.ImageState> paths) {
        this.mPaths = paths;
        this.mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (null == mPaths) {
            return null;
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_image_preview_content, container, false);
        container.addView(view);
//        PhotoViewDrawee imageView = (PhotoViewDrawee) view.findViewById(R.id.layout_image_preview_image);
        PhotoView imageView = (PhotoView) view.findViewById(R.id.layout_image_preview_image);
        ActivityImageSelect.ImageState state = mPaths.get(position);
        String path = state.path;
        if (TextUtils.isEmpty(path)) {
            imageView.setImageResource(R.mipmap.chatting_default_image_broke);
        } else {
//            imageView.setImageUri("file://" + path);
            ImageLoaderUtils.displayLocalImage(mContext, path, imageView);
        }
        return view;
    }

    @Override
    public int getCount() {
        if (null == mPaths) {
            return 0;
        }
        return mPaths.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == (arg1);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    
    public ArrayList<ActivityImageSelect.ImageState> items() {
        return mPaths;
    }
}
