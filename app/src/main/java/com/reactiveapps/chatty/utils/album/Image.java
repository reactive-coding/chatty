package com.reactiveapps.chatty.utils.album;

import java.io.Serializable;

public class Image implements Serializable {
    private String mImageId;
    private String mImagePath;
    private String mDisplayName;
    private String mSize;
    private int mDate;
    private String mTitle;

    private String mBucketId;
    private String mBucketDisplayName;
    private String mThumbnailPath;

    private String mPicasaId;

    private boolean mIsSelected = false;

    public void setImageId(String imageId) {
        this.mImageId = imageId;
    }

    public String getImageId() {
        return this.mImageId;
    }

    public void setBucketId(String bucketId) {
        this.mBucketId = bucketId;
    }

    public String getBucketId() {
        return this.mBucketId;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.mThumbnailPath = thumbnailPath;
    }

    public String getThumbnailPath() {
        return this.mThumbnailPath;
    }

    public void setImagePath(String imagePath) {
        this.mImagePath = imagePath;
    }

    public String getImagePath() {
        return this.mImagePath;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public String getDisplayName() {
        return this.mDisplayName;
    }

    public void setPicasaId(String picasaId) {
        this.mPicasaId = picasaId;
    }

    public String getPicasaId() {
        return this.mPicasaId;
    }

    public void setSize(String size) {
        this.mSize = size;
    }

    public String getSize() {
        return this.mSize;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setBucketDisplayName(String bucketDisplayName) {
        this.mBucketDisplayName = bucketDisplayName;
    }

    public String getBucketDisplayName() {
        return this.mBucketDisplayName;
    }

    public void setSelected(boolean select) {
        this.mIsSelected = select;
    }

    public boolean getSelected() {
        return this.mIsSelected;
    }

    public int getDate() {
        return mDate;
    }

    public void setDate(int Date) {
        this.mDate = Date;
    }

    @Override
    public String toString() {
        return "Image [mImageId=" + mImageId + ", mImagePath=" + mImagePath
                + ", mDisplayName=" + mDisplayName + ", mSize=" + mSize
                + ", mDate=" + mDate + ", mTitle=" + mTitle + ", mBucketId="
                + mBucketId + ", mBucketDisplayName=" + mBucketDisplayName
                + ", mThumbnailPath=" + mThumbnailPath + ", mPicasaId="
                + mPicasaId + ", mIsSelected=" + mIsSelected + "]";
    }
}