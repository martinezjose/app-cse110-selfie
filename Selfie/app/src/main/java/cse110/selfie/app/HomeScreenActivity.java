/*
    This is the app launcher point.
 */
package cse110.selfie.app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by JuanJ on 4/28/2014.
 */
public class HomeScreenActivity extends FragmentActivity implements CategoryFragment.onItemSelectedListener {

    public FragmentManager fm = getSupportFragmentManager();
    public FragmentTransaction ft = fm.beginTransaction();

    public FrameLayout list, detail;

    public CategoryFragment cl;
    public SpecialTabFragment stf;
    @Override
    public void onItemSelected(int itemId) {
        Bundle arguments = new Bundle();
        arguments.putInt(MenuItemList.ARG_ITEM_ID, itemId);

        MenuItemList fragment = new MenuItemList();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MSfragment_listContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        cl = new CategoryFragment();
        stf = new SpecialTabFragment();
        ft.replace(R.id.MSfragment_detailContainer, stf);
        ft.replace(R.id.MSfragment_listContainer, cl).addToBackStack(null).commit();


        final ImageButton backButton = (ImageButton) findViewById(R.id.MS_back_button);
        final Button checkoutButton = (Button) findViewById(R.id.MS_checkout_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment theFragment = fm.findFragmentById(R.id.MSfragment_detailContainer);
                if(fm.getBackStackEntryCount() == 0) {
                    ft.replace(R.id.MSfragment_listContainer, cl).addToBackStack(null);
                }
                else if(fm.getBackStackEntryCount() > 1) {

                    fm.popBackStack();
                    if(theFragment.getClass() == MenuItemList.class) {
                        list = (FrameLayout)findViewById(R.id.MSfragment_listContainer);
                        list.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));

                        detail = (FrameLayout) findViewById(R.id.MSfragment_detailContainer);
                        detail.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 4.0f));

                        fm.popBackStack();
                    }
                }
                if(fm.getBackStackEntryCount() > 10) {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ft.add(R.id.MSfragment_listContainer, cl).addToBackStack(null);
                }

            }
        });


    }
}
