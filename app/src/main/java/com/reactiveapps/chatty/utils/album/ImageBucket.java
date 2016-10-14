package com.reactiveapps.chatty.utils.album;

import java.util.ArrayList;
import java.util.List;

public class ImageBucket {
    private String mBucketName;
    private ArrayList<Image> mImageList;

    public ImageBucket() {
        mImageList = new ArrayList<>();
    }

    public void setBucketName(String bucketName) {
        this.mBucketName = bucketName;
    }

    public String getBucketName() {
        return this.mBucketName;
    }

    public void addImage(Image image) {
        if (mImageList != null)
            mImageList.add(image);
    }

    public int getCount() {
        if (mImageList != null)
            return mImageList.size();
        else {
            return 0;
        }
    }

    public List<Image> getImageList() {
        return mImageList;
    }
}