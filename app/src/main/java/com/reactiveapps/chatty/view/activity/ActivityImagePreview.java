package com.reactiveapps.chatty.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.utils.DisplayUtils;
import com.reactiveapps.chatty.utils.FileUtils;
import com.reactiveapps.chatty.utils.ImageLoaderUtils;
import com.reactiveapps.chatty.utils.ListDialog;
import com.reactiveapps.chatty.utils.ToastUtil;
import com.reactiveapps.chatty.view.Base.ActivityBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import uk.co.senab.photoview.PhotoView;

public class ActivityImagePreview extends ActivityBase implements
        OnClickListener, View.OnLongClickListener {
    private static final String TAG = ActivityImagePreview.class.getSimpleName();
    private static final String PATH_PREFIX = "file://";
    //    private PhotoViewDrawee mPhotoView;
//    private SimpleDraweeView mPhotoView;
//    private ImageView mPhotoView;
    private PhotoView mPhotoView;
    private ImageInfo mImages;
    private int mCurrentIndex;
    private String mSendOrReceive;
    private String mFromWhere;//来自聊天或者历史页

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        initView();
    }

    public void initView() {
//        TextView title = (TextView) findViewById(R.id.centerText);
//        title.setText(R.string.image_preview_picture);
//        title.setVisibility(View.VISIBLE);
//
//        ImageView back = (ImageView) findViewById(R.id.leftImage);
//        back.setVisibility(View.VISIBLE);
//        back.setImageResource(R.drawable.toolbar_btn_back_seletor);
//        back.setOnClickListener(this);

        mPhotoView = (PhotoView) findViewById(R.id.photo_view);
        mPhotoView.setOnLongClickListener(this);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            mImages = intent.getExtras().getParcelable("images");
            mCurrentIndex = intent.getExtras().getInt("index");
            mSendOrReceive = intent.getExtras().getString("sendOrRec");
            mFromWhere = intent.getExtras().getString("from");

            if ("send".equals(mSendOrReceive)) {
                if (mImages != null && mImages.getLocalPath() != null) {
                    Log.d(TAG, "------ ActivityImagePreview(), file_path: " + mImages.getLocalPath() + " ------");

                    ImageLoaderUtils.displayLocalImage(ActivityImagePreview.this, mImages.getLocalPath(), mPhotoView);
                } else if (mImages != null && mImages.getUrl() != null) {

                    ImageLoaderUtils.displayImage(ActivityImagePreview.this, mImages.getUrl(), mPhotoView, R.mipmap.chatting_default_download_icon);
                }
            } else {
                if (mImages != null && mImages.getUrl() != null) {
                    Log.d(TAG, "------ ActivityImagePreview(), url: " + mImages.getUrl() + " ------");

                    ImageLoaderUtils.displayImage(ActivityImagePreview.this, mImages.getUrl(), mPhotoView, R.mipmap.chatting_default_download_icon);
                }
            }

        } else {
            Log.d(TAG, "intent is not correct");
        }
    }

//    @Override
//    public boolean onMenuItemSelected(int featureId, MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                break;
//            default:
//                break;
//        }
//        return super.onMenuItemSelected(featureId, item);
//    }

    @Override
    protected void onResume() {
//        Notifier notifier = new Notifier(this);
//        notifier.cancel();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.leftImage) {
//            finish();
//        }
    }

    @Override
    public boolean onLongClick(View v) {

        if (v.getId() == R.id.photo_view) {
            String[] contents = getResources().getStringArray(R.array.list_dialog_picetur);
            int height = (int) (DisplayUtils.getScreenHeight() / 2.7);
            final ListDialog dialog = new ListDialog(this, -1, height, new ListDialog.ListItemClick() {
                @Override
                public void onItemClick(int position) {
                    if (position == 0) {
                        // save as
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mPhotoView.setDrawingCacheEnabled(true);
                                Bitmap bitmap = mPhotoView.getDrawingCache().copy(Bitmap.Config.RGB_565, false);
                                mPhotoView.setDrawingCacheEnabled(false);
                                int resId;
                                if (bitmap != null) {
                                    StringBuffer fileName = new StringBuffer();
                                    fileName.append(FileUtils.randomString(12));
                                    fileName.append(".png");
                                    File f = new File(FileUtils.getImageDir(), fileName.toString());
                                    try {
                                        FileOutputStream out = new FileOutputStream(f);
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                        out.flush();
                                        out.close();
                                        MediaScannerConnection.scanFile(ActivityImagePreview.this, new String[]{f.toString()}, null, null);
                                        resId = R.string.image_save_success;
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                        resId = R.string.image_save_failed;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        resId = R.string.image_save_failed;
                                    }
                                } else {
                                    resId = R.string.image_save_failed;
                                }
                                ToastUtil.showShortToast(resId);
                            }
                        });
                        thread.setName("ImageViewSaveImage");
                        thread.start();
                    } else if (position == 1) {
                        // cancel
                    }
                }
            });

            ArrayList<Object> items = new ArrayList<>();
            for (String item : contents) {
                items.add(item);
            }
            dialog.setTitleText("请选择");
            dialog.setListData(items);
            dialog.setCancelVisibility(true);
            dialog.show();
            return true;
        }
        return false;
    }

    public static class ImageInfo implements Parcelable {
        private String url;
        private String localPath;
        private String thumbnailPath;
        private String bigPath;
        private String msgId;
        private String datetime;
//        private String clickOriginal;

        public ImageInfo(String url, String localPath, String msgId, String datetime, String thumbnailPath, /*String clickOriginal, */String bigPath) {
            this.url = url;
            this.localPath = localPath;
            this.msgId = msgId;
            this.datetime = datetime;
            this.thumbnailPath = thumbnailPath;
//            this.clickOriginal = clickOriginal;
            this.bigPath = bigPath;
        }

        public String getUrl() {
            return url;
        }

        public String getBigPath() {
            return bigPath;
        }

        public void setBigPath(String bigPath) {
            this.bigPath = bigPath;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLocalPath() {
            return localPath;
        }

        public void setLocalPath(String localPath) {
            this.localPath = localPath;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getThumbnailPath() {
            return thumbnailPath;
        }

//        public String getClickOriginal() {
//            return clickOriginal;
//        }
//
//        public void setClickOriginal(String clickOriginal) {
//            this.clickOriginal = clickOriginal;
//        }

        public void setThumbnailPath(String thumbnailPath) {
            this.thumbnailPath = thumbnailPath;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel arg0, int arg1) {
            arg0.writeString(this.url);
            arg0.writeString(this.localPath);
            arg0.writeString(this.msgId);
            arg0.writeString(this.datetime);
            arg0.writeString(this.thumbnailPath);
//            arg0.writeString(this.clickOriginal);
            arg0.writeString(this.bigPath);
        }

        public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {

            @Override
            public ImageInfo createFromParcel(Parcel arg0) {
                return new ImageInfo(arg0.readString(), arg0.readString(),
                        arg0.readString(), arg0.readString(), arg0.readString(),/* arg0.readString(),*/ arg0.readString());
            }

            @Override
            public ImageInfo[] newArray(int arg0) {
                return new ImageInfo[arg0];
            }
        };
    }
}