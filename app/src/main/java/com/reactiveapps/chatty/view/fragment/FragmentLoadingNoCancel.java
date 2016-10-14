package com.reactiveapps.chatty.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reactiveapps.chatty.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentLoadingNoCancel.OnFragmentLoadingListener} interface
 * to handle interaction events.
 * Use the {@link FragmentLoadingNoCancel#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLoadingNoCancel extends DialogFragment {
    private static final String TAG = FragmentLoadingNoCancel.class.getName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
//    private OnFragmentLoadingListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLoadingNoCancel.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentLoadingNoCancel newInstance(String param1, String param2) {
        Log.d(TAG, "------ newInstance() ------>");
        FragmentLoadingNoCancel fragment = new FragmentLoadingNoCancel();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        Log.d(TAG, "<------ newInstance() ------");
        return fragment;
    }

    public FragmentLoadingNoCancel() {
        // Required empty public constructor
        Log.d(TAG, "------ FragmentLoadingNoCancel() ------>");
        Log.d(TAG, "<------ FragmentLoadingNoCancel() ------");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "------ onCreate() ------>");
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        setCancelable(false);
        int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
        setStyle(style,theme);
        Log.d(TAG, "<------ onCreate() ------");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "------ onCreateView() ------>");
        // Inflate the layout for this fragment

        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event){
                if (keyCode == KeyEvent.KEYCODE_BACK)
                    return true; // pretend we've processed it
                else
                    return false; // pass on to be processed as normal
            }
        });

        // Inflate the layout for this fragment
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_loading, container, false);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，
        // 要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        Log.d(TAG, "<------ onCreateView() ------");
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onBackPressedEvent() {
//        if (mListener != null) {
//            mListener.onFragmentLoadingBackPressed();
//        }
//    }

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
//        mListener = null;
        Log.d(TAG, "<------ onDetach() ------");
    }

//    @Override
//    public void onCancel(DialogInterface dialog) {
//        super.onCancel(dialog);
//        onBackPressedEvent();
//    }
//
//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//        onBackPressedEvent();
//    }

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
        public void onFragmentLoadingBackPressed();
    }

}
