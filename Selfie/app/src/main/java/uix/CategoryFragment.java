package uix;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import classes.Category;
import cse110.selfie.app.UI.R;
import database.CategoryDataSource;


/**
 * Created by JuanJ on 5/1/2014.
 * Screen displaying the list of categories.
 * The list is dynamically populated from a list given by the database.
 * On row click will open a menu list for the corresponding to the category in that row.
 */
public class CategoryFragment extends ListFragment{

    WeightController weightController;
    CategoryDataSource categoryDataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weightController = new WeightController(getActivity());
        categoryDataSource = new CategoryDataSource(getActivity());

        List<Category> category = categoryDataSource.getAllCategories();
        String[] cat = getNames(category);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, cat);
        setListAdapter(arrayAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bundle argMenu = new Bundle();
        argMenu.putInt(MenuItemList.ARG_CATEGORY_ID, ++position);
        argMenu.putInt(MenuItemList.ARG_ITEM_ID, -1);
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

    private String[] getNames(List<Category> l) {
        String[] n = new String[l.size()];
        for(int i=0; i<l.size(); i++) {
            n[i] = l.get(i).getCategoryName();
        }
        return n;
    }
}