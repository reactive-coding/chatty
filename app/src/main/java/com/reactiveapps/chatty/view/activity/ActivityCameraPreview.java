package com.reactiveapps.chatty.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;


import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.utils.ImageLoaderUtils;
import com.reactiveapps.chatty.view.Base.ActivityBase;

@SuppressLint("NewApi")
public class ActivityCameraPreview extends ActivityBase implements OnClickListener {
    public static final String TAG = ActivityCameraPreview.class.getSimpleName();
    private ImageView mCameraImage;//相机预览
    private String mPath;//照相图片路径

    private static final String PATH_PREFIX = "file://";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_review);
        initView();
    }

    public void initView() {
//        TextView title = (TextView) findViewById(R.id.centerText);
//        title.setText(R.string.camera_preview_title);
//        title.setVisibility(View.VISIBLE);
//
//        ImageView back = (ImageView) findViewById(R.id.leftImage);
//        back.setVisibility(View.VISIBLE);
//        back.setImageResource(R.drawable.toolbar_btn_back_selector);
//        back.setOnClickListener(this);
//
//        TextView send = (TextView) findViewById(R.id.firstRightText);
//        send.setVisibility(View.VISIBLE);
//        send.setText(R.string.ok);
//        send.setOnClickListener(this);


        mCameraImage = (ImageView) findViewById(R.id.camera_image);
        Intent intent = getIntent();
        if (intent != null) {
            mPath = intent.getStringExtra("path");
            if (!TextUtils.isEmpty(mPath)) {
//                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(PATH_PREFIX + mPath))
//                        .setResizeOptions(new ResizeOptions(500, 500))
//                        .setAutoRotateEnabled(true).build();
//
//                PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
//                        .setOldController(mCameraImage.getController())
//                        .setImageRequest(request)
//                        .build();
//                mCameraImage.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
//                mCameraImage.setController(controller);
                ImageLoaderUtils.displayLocalImage(ActivityCameraPreview.this, mPath, mCameraImage);
            } else {
//                mCameraImage.setImageURI(null);
                ImageLoaderUtils.displayLocalImage(ActivityCameraPreview.this, R.mipmap.chatting_default_image_broke, mCameraImage, R.mipmap.chatting_default_image_broke);
            }
        }
    }

    @Override
    public void onClick(View v) {
//        if(v.getId() ==  R.id.firstRightText) {//发送
//            if (TextUtils.isEmpty(mPath)) {
//                ToastUtil.showShortToast(getString(R.string.not_found_image_path));
//                return;
//            }
//            Intent intent = new Intent();
//            File file = new File(mPath);
//            intent.putExtra("path", file.getAbsolutePath());//降质图path
//            setResult(RESULT_OK, intent);
//            finish();
//        }
//        if(v.getId() ==  R.id.leftImage) {//打开相机
//            finish();
//        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() >>>");
//        Notifier notifier = new Notifier(this);
//        notifier.cancel();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause() >>>");
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
}