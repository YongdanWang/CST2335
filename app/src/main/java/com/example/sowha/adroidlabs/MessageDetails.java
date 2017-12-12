package com.example.sowha.adroidlabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Created by sowha on 2017-12-10.
 */

public class MessageDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_details);

        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        messageFragment.setArguments(bundle);

        FragmentManager fragmentManager =getFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.phoneFrameLayout, messageFragment).addToBackStack(null).commit();
    }
}
