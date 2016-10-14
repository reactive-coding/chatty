package com.reactiveapps.chatty.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.view.dialog.ProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentLoading.OnFragmentLoadingListener} interface
 * to handle interaction events.
 * Use the {@link FragmentLoading#newInstance} factory method to
 * create an instance of this fragment.
 *
 *  用法:
 *  显示:
 * loadingFragment = FragmentLoading.newInstance("Alert", "This is the Alter Message for test!");
 *   FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
 *  loadingFragment.show(ft, FragmentLoading.class.getName());
 *  消失:
 *  if (null != loadingFragment){
 *       loadingFragment.dismissAllowingStateLoss();
 *  }
 *
 */
public class FragmentLoading extends DialogFragment {
    private final static String TAG = FragmentLoading.class.getName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentLoadingListener mListener;

//    private static FragmentLoading fragment = new FragmentLoading();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLoadingNoCancel.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentLoading newInstance(String param1, String param2) {
        Log.d(TAG, "------ newInstance() ------>");
        FragmentLoading fragment = new FragmentLoading();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        Log.d(TAG, "<------ newInstance() ------");
        return fragment;
    }

    public static FragmentLoading newInstance() {
        Log.d(TAG, "------ newInstance() ------>");
        FragmentLoading fragment = new FragmentLoading();
        Log.d(TAG, "<------ newInstance() ------");
        return fragment;
    }

    public FragmentLoading() {
        // Required empty public constructor
        Log.d(TAG, "------ FragmentLoading() ------>");
        Log.d(TAG, "<------ FragmentLoading() ------");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "------ onCreate() ------>");
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

//        setCancelable(false);
//        int style = DialogFragment.STYLE_NO_TITLE, theme = 0;

        /**
         * 如果是使用Fragment显示Dialog则这个方法必须要在onCreateView之前调用
         * 如果是直接使用Dialog则无所谓调用,因为必须要在onCreateDialog中实现自己的Dialog.
         */
//        setStyle(style,theme);
        Log.d(TAG, "<------ onCreate() ------");
    }

    /**
     * Fragment实现对话框要重写这个方法来处理UI
     * 这个方法和onCreateDialog只能二选一
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "------ onCreateView() ------>");
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_loading, container, false);
        Log.d(TAG, "<------ onCreateView() ------");
        return null;
    }

    /**
     * 通过Dialog方式实现对话框
     * 这个方法和onCreateView只能二选一
     * onCreateDialog先于onCreateView执行.
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "------ onCreateDialog() ------>");
        ProgressDialog dialog = new ProgressDialog(this.getActivity(), R.style.Theme_My_NoDimDialog);
        View progress = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_progress, null);
        dialog.setContentView(progress);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(this);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparentBackground);
        Log.d(TAG, "<------ onCreateDialog() ------");
        return dialog;
//        return super.onCreateDialog(savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onBackPressedEvent() {
        Log.d(TAG, "------ onBackPressedEvent() ------>");
        if (mListener != null) {
            mListener.onFragmentLoadingBackPressed();
        }
        Log.d(TAG, "<------ onBackPressedEvent() ------");
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "------ onAttach() ------>");
        super.onAttach(activity);
        /**
         * 该fragment 仅仅是显示一个loading,因此不必让Activity去实现这个接口来通信
         */
//        try {
//            mListener = (OnFragmentLoadingListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement OnFragmentLoadingListener");
//        }
        Log.d(TAG, "<------ onAttach() ------");

    }

    @Override
    public void onDetach() {
        Log.d(TAG, "------ onDetach() ------>");
        super.onDetach();
        mListener = null;
        Log.d(TAG, "<------ onDetach() ------");
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(TAG, "------ onCancel() ------>");
        onBackPressedEvent();
        Log.d(TAG, "<------ onCancel() ------");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with BeanMyOrder Fragments</a> for more information.
     */
    public interface OnFragmentLoadingListener {
        // TODO: Update argument type and name
        void onFragmentLoadingBackPressed();
    }

}
