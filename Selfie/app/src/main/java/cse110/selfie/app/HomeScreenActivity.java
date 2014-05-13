/*
    This is the app launcher point.
 */
package cse110.selfie.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by JuanJ on 4/28/2014.
 * Controller for the Home activity
 */

// mCurrentFragment: HOME, CHECKOUT, CLASSIC
public class HomeScreenActivity extends FragmentActivity {

    WeightController weightController;

    public CategoryFragment categoryFragment = new CategoryFragment();
    public SpecialTabFragment specialTabFragment = new SpecialTabFragment();
    public OrderFragment checkoutFragment = new OrderFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        weightController = new WeightController(this);
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
            case R.id.MS_checkout_button:
                //render checkout screen
                if(fManager.getBackStackEntryAt
                        (fManager.getBackStackEntryCount()-1).getName() != "Order") {
                    fTransaction.replace(R.id.MSfragment_listContainer, checkoutFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack("Order")
                            .commit();
                    weightController.changeLayoutWeight(2);
                }
                break;
            case R.id.MS_back_button:
                //get previous screen to determine the size of the layout
                FragmentManager.BackStackEntry mPrevious = fManager.getBackStackEntryAt(
                        fManager.getBackStackEntryCount()-2);

                if(fManager.getBackStackEntryCount() > 1) {
                    fManager.popBackStack();
                    if(fManager.getBackStackEntryAt
                            (fManager.getBackStackEntryCount()-1).getName() == "Home")
                        moveTaskToBack(true);

                    else if(mPrevious.getName() == "Home") {
                        weightController.changeLayoutWeight(0);
                    }
                    else if(mPrevious.getName().startsWith("Menu ")) {
                        weightController.changeLayoutWeight(1);
                    }

                    //clears stack if 10 screens have been added
                    if (fManager.getBackStackEntryCount() > 10) {
                        for(int i=0;i<fManager.getBackStackEntryCount()-2;i++)
                            fManager.popBackStack();
                        weightController.changeLayoutWeight(0);
                    }
                }
                else {}
                break;
        }
    }

    //for my (Juan's) testing only
    public void init() {
        Test.add(0, new item("Triple Dipper", 1, 0, 10.79f, true));
        Test.add(0, new item("Southwestern Eggrolls", 2, 0, 8.29f, true));
        Test.add(0, new item("Loaded Potato Skins", 3, 0, 7.09f, false));
        Test.add(0, new item("Classic Nachos", 4, 0, 7.69f, true));
        Test.add(0, new item("Triple Dipper", 5, 0, 10.79f, true));
        Test.add(0, new item("Southwestern Eggrolls", 0, 6, 8.29f, true));
        Test.add(0, new item("Loaded Potato Skins", 7, 0, 7.09f, false));
        Test.add(0, new item("Classic Nachos", 8, 0, 7.69f, true));
        Test.add(0, new item("Triple Dipper", 9, 0, 10.79f, true));
        Test.add(0, new item("Southwestern Eggrolls", 0, 10, 8.29f, true));
        Test.add(0, new item("Loaded Potato Skins", 11, 0, 7.09f, false));
        Test.add(0, new item("Classic Nachos", 12, 0, 7.69f, true));
        Test.add(0, new item("Triple Dipper", 13, 0, 10.79f, true));
        Test.add(0, new item("Southwestern Eggrolls", 14, 0, 8.29f, true));
        Test.add(0, new item("Loaded Potato Skins", 15, 0, 7.09f, false));
        Test.add(0, new item("Classic Nachos", 16, 0, 7.69f, true));
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