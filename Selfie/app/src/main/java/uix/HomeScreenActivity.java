/*
    This is the app launcher point.
 */
package uix;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.util.Log;

import cse110.selfie.app.UI.R;
import database.ItemDataSource;

/**
 * Created by JuanJ on 4/28/2014.
 * Controller for the Home activity
 */

public class HomeScreenActivity extends FragmentActivity {

    WeightController weightController;

    public CategoryFragment categoryFragment = new CategoryFragment();
    public SpecialTabFragment specialTabFragment = new SpecialTabFragment();
    public OrderFragment orderFragment = new OrderFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        weightController = new WeightController(this);
        ItemDataSource itemDataSource = new ItemDataSource(this);
        try {
            itemDataSource.setUp();
        } catch (Exception e) {
            Log.e("ITEMDATASOURCE", "SETUP EXCEPTION");
        }

        Test.init(4);
        init();

        FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();

        //renders category list and special tab
        fTransaction.replace(R.id.MSfragment_listContainer, categoryFragment)
                .replace(R.id.MSfragment_detailContainer, specialTabFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("Home")
                .commit();
    }

    //listener for back and checkout and waiter ping buttons
    public void myButtonListener(View view) {
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();

        switch (view.getId()) {
            //does nothing for now
            case R.id.MS_alert:
                new AlertDialog.Builder(this)
                        .setTitle("Confirmation").setMessage("A Waiter Have Been Notified")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
                break;
            case R.id.MS_home_button:
                if(fManager.getBackStackEntryAt(
                        fManager.getBackStackEntryCount()-1).getName() != "Home") {
                    fTransaction.replace(R.id.MSfragment_detailContainer, specialTabFragment)
                            .replace(R.id.MSfragment_listContainer, categoryFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack("Home")
                            .commit();
                    weightController.changeLayoutWeight(0);
                }
                break;
            case R.id.MS_checkout_button:
                //render checkout screen
                if(fManager.getBackStackEntryAt(
                    fManager.getBackStackEntryCount()-1).getName() != "Order") {
                    fTransaction.replace(R.id.MSfragment_listContainer, orderFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack("Order")
                            .commit();
                    weightController.changeLayoutWeight(2);
                }
                break;
            case R.id.MS_back_button:
                FragmentManager.BackStackEntry mPrevious = fManager.getBackStackEntryAt(
                        fManager.getBackStackEntryCount()-2);
                if(fManager.getBackStackEntryCount() > 1) {
                    fManager.popBackStack();
                    if(mPrevious.getName() == "Home") {
                        weightController.changeLayoutWeight(0);
                    }
                    else if(mPrevious.getName().startsWith("Menu ")) {
                        fManager.popBackStack();
                        weightController.changeLayoutWeight(0);
                    }
                    else if(mPrevious.getName() == "Order") {
                        weightController.changeLayoutWeight(2);
                    }
                    else if(mPrevious.getName().startsWith("Detail ")) {
                        weightController.changeLayoutWeight(1);
                    }

                    //clears stack if 10 screens have been added
                    if (fManager.getBackStackEntryCount() > 10) {
                        for(int i=0;i<fManager.getBackStackEntryCount()-2;i++)
                            fManager.popBackStack();
                        weightController.changeLayoutWeight(0);
                    }
                }
                else {
                    moveTaskToBack(true);
                }
                break;
        }
    }

    //for my (Juan's) testing only
    public void init() {
        Test.add(0, new item("Triple Dipper", 1, 0, 10.79f, true));
        Test.add(0, new item("Southwestern Eggrolls", 2, 0, 8.29f, true));
        Test.add(0, new item("Loaded Potato Skins", 3, 0, 7.09f, false));
        Test.add(0, new item("Classic Nachos", 4, 0, 7.69f, true));
        Test.add(1, new item("Bacon Burger", 17, 1, 9.59f, false));
        Test.add(1, new item("Cheese Burger", 18, 1, 8.59f, true));
        Test.add(1, new item("Veggie Burger", 19, 1, 7.59f, true));
        Test.add(1, new item("Bacon Cheese Burger", 20, 1, 11.59f, false));
        Test.add(2, new item("Chocolate Cake", 21, 2, 6.29f, false));
        Test.add(2, new item("Chocolate Ice Cream", 22, 2, 6.09f, false));
        Test.add(2, new item("Chocolate Cheesecake", 23, 2, 7.39f, true));
        Test.add(3, new item("Apple Juice", 24, 3, 3.43f, true));
        Test.add(3, new item("Mango Juice", 25, 3, 3.43f, false));
        Test.add(3, new item("Orange Juice", 26, 3, 3.43f, false));
    }
}