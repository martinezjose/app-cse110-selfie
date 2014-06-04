/*
    This is the app launcher point.
 */
package uix;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import cse110.selfie.app.UI.R;
import database.ItemDataSource;

/**
 * Created by JuanJ on 4/28/2014.
 * Main Screen to display every other screen.
 * All other screens are stack on top of this one.
 * Button listeners for home screen, back, waiter, and order.
 */

public class HomeScreenActivity extends FragmentActivity {

    private WeightController weightController;
    private FragmentTransaction fTransaction;

    private CategoryFragment categoryFragment = new CategoryFragment();
    private SpecialTabFragment specialTabFragment = new SpecialTabFragment();
    private OrderFragment orderFragment = new OrderFragment();

    private TextView categoryName;

    @Override
    //instantiation of required classes
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        weightController = new WeightController(this);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Lobster.otf");

        categoryName = (TextView) findViewById(R.id.MS_caterogory_name);
        categoryName.setTypeface(tf);
        categoryName.setVisibility(TextView.INVISIBLE);

        ImageView homeIV = (ImageView) findViewById(R.id.MS_home_button);
        homeIV.setImageResource(R.drawable.home_button);

        ImageView waiterIV = (ImageView) findViewById(R.id.MS_alert);
        waiterIV.setImageResource(R.drawable.waiter_button);

        ImageView orderIV = (ImageView) findViewById(R.id.MS_order_button);
        orderIV.setImageResource(R.drawable.new_order_button);

        TextView orderAmountTV = (TextView) findViewById(R.id.MS_order_amount);
        orderAmountTV.setText("(" + Integer.toString(Order.getSize()) + ")");

        fTransaction = getSupportFragmentManager().beginTransaction();
        fTransaction.replace(R.id.MSfragment_listContainer, categoryFragment)
                .replace(R.id.MSfragment_detailContainer, specialTabFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("Home")
                .commit();
    }

    @Override
    //used when the user move the app to the background to render the screen
    protected void onResume() {
        super.onResume();
        fTransaction = getSupportFragmentManager().beginTransaction();
        fTransaction.replace(R.id.MSfragment_listContainer, categoryFragment)
                .replace(R.id.MSfragment_detailContainer, specialTabFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("Home")
                .commit();
    }

    @Override
    //adds our functionality to android's soft back button
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fManager = getSupportFragmentManager();

        if(fManager.getBackStackEntryCount() > 1) {
            //gets the previous screen to determine the ratio of the screens
            FragmentManager.BackStackEntry mPrevious = fManager.getBackStackEntryAt(
                    fManager.getBackStackEntryCount()-2);

            if(mPrevious.getName() == "Home") {
                fManager.popBackStack();
                weightController.changeLayoutWeight(0);
                categoryName.setVisibility(TextView.INVISIBLE);
            }
            else if(mPrevious.getName().startsWith("Menu ")) {
                weightController.changeLayoutWeight(1);
            }
            else if(mPrevious.getName() == "Order") {
                weightController.changeLayoutWeight(2);
            }
            else if(mPrevious.getName().startsWith("Detail ")) {
                weightController.changeLayoutWeight(1);
            }

            //deletes the back history if it gets over 10 (arbitrary number)
            //which makes the back button return the user to the "home screen"
            if (fManager.getBackStackEntryCount() > 10) {
                for(int i=0;i<fManager.getBackStackEntryCount()-2;i++)
                    fManager.popBackStack();
                weightController.changeLayoutWeight(0);
            }
        }
        else {
            //move the app to the background
            moveTaskToBack(true);
        }
    }

    //listener for back and checkout and waiter ping buttons
    public void myButtonListener(View view) {
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();

        switch (view.getId()) {
            //sends a ping to the POS with the tableId
            case R.id.MS_alert:
                new AlertDialog.Builder(this)
                        .setTitle("Confirmation").setMessage("A Waiter Has Been Notified")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
                break;
            //takes the user to the "home screen" unless it's already there
            case R.id.MS_home_button:
                if(fManager.getBackStackEntryAt(
                        fManager.getBackStackEntryCount()-1).getName() != "Home") {
                    fTransaction.replace(R.id.MSfragment_detailContainer, specialTabFragment)
                            .replace(R.id.MSfragment_listContainer, categoryFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack("Home")
                            .commit();
                    weightController.changeLayoutWeight(0);
                    categoryName.setVisibility(TextView.INVISIBLE);
                }
                break;
            //takes the user to the "order screen" unless it's already there
            case R.id.MS_order_button:
                if(fManager.getBackStackEntryAt(
                    fManager.getBackStackEntryCount()-1).getName() != "Order") {
                    fTransaction.replace(R.id.MSfragment_listContainer, orderFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack("Order")
                            .commit();
                    weightController.changeLayoutWeight(2);
                }
                break;
        }
    }
}