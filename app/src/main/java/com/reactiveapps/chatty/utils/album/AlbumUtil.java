package com.reactiveapps.chatty.utils.album;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import com.reactiveapps.chatty.App;
import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.utils.PermissionsUtil;
import com.reactiveapps.chatty.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class AlbumUtil {
    private static AlbumUtil sInstance;
    private ContentResolver mContentResolver;

    private boolean mHasBuildImagesBucketList;

    private HashMap<String, String> mThumbnailsList;
    private HashMap<String, ImageBucket> mBucketListTotal;
    private HashMap<String, ImageBucket> mBucketList;
    private ArrayList<ImageBucket> mList;

    private AlbumUtil() {
        mContentResolver = App.getInst().getApplicationContext().getContentResolver();
        mThumbnailsList = new HashMap<>();
        mBucketList = new HashMap<>();
        mBucketListTotal = new HashMap<>();
    }

    public static AlbumUtil getInstance() {
        if (sInstance == null) {
            synchronized (AlbumUtil.class) {
                if (sInstance == null) {
                    sInstance = new AlbumUtil();
                }
            }
        }
        return sInstance;
    }

    public List<ImageBucket> getImageBucketList(boolean refresh) {
        if (refresh || (!refresh && !mHasBuildImagesBucketList)) {
            buildImageBucketList();
        }

        mList = new ArrayList<>();
        Iterator<Entry<String, ImageBucket>> iteratorTotal = mBucketListTotal.entrySet().iterator();
        while (iteratorTotal.hasNext()) {
            ImageBucket imageBucket = iteratorTotal.next().getValue();
            mList.add(imageBucket);
        }

        Iterator<Entry<String, ImageBucket>> iterator = mBucketList.entrySet().iterator();
        while (iterator.hasNext()) {
            ImageBucket imageBucket = iterator.next().getValue();
            mList.add(imageBucket);
        }

        return mList;
    }

    private void buildImageBucketList() {
        if (PermissionsUtil.getInstance().isMNC()) {
            if (PermissionsUtil.getInstance().hasPermission(App.getInst(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//从后台切换至前台，此时存储权限已关闭,需要判断有误存储权限
                getThumbnail();
                getPhoto();
                mHasBuildImagesBucketList = true;
            } else {
                ToastUtil.showLongToast(App.getInst().getString(R.string.permission_dialog_title2, App.getInst().getString(R.string.permission_storage)));
            }
        } else {
            getThumbnail();
            getPhoto();
            mHasBuildImagesBucketList = true;
        }
    }

    private void getThumbnail() {
        String projection[] = {Thumbnails._ID, Thumbnails.IMAGE_ID,
                Thumbnails.DATA, Thumbnails.HEIGHT, Thumbnails.WIDTH};
        Cursor cursor = mContentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            int _ID = cursor.getColumnIndex(Thumbnails._ID);
            int IMAGE_ID = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
            int DATA = cursor.getColumnIndex(Thumbnails.DATA);
            if (cursor.moveToFirst()) {
                do {
                    String image_id = cursor.getString(IMAGE_ID);
                    String data = cursor.getString(DATA);
                    mThumbnailsList.put(image_id, data);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    private void getPhoto() {
        String projection[] = {Media._ID, Media.BUCKET_ID, Media.PICASA_ID,
                Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
                Media.BUCKET_DISPLAY_NAME, Media.SIZE};
        String sortOrder = Media.DATE_MODIFIED + " desc";
        Cursor cursor = mContentResolver.query(Media.EXTERNAL_CONTENT_URI,
                projection, null, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            int _ID = cursor.getColumnIndex(Media._ID);
            int BUCKET_ID = cursor.getColumnIndex(Media.BUCKET_ID);
            int PICASA_ID = cursor.getColumnIndex(Media.PICASA_ID);
            int DATA = cursor.getColumnIndex(Media.DATA);
            int DISPLAY_NAME = cursor.getColumnIndex(Media.DISPLAY_NAME);
            int TITLE = cursor.getColumnIndex(Media.TITLE);
            int BUCKET_DISPLAY_NAME = cursor.getColumnIndex(Media.BUCKET_DISPLAY_NAME);
            int SIZE = cursor.getColumnIndex(Media.SIZE);

            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(_ID);
                    String bucket_id = cursor.getString(BUCKET_ID);
                    String picasa_id = cursor.getString(PICASA_ID);
                    String size = cursor.getString(SIZE);
                    String data = cursor.getString(DATA);
                    String display_name = cursor.getString(DISPLAY_NAME);
                    String title = cursor.getString(TITLE);
                    String bucket_display_name = cursor.getString(BUCKET_DISPLAY_NAME);
                    ImageBucket imageBucketTotal = mBucketListTotal.get("total");
                    ImageBucket imageBucket = mBucketList.get(bucket_id);

                    if (imageBucket == null) {
                        imageBucket = new ImageBucket();
                        imageBucket.setBucketName(bucket_display_name);
                        mBucketList.put(bucket_id, imageBucket);
                    }
                    if (imageBucketTotal == null) {
                        imageBucketTotal = new ImageBucket();
                        imageBucketTotal.setBucketName("All pictures");
                        mBucketListTotal.put("total", imageBucketTotal);
                    }
                    Image image = new Image();
                    image.setImageId(id);
                    image.setBucketId(bucket_id);
                    image.setPicasaId(picasa_id);
                    image.setSize(size);
                    image.setDisplayName(display_name);
                    image.setTitle(title);
                    image.setImagePath(data);
                    image.setBucketDisplayName(bucket_display_name);
                    image.setSelected(false);
                    image.setThumbnailPath(mThumbnailsList.get(id));
                    imageBucket.addImage(image);

                    Image imageTotal = new Image();
                    imageTotal.setImageId(id);
                    imageTotal.setBucketId("1");// Must be number
                    imageTotal.setPicasaId(picasa_id);
                    imageTotal.setSize(size);
                    imageTotal.setDisplayName("ALL");
                    imageTotal.setTitle(title);
                    imageTotal.setImagePath(data);
                    imageTotal.setBucketDisplayName("All Picture");
                    imageTotal.setSelected(false);
                    imageTotal.setThumbnailPath(mThumbnailsList.get(id));
                    imageBucketTotal.addImage(imageTotal);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
    }

    public void clear() {
        mThumbnailsList.clear();
        mBucketListTotal.clear();
        mBucketList.clear();
        mList.clear();
    }
}