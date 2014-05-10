/*
    This is the app launcher point.
 */
package cse110.selfie.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.Stack;

/**
 * Created by JuanJ on 4/28/2014.
 */

// mCurrentFragment: HOME, CHECKOUT, CLASSIC
public class HomeScreenActivity extends FragmentActivity implements CategoryFragment.onItemSelectedListener {

    public FrameLayout list, detail;

    public CategoryFragment categoryFragment = new CategoryFragment();
    public SpecialTabFragment specialTabFragment = new SpecialTabFragment();
    public CheckoutFragment checkoutFragment = new CheckoutFragment();

    @Override
    //on Category selected
    public void onItemSelected(int itemId) {
        Bundle arguments = new Bundle();
        arguments.putInt(MenuItemList.ARG_CATEGORY_ID, itemId);

        MenuItemList menu = new MenuItemList();
        menu.setArguments(arguments);

        DetailFragment details = new DetailFragment();

        //renders menu of category selected and first item details
        FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
        fTransaction.replace(R.id.MSfragment_listContainer, menu)
                .replace(R.id.MSfragment_detailContainer, details)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("Menu 1")
                .commit();
        changeLayoutWeight(1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();

        //renders category list and special tab
        fTransaction.replace(R.id.MSfragment_listContainer, categoryFragment)
                .replace(R.id.MSfragment_detailContainer, specialTabFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("Home")
                .commit();
    }

    //sets the sizes of the layouts depending on the screens
    public void changeLayoutWeight (int changeType) {
        float frag1 = 1.0f, frag2 = 4.0f;
        list = (FrameLayout) findViewById(R.id.MSfragment_listContainer);
        detail = (FrameLayout) findViewById(R.id.MSfragment_detailContainer);

        switch(changeType) {
            case 0:
                frag1 = 1.0f; frag2 = 4.0f;
                break;
            case 1:
                frag1 = 1.0f; frag2 = 1.0f;
                break;
            case 2:
                frag1 = 1.0f; frag2 = 0.0f;
                break;
        }
        list.setLayoutParams(
             new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, frag1));
        detail.setLayoutParams(
             new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, frag2));

    }


    //listener for back and checkout and waiter ping buttons
    public void myButtonListener(View view) {
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();

        switch (view.getId()) {
            //does nothing for now
            case R.id.MS_alert:
                Button alert = (Button) findViewById(R.id.MS_alert);
                alert.setText("Waiting...");
                break;
            case R.id.MS_checkout_button:
                //render checkout screen
                fTransaction.replace(R.id.MSfragment_listContainer, checkoutFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack("Checkout")
                        .commit();
                changeLayoutWeight(2);
                break;
            case R.id.MS_back_button:
                //get previous screen to determine the size of the layout
                FragmentManager.BackStackEntry mPrevious = fManager.getBackStackEntryAt(
                        fManager.getBackStackEntryCount()-2);

                if(fManager.getBackStackEntryCount() > 1) {
                    fManager.popBackStack();
                    if(mPrevious.getName() == "Home") {
                        changeLayoutWeight(0);
                    }
                    else if(mPrevious.getName().startsWith("Menu ")) {
                        changeLayoutWeight(1);
                    }
                    //clears stack if 10 screens have been added
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