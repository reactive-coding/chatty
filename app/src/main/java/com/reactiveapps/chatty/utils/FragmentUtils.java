package com.reactiveapps.chatty.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.List;

/**
 * Created by Tank on 16/2/25.
 */
public class FragmentUtils {
    private final static String TAG = "FragmentUtils";

    /**
     * Detach all other Fragments in this container, and show the specified Fragment, Only one Fragment is visible to user in a moment
     *
     * @param manager     the manager to apply
     * @param containerId the container id
     * @param fragment    the fragment to switch to
     */
    public static void switchToFragment(FragmentManager manager, int containerId, Fragment fragment) {
        switchToFragment(manager, containerId, fragment.getClass().getName(), fragment);
    }

    public static void switchToFragment(FragmentManager manager, int containerId, String tag, Fragment fragment) {
        List<Fragment> fragments = manager.getFragments();
        FragmentTransaction transaction = manager.beginTransaction();
        if (null != fragments) {
            Log.i(TAG, "------ Try to detach fragments ------ ");
            for (Fragment addedFragment : fragments) {
                if (null != addedFragment && containerId == addedFragment.getId() && fragment != addedFragment && !addedFragment.isDetached()) {
                    Log.i(TAG, "------ Detach fragment " + addedFragment.getClass().getSimpleName()+" ------");
                    //Detach all the Fragments with specified id
                    transaction.detach(addedFragment);
                }
            }
        }

        if (!fragment.isAdded() && !fragment.isDetached()) {
            //if the specified Fragment is not added and attached, then add it
            Log.i(TAG, "------ Add fragment " + fragment.getClass().getSimpleName()+" ------");
            transaction.add(containerId, fragment, tag);
        } else {
            //if the specified Fragment is added and not attached, then attach it
            Log.i(TAG, "------ Attach fragment " + fragment.getClass().getSimpleName()+" ------");
            transaction.attach(fragment);
        }
        transaction.commitAllowingStateLoss();
        Log.i(TAG, "------ Switch to fragment: " + fragment.getClass().getSimpleName()+" ------");
    }

}
