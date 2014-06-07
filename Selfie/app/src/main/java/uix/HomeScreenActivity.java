/*
    This is the app launcher point.
 */
package uix;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import classes.WebAPI;
import cse110.selfie.app.UI.R;
import database.ItemDataSource;

/**
 * Created by JuanJ on 4/28/2014.
 * Main Screen to display every other screen.
 * All other screens are stack on top of this one.
 * Button listeners for home screen, back, waiter, and order.
 */

public class HomeScreenActivity extends FragmentActivity {

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

        //ItemDataSource itemDataSource = new ItemDataSource(this);
        //try {
        //    itemDataSource.setUp();
        //} catch (Exception e) {
        //    Log.e("ITEMDATASOURCE", "SETUP EXCEPTION");z
        //}

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Lobster.otf");
        categoryName = (TextView) findViewById(R.id.MS_caterogory_name);
        categoryName.setTypeface(tf);
        categoryName.setVisibility(TextView.INVISIBLE);
        //there is a comment
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
                Helper.changeWeight(this, 0);
                categoryName.setVisibility(TextView.INVISIBLE);
            }
            else if(mPrevious.getName().startsWith("Menu ")) {
                Helper.changeWeight(this, 1);
            }
            else if(mPrevious.getName() == "Order") {
                Helper.changeWeight(this, 2);
            }
            else if(mPrevious.getName().startsWith("Detail ")) {
                Helper.changeWeight(this, 1);
            }

            //deletes the back history if it gets over 10 (arbitrary number)
            //which makes the back button return the user to the "home screen"
            if (fManager.getBackStackEntryCount() > 10) {
                for(int i=0;i<fManager.getBackStackEntryCount()-2;i++)
                    fManager.popBackStack();
                Helper.changeWeight(this, 0);
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

                final Context context = this;
                final ImageView waiterIV = (ImageView) findViewById(R.id.MS_alert);
                waiterIV.setEnabled(false);
                final ProgressDialog dialog;

                dialog = new ProgressDialog(context);
                dialog.setCancelable(false);
                dialog.setMessage("Pinging a waiter, please wait");
                dialog.show();



                Thread thread = new Thread() {
                    public void run() {
                        try {

                            // Get a handler that can be used to post to the main thread
                            Handler mainHandler = new Handler(context.getMainLooper());


                            long result = WebAPI.pingWaiter();

                            Runnable myRunnable = new Runnable() {
                                @Override
                                public void run() {

                                    dialog.hide();

                                }
                            }; // This is your code
                            mainHandler.post(myRunnable);


                            if (result == -1)
                                throw new InterruptedException("" +
                                        "We couldn't ask for a waiter\n Please try again or ask for assistance"
                                        );
                            else {
                                myRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(context)
                                                .setTitle("Thanks!").setMessage("A server will be with you shortly")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                    }
                                                })
                                                .show();

                                    }
                                }; // This is your code
                                mainHandler.post(myRunnable);


                            }

                        } catch (InterruptedException e) {

                            final InterruptedException ex = e;


                            // Get a handler that can be used to post to the main thread
                            Handler mainHandler = new Handler(context.getMainLooper());

                            Runnable myRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(context)
                                            .setTitle("Oops!").setMessage(ex.getMessage())
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            })
                                            .show();
                                }
                            }; // This is your code
                            mainHandler.post(myRunnable);


                        } catch (Exception e) {
                            Log.e("ITEMDATASOURCE", "SETUP EXCEPTION");
                        } finally {
                            // Get a handler that can be used to post to the main thread
                            Handler mainHandler = new Handler(context.getMainLooper());
                            Runnable myRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    waiterIV.setEnabled(true);

                                }
                            }; // This is your code
                            mainHandler.post(myRunnable);

                        }
                    }
                };
                thread.start();




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
                    Helper.changeWeight(this, 0);
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
                    Helper.changeWeight(this, 2);
                }
                break;
        }
    }
}


/*
    This is the app launcher point.
 */