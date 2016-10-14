package com.reactiveapps.chatty.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.reactiveapps.chatty.R;

public class DialogFactory {

    @SuppressLint("ServiceCast")
    public static Dialog creatRequestDialog(final Context context, int layout) {
        final Dialog dialog = new Dialog(context, R.style.CustomProgressDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(layout);
        return dialog;
    }

//    public static void creatOffLineDialog(final Activity activity, String msg) {
//        AlertDialog.Builder builder = new Builder(activity);
//        TextView title = new TextView(activity);
//        title.setText(msg);
//        title.setPadding(10, 10, 10, 10);
//        title.setGravity(Gravity.CENTER);
//        title.setTextColor(Color.WHITE);
//        title.setTextSize(20);
//
//        builder.setCustomTitle(title);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                try {
//                    ActivityManagerUtil.getInstance().finishAllActivity();
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.setCancelable(false);
//        dialog.show();
//    }

    /**
     * 两个底部按钮和一个message文案
     *
     * @param context
     * @param message         提示消息内容
     * @param leftButtonText  左边按钮内容
     * @param rightButtonText 右边按钮内容
     * @param onClickListener
     * @return
     * @throws IllegalArgumentException
     */
    public static Dialog createDialogWithStyle2(Context context, CharSequence message, String leftButtonText, String rightButtonText, View.OnClickListener onClickListener) throws IllegalArgumentException {
        if (context == null) {
            throw new IllegalArgumentException("the param context can not be null in this dialog style");
        } else if (TextUtils.isEmpty(message)) {
            throw new IllegalArgumentException("the param message can not be empty in this dialog style");
        } else if (TextUtils.isEmpty(leftButtonText)) {
            throw new IllegalArgumentException("the param leftButtonText can not be empty in this dialog style");
        } else if (TextUtils.isEmpty(rightButtonText)) {
            throw new IllegalArgumentException("the param rightButtonText can not be empty in this dialog style");
        } else {
            final Dialog dialog = new Dialog(context, R.style.dialog_permission);
            dialog.setContentView(R.layout.dialog_permission);
            dialog.setCanceledOnTouchOutside(false);
            TextView messageView = (TextView) dialog.findViewById(R.id.dialog_permission_message);
            messageView.setText(message);
            Button leftButton = (Button) dialog.findViewById(R.id.dialog_permission_pos_button);
            leftButton.setText(leftButtonText);
            leftButton.setOnClickListener(onClickListener);
            Button rightButton = (Button) dialog.findViewById(R.id.dialog_permission_neg_button);
            rightButton.setText(rightButtonText);
            rightButton.setOnClickListener(onClickListener);
            return dialog;
        }
    }

    /**
     * 一个按钮和一个message文案
     *
     * @param context
     * @param message         提示消息内容
     * @param buttonText      按钮内容
     * @param onClickListener
     * @return
     * @throws IllegalArgumentException
     */
    public static Dialog createDialogWithStyle1(Context context, CharSequence message, String buttonText, View.OnClickListener onClickListener) throws IllegalArgumentException {

        if (context == null) {
            throw new IllegalArgumentException("the param context can not be null in this dialog style");
        }

        if (TextUtils.isEmpty(message)) {
            throw new IllegalArgumentException("the param message can not be empty in this dialog style");
        }

        if (TextUtils.isEmpty(buttonText)) {
            throw new IllegalArgumentException("the param buttonText can not be empty in this dialog style");
        }

        final Dialog dialog = new Dialog(context, R.style.dialog_permission);
        dialog.setContentView(R.layout.layout_dialog_offline);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView messageView = (TextView) dialog.findViewById(R.id.dialog_permission_message);
        messageView.setText(message);

        Button posButton = (Button) dialog.findViewById(R.id.dialog_permission_pos_button);
        posButton.setText(buttonText);
        posButton.setOnClickListener(onClickListener);

        return dialog;
    }
}
