/*
    This is the app launcher point.
 */
package cse110.selfie.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;

/**
 * Created by JuanJ on 4/28/2014.
 */
public class HomeScreenActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        CategoryList cl = new CategoryList();
        ft.add(R.id.MSfragment_listContainer, cl).commit();
    }
}
