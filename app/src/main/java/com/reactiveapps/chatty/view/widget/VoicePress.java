package com.reactiveapps.chatty.view.widget;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.utils.DImenUtil;
import com.reactiveapps.chatty.view.fragment.FragmentChatInputBase;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/10.
 */
public class VoicePress extends FragmentChatInputBase {

    private final String TAG = VoicePress.class.getSimpleName();
    private View rootView = null;
    private TextView sayTextView;

    private PopupWindow popupWindow, popupCancelWindow;
    private View hintView, hintCancelView;

    private ImageView volume;
    private boolean hasPermission = false;

    private Handler mHandler = new Handler();
    private SpeechRecognizer mRecognizer;
    private String mVoiceName;
    private String voiceRecognizedResult;
    private int voiceDuration;
    private boolean voiceCanceled;
//    private RecognizerListener mRecognizeListener;
    private static final String VOICE_DIR
            = Environment.getExternalStorageDirectory().getPath() + "/baixingchat/";
    private voiceMessageListener listener = null;

    public static interface voiceMessageListener {
        public void onSendVoiceMessage(String path, int duration, String description);
    }

    public void setListener(voiceMessageListener listener){
        this.listener = listener;
    }

    @Override
    protected View onInitializeView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.voice_press, container, false);
        sayTextView = (TextView)rootView.findViewById(R.id.press_to_talk);
        init();
        return rootView;
    }

    private void init() {
        initHintView();
        initListeners();
//        initSpeechUtility();
    }

    private void initHintView() {
        Activity activity = this.getActivity();
        hintView = LayoutInflater.from(activity).inflate(R.layout.avos_input_voice, null);
        volume = (ImageView) hintView.findViewById(R.id.id_voice_amplitude);
        hintCancelView = LayoutInflater.from(activity).inflate(R.layout.avos_voice_cancel, null);
        popupWindow = new PopupWindow(activity);
        popupWindow.setContentView(hintView);
        popupWindow.setWidth(DImenUtil.dip2px(activity, 200)); popupWindow.setHeight(DImenUtil.dip2px(activity, 200));
        popupCancelWindow = new PopupWindow(activity);
        popupCancelWindow.setContentView(hintCancelView);
        popupCancelWindow.setWidth(DImenUtil.dip2px(activity, 200)); popupCancelWindow.setHeight(DImenUtil.dip2px(activity, 200));

    }

    private void initListeners() {
        sayTextView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float eventY = event.getY();
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (eventY < -100) {//手指在上面,应hint上滑取消
                        if (popupWindow.isShowing()) {
                            popupWindow.dismiss();
                            stopFrameAnim(volume);
                        }
                        if (popupCancelWindow != null && !popupCancelWindow.isShowing()) {
                            popupCancelWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                            startFrameAnim(volume);
                        }
                    } else {//手指在下面,应hint松开取消
                        if (popupCancelWindow.isShowing()) {
                            popupCancelWindow.dismiss();
                            stopFrameAnim(volume);
                        }
                        if (popupWindow != null && !popupWindow.isShowing()) {
                            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                            startFrameAnim(volume);
                        }
                    }
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        stopFrameAnim(volume);
                    }
                    if (popupCancelWindow.isShowing()) {
                        popupCancelWindow.dismiss();
                    }
                    ((TextView) v).setText("按住说话");
                    v.setBackgroundResource(R.drawable.talk_btn_bg);
                    stop();
                    if (eventY > -100 && hasPermission) {//未取消
                        voiceCanceled = false;
                    } else {
                        voiceCanceled = true;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    ((TextView) v).setText("松开结束");
                    v.setBackgroundResource(R.drawable.talk_btn_bg_press);
                    long startVoiceT = SystemClock.currentThreadTimeMillis();
                    mVoiceName = startVoiceT + "";
//                    Log.e("mVoiceName set to", mVoiceName);
                    start();
                }
                return true;
            }
        });
    }

    String getVoiceNameWithoutSuffix() {
        return mVoiceName;
    }
    private static final int POLL_INTERVAL = 300;
    private void start() {
//        Log.e("ChatInputLayout#start", "entering");
        voiceRecognizedResult = "";
        voiceDuration = 0;

        if (!hasPermission) {
            if (!judgePermission()) {
                return;
            }
        }
        /**
         *  Here will call the recording library and handle the local voice file,
         *  call the callback when finish the voice file, tell the ui to handle the UI logical.
        */
        listener.onSendVoiceMessage("path", 60, "Recoding voice description");
    }


    private void stop() {
        if (mRecognizer == null){
            return;
        }
    }

    private boolean judgePermission() {
        PackageInfo packageInfo;
        try {
            packageInfo = this.getActivity().getPackageManager().getPackageInfo(this.getActivity().getPackageName(), PackageManager.GET_PERMISSIONS);
            String permissions[] = packageInfo.requestedPermissions;
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < permissions.length; i++) {
                list.add(permissions[i]);
            }
            if (list.contains("android.permission.RECORD_AUDIO")) {
                hasPermission = true;
            } else {
                hasPermission = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasPermission;
    }

    private void updateDisplay(double signalEMA) {

        switch ((int) signalEMA) {
            case 0:
                volume.setImageResource(R.mipmap.amp1);
                break;
            case 1:
                volume.setImageResource(R.mipmap.amp2);

                break;
            case 2:
                volume.setImageResource(R.mipmap.amp3);
                break;
            case 4:
                volume.setImageResource(R.mipmap.amp4);
                break;
            case 5:
                volume.setImageResource(R.mipmap.amp5);
                break;
            case 6:
                volume.setImageResource(R.mipmap.amp6);
                break;
            default:
                volume.setImageResource(R.mipmap.amp7);
                break;
        }
    }

    private void startFrameAnim(ImageView imageView) {
        Log.d(TAG, "------ startFrameAnim() ------>");
        imageView.setImageResource(R.drawable.record_audio_play_anim);
        ((AnimationDrawable) imageView.getDrawable()).start();
        Log.d(TAG, "<------ startFrameAnim() ------");
    }

    private void stopFrameAnim(ImageView imageView) {
        Log.d(TAG, "------ stopFrameAnim() ------>");
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).stop();
            imageView.clearAnimation();
            imageView.setImageResource(R.mipmap.amp1);
        } else if (drawable instanceof BitmapDrawable) {
        }
        Log.d(TAG, "<------ stopFrameAnim() ------");
    }
}
