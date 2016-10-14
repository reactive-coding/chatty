package com.reactiveapps.chatty.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.reactiveapps.chatty.App;
import com.reactiveapps.chatty.R;

public class PermissionsUtil implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = PermissionsUtil.class.getSimpleName();
    private static PermissionsUtil mInstance;
    private OnRequestPermissionsListener mOnRequestPermissionsListener;

    public static synchronized PermissionsUtil getInstance() {
        if (mInstance == null) {
            mInstance = new PermissionsUtil();
        }
        return mInstance;
    }

    public interface OnRequestPermissionsListener {
        void onRequestPermissionsSuccess();

        void onRequestPermissionsFailured();
    }

    public void setonRequestPermissionsListener(OnRequestPermissionsListener listener) {
        this.mOnRequestPermissionsListener = listener;
    }

    /**
     * 检查权限
     */
    public int checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission);
    }

    /***
     * 是否有该权限
     */
    public boolean hasPermission(Context context, String permission) {
        boolean hasPermission = false;
        int mPermission = ContextCompat.checkSelfPermission(context, permission);
        if (mPermission == PackageManager.PERMISSION_GRANTED) {
            hasPermission = true;
        }
        return hasPermission;
    }

    /***
     * public int checkPermission(Context context, String permission) {
     * return ContextCompat.checkSelfPermission(context, permission);
     * }
     * <p/>
     * /**
     * 是否需要展示请求权限的提示
     */
    public boolean shouldShowRequestPermissionRationale(Activity activity, String permission, Object obj) {
        boolean isShow = false;
        if (obj instanceof Activity) {
            isShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        } else if (obj instanceof Fragment) {
            Fragment fragment = (Fragment) obj;
            isShow = fragment.shouldShowRequestPermissionRationale(permission);
        }
        return isShow;
    }

    /**
     * 申请权限
     */
    public void requestcheckPermission(Activity activity, String[] permission, int requestCode, Object obj) {
        if (obj instanceof Activity) {
            ActivityCompat.requestPermissions(activity, permission, requestCode);
        } else if (obj instanceof Fragment) {
            Fragment fragment = (Fragment) obj;
            fragment.requestPermissions(permission, requestCode);
        }
    }

    /**
     * Activity申请权限回调，fragment自己实现回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults != null) {
            for (int i = 0; i < grantResults.length; i++) {
                int grantResult = grantResults[i];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    /** permission  granted*/
                    if (mOnRequestPermissionsListener != null) {
                        Log.d(TAG, " ------onRequestPermissionsResult.onRequestPermissionsSuccess------");
                        mOnRequestPermissionsListener.onRequestPermissionsSuccess();
                    }
                } else {
                    /**  permission denied*/
                    if (mOnRequestPermissionsListener != null) {
                        Log.d(TAG, " ------onRequestPermissionsResult.onRequestPermissionsFailured------");
                        mOnRequestPermissionsListener.onRequestPermissionsFailured();
                    }
                }
            }
        }
    }

    private Dialog dialog;

    /**
     * 申请一個权限
     */
    public void requestPermission(final Activity activity, final String permission, final int requestCode, final Object object) {

        int mPermission = PermissionsUtil.getInstance().checkPermission(App.getInst().getApplicationContext(), permission);
        if (mPermission != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(activity, permission, object)) {
                Log.d(TAG, " ------shouldShowRequestPermissionRationale------");
                dialog = DialogFactory.createDialogWithStyle2(activity, getDialogTitle(getPermissionName(permission)), App.getInst().getApplicationContext().getString(R.string.permission_dialog_cacel),
                        App.getInst().getApplicationContext().getString(R.string.permission_dialog_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int id = v.getId();
                                if (id == R.id.dialog_permission_pos_button) {
                                    dialog.dismiss();
                                    ToastUtil.showLongToast(App.getInst().getApplicationContext().getString(R.string.permission_dialog_title2, getPermissionName(permission)));
                                } else if (id == R.id.dialog_permission_neg_button) {
                                    dialog.dismiss();
                                    requestcheckPermission(activity, new String[]{permission}, requestCode, object);
                                }
                            }
                        });
                dialog.show();
            } else {
                Log.d(TAG, " ------requestcheckPermission------");
                requestcheckPermission(activity, new String[]{permission}, requestCode, object);
            }
        } else {
            if (mOnRequestPermissionsListener != null) {
                mOnRequestPermissionsListener.onRequestPermissionsSuccess();
            }
        }
    }

    /**
     * 申请多個权限，彈出框優先顯示第一個權限名字
     */
    public void requestPermissions(final Activity activity, final String[] permission, final int requestCode, final Object object) {
        for (String pm : permission) {
            Log.d("permission:", pm);
            int mPermission = PermissionsUtil.getInstance().checkPermission(App.getInst().getApplicationContext(), pm);
            if (mPermission != PackageManager.PERMISSION_GRANTED) {
                if (PermissionsUtil.shouldShowRationale(activity, permission)) {
                    dialog = DialogFactory.createDialogWithStyle2(activity, getDialogTitle(getPermissionNames(permission)), App.getInst().getApplicationContext().getString(R.string.permission_dialog_cacel),
                            App.getInst().getApplicationContext().getString(R.string.permission_dialog_ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int id = v.getId();
                                    if (id == R.id.dialog_permission_pos_button) {
                                        dialog.dismiss();
                                        ToastUtil.showLongToast(App.getInst().getApplicationContext().getString(R.string.permission_dialog_title2, getPermissionNames(permission)));
                                    } else if (id == R.id.dialog_permission_neg_button) {
                                        dialog.dismiss();
                                        requestcheckPermission(activity, permission, requestCode, object);
                                    }
                                }
                            });
                    dialog.show();
                } else {
                    requestcheckPermission(activity, permission, requestCode, object);
                }
                return;
            }
        }
        if (mOnRequestPermissionsListener != null) {
            mOnRequestPermissionsListener.onRequestPermissionsSuccess();
        }
    }

    /**
     * 是否是6.0以上版本
     *
     * @return
     */
    public boolean isMNC() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static String getPermissionName(String permission) {
        String name;
        switch (permission) {
            case Manifest.permission.READ_PHONE_STATE:
                name = App.getInst().getApplicationContext().getString(R.string.permission_read_phone_state);
                break;
            case Manifest.permission.ACCESS_FINE_LOCATION:
                name = App.getInst().getApplicationContext().getString(R.string.permission_location);
                break;
            case Manifest.permission.READ_CONTACTS:
                name = App.getInst().getApplicationContext().getString(R.string.permission_read_contacts);
                break;
            case Manifest.permission.CAMERA:
                name = App.getInst().getApplicationContext().getString(R.string.permission_camera);
                break;
            case Manifest.permission.RECORD_AUDIO:
                name = App.getInst().getApplicationContext().getString(R.string.permission_audio);
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                name = App.getInst().getApplicationContext().getString(R.string.permission_storage);
                break;
            default:
                name = "";
                break;
        }
        return name;
    }

    public static String getPermissionNames(String[] permissions) {
        String name = "";
        for (String permission : permissions) {
            switch (permission) {
                case Manifest.permission.READ_PHONE_STATE:
                    name = App.getInst().getApplicationContext().getString(R.string.permission_read_phone_state);
                    break;
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    name = App.getInst().getApplicationContext().getString(R.string.permission_location);
                    break;
                case Manifest.permission.READ_CONTACTS:
                    name = App.getInst().getApplicationContext().getString(R.string.permission_read_contacts);
                    break;
                case Manifest.permission.CAMERA:
                    if (!TextUtils.isEmpty(name)) {
                        name = name.concat("、").concat(App.getInst().getApplicationContext().getString(R.string.permission_camera));
                    } else {
                        name = App.getInst().getApplicationContext().getString(R.string.permission_camera);
                    }
                    break;
                case Manifest.permission.RECORD_AUDIO:
                    if (!TextUtils.isEmpty(name)) {
                        name = name.concat("、").concat(App.getInst().getApplicationContext().getString(R.string.permission_audio));
                    } else {
                        name = App.getInst().getApplicationContext().getString(R.string.permission_audio);
                    }
                    break;
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    if (!TextUtils.isEmpty(name)) {
                        name = name.concat("、").concat(App.getInst().getApplicationContext().getString(R.string.permission_storage));
                    } else {
                        name = App.getInst().getApplicationContext().getString(R.string.permission_storage);
                    }

                    break;
                default:
                    name = "";
                    break;
            }
        }

        return name;
    }

    public String getDialogTitle(String permissionName) {
        String title = "";
        if (App.getInst().getApplicationContext().getString(R.string.permission_read_phone_state).equals(permissionName)) {
            title = App.getInst().getApplicationContext().getString(R.string.permission_dialog_title, permissionName);
        } else {
            title = App.getInst().getApplicationContext().getString(R.string.permission_dialog_title1, permissionName);
        }
        return title;
    }

    /**
     * 是否应该提示用户，我们需要权限的原因
     */
    public static boolean shouldShowRationale(Activity activity, String[] permissionsNeeded) {
        boolean result = false;
        for (String per : permissionsNeeded) {
            result = result | ActivityCompat.shouldShowRequestPermissionRationale(activity, per);
        }
        return result;
    }

}
