/*
    This is the app launcher point.
 */
package cse110.selfie.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by JuanJ on 4/28/2014.
 */

// mCurrentFragment: HOME, CHECKOUT, CLASSIC, INTERACTIVE
public class HomeScreenActivity extends FragmentActivity implements CategoryFragment.onItemSelectedListener {

    public String mCurrentFragment = "HOME";
    public String mPreviousFragment = "null";

    public FrameLayout list, detail;

    public CategoryFragment categoryFragment = new CategoryFragment();
    public SpecialTabFragment specialTabFragment = new SpecialTabFragment();
    public CheckoutFragment checkoutFragment = new CheckoutFragment();

    @Override
    //on Category selected
    public void onItemSelected(int itemId) {
        Bundle arguments = new Bundle();
        arguments.putInt(MenuItemList.ARG_ITEM_ID, itemId);

        MenuItemList menu = new MenuItemList();
        menu.setArguments(arguments);

        DetailFragment details = new DetailFragment();

        FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
        fTransaction.replace(R.id.MSfragment_listContainer, menu)
                .replace(R.id.MSfragment_detailContainer, details)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();

        //place the main 2 fragments
        fTransaction.replace(R.id.MSfragment_listContainer, categoryFragment)
                .replace(R.id.MSfragment_detailContainer, specialTabFragment)
                .addToBackStack(null)
                .commit();
    }

    /*
        set layout ratios
        0 = 1:4 category & specials' tab
        1 = 1:1 menu list & item's detail
        2 = 1:0 checkout
    */
    public void changeLayoutWeight (int changeType) {
        float frag1 = 1.0f, frag2 = 4.0f;
        list = (FrameLayout) findViewById(R.id.MSfragment_listContainer);
        detail = (FrameLayout) findViewById(R.id.MSfragment_detailContainer);

        switch(changeType) {
            case 0:
                frag1 = 1.0f; frag2 = 4.0f;
                Log.e("LAYOUT", "CASE 0"); break;
            case 1:
                frag1 = 1.0f; frag2 = 1.0f;
                Log.e("LAYOUT", "CASE 1"); break;
            case 2:
                frag1 = 1.0f; frag2 = 0.0f;
                Log.e("LAYOUT", "CASE 2"); break;
        }
        list.setLayoutParams(
             new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, frag1));
        detail.setLayoutParams(
             new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, frag2));

    }

    //listener for back and checkout
    public void myButtonListener(View view) {
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();

        Button mCurrentView = (Button) findViewById(R.id.MS_view_button);
        Fragment listFragment = fManager.findFragmentById(R.id.MSfragment_listContainer);
        Fragment detailFragment = fManager.findFragmentById(R.id.MSfragment_detailContainer);

        switch (view.getId()) {
            case R.id.MS_view_button:
                if(mCurrentFragment == "CLASSIC") {
                    Log.e("BUTTONLISTENER", "NOW INTERACTIVE");
                    mPreviousFragment = mCurrentFragment;
                    mCurrentFragment = "INTERACTIVE";
                    changeLayoutWeight(0);
                    mCurrentView.setText("Classic");
                }
                else {
                    Log.e("BUTTONLISTENER", "NOW CLASSIC");
                    MenuItemList ml = new MenuItemList();
                    mPreviousFragment = mCurrentFragment;
                    mCurrentFragment = "CLASSIC";
                    changeLayoutWeight(1);
                    mCurrentView.setText("Interactive");
                }
                break;
            case R.id.MS_checkout_button:
                Log.e("BUTTONLISTENER", "NOW CHECKOUT");
                changeLayoutWeight(2);
                fTransaction.replace(R.id.MSfragment_listContainer, checkoutFragment)
                        .addToBackStack(null).commit();
                mPreviousFragment = mCurrentFragment;
                mCurrentFragment = "CHECKOUT";
                break;
            case R.id.MS_back_button:
                if(fManager.getBackStackEntryCount() > 1
                        && (mCurrentFragment != "CLASSIC")) {
                    fManager.popBackStack();
                    if (fManager.getBackStackEntryCount() == 2) {
                        if (mCurrentFragment == "CHECKOUT") {
                            if (mPreviousFragment == "HOME") {
                                Log.e("BUTTONLISTENER", "NOW HOME");
                                changeLayoutWeight(0);
                                mPreviousFragment = mCurrentFragment;
                                mCurrentFragment = "HOME";
                            } else if (mPreviousFragment == "INTERACTIVE" || mPreviousFragment == "CLASSIC") {
                                Log.e("BUTTONLISTENER", "NOW "+ mCurrentView.getText().toString());
                                changeLayoutWeight(1);
                                mPreviousFragment = mCurrentFragment;
                                mCurrentFragment = mCurrentView.getText().toString();
                            }
                        }
                    }
                    if (fManager.getBackStackEntryCount() > 10) {
                        for(int i=0;i<fManager.getBackStackEntryCount()-2;i++)
                            fManager.popBackStack();
                        changeLayoutWeight(0);
                    }
                }
                break;
        }
    }
}