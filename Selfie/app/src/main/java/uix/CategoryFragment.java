package uix;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cse110.selfie.app.UI.R;
import database.ItemDataSource;


/**
 * Created by JuanJ on 5/1/2014.
 * Controller for the Category screen
 */
public class CategoryFragment extends ListFragment{

    WeightController weightController;
    ItemDataSource itemDataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weightController = new WeightController(getActivity());
        itemDataSource = new ItemDataSource(getActivity());

        String [] category = getResources().getStringArray(R.array.cat_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, category);
        setListAdapter(arrayAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bundle argMenu = new Bundle();
        argMenu.putInt(MenuItemList.ARG_CATEGORY_ID, position);
        MenuItemList menu = new MenuItemList();
        menu.setArguments(argMenu);

        FragmentTransaction fTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        fTransaction.replace(R.id.MSfragment_listContainer, menu)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("Menu " +Integer.toString(position))
                .commit();
        weightController.changeLayoutWeight(1);
    }
}