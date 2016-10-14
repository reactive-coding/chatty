package com.reactiveapps.chatty.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reactiveapps.chatty.R;
import com.reactiveapps.chatty.model.bean.LastMessage;
import com.reactiveapps.chatty.utils.DateUtils;
import com.reactiveapps.chatty.view.ActivityUtils;
import com.reactiveapps.chatty.view.adapter.AdapterChats;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentChatListListener} interface
 * to handle interaction events.
 * Use the {@link FragmentChats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentChats extends FragmentBase {

    private String TAG = FragmentChats.class.getName();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mChats;
    private LinearLayoutManager mLayoutManager;
    private AdapterChats mChatsAdapter;

    private HashMap<String, Object> mMap = new HashMap<>();
    private ArrayList<Object> mChatsList = new ArrayList<>();
    private boolean mIsLoading;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentChatListListener mListener;

    public FragmentChats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentChats.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentChats newInstance(String param1, String param2) {
        FragmentChats fragment = new FragmentChats();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        createMockData();
    }

    private void initView(View rootView) {
        Log.d(TAG, "initView() ------>");
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.chat_list_srl);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(R.color.dodgerblue, R.color.goldenrod, R.color.chartreuse, R.color.tomato);
        /**
         * refresh disabled
         */
        mSwipeRefreshLayout.setEnabled(false);

        mChats = (RecyclerView) rootView.findViewById(R.id.chat_rv);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mChats.setLayoutManager(mLayoutManager);
        mChats.setItemAnimator(new DefaultItemAnimator());
        mChatsAdapter = new AdapterChats(this, mChatsList);
        mChatsAdapter.enableFooter(false);
        mChats.setAdapter(mChatsAdapter);
        mChatsAdapter.setOnItemClickListener(new AdapterChats.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick(View view, int position) ------>");
                ActivityUtils.showActivityChatting(FragmentChats.this.getActivity(), null);
                Log.d(TAG, "onItemClick(View view, int position) <------");
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.d(TAG, "onItemLongClick(View view, int position) ------>");
                try {

                } catch (Exception e) {
                    Log.e(TAG, "exception caught ", e);
                }
                Log.d(TAG, "onItemLongClick(View view, int position) <------");
            }
        });

        Log.d(TAG, "initView() <------");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int which) {
        if (mListener != null) {
            mListener.onFragmentInteraction(which);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentChatListListener) {
            mListener = (OnFragmentChatListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentContactsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentChatListListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int which);
    }

    private void createMockData(){
        for (int i = 0; i < 20; i++) {
            addMessageToChats(createMessage("Danny Mao"+i,"Nick", "So what r u want to do?"+i));
        }
    }

    private LastMessage createMessage(String from, String to, String content){
        LastMessage lastMessage = new LastMessage();
        lastMessage.from = from;
        lastMessage.to = to;
        lastMessage.content = content;
        lastMessage.time = System.currentTimeMillis();
        return lastMessage;
    }

    private void addMessageToChats(LastMessage lastMessage){
        mChatsList.add(lastMessage);
        mChatsAdapter.notifyDataSetChanged();
    }
}
