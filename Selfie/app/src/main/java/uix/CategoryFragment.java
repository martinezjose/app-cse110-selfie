package uix;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

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

    private WeightController weightController;
    private CategoryDataSource categoryDataSource;

    ArrayList<Category> category;

    @Override
    //instantiation of classes
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weightController = new WeightController(getActivity());
        categoryDataSource = new CategoryDataSource(getActivity());

        category = categoryDataSource.getAllCategories();
        CategoryAdapter myAdapter = new CategoryAdapter(category);
        setListAdapter(myAdapter);
    }

    @Override
    //listener for the row selection
    //sends the category position and a default itemId
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bundle argMenu = new Bundle();
        argMenu.putLong(MenuItemList.ARG_CATEGORY_ID, (long) position+1);
        argMenu.putString(MenuItemList.ARG_CATEGORY_NAME, category.get(position).getCategoryName());
        argMenu.putLong(MenuItemList.ARG_ITEM_ID, -1l);
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

    //custom adapter contains 1 textview
    private class CategoryAdapter extends ArrayAdapter<Category> {

        public CategoryAdapter(ArrayList<Category> c) {
            super(getActivity(), android.R.layout.simple_list_item_1, c);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.mylist_category_item, null);

                holder = new ViewHolder();
                holder.t = (TextView) convertView.findViewById(R.id.categoryLabel);

                convertView.setTag(holder);
            }
            else
                holder = (ViewHolder)convertView.getTag();

            holder.t.setText(category.get(position).getCategoryName());
            holder.t.setTypeface(Helper.getFont(getActivity(), 2));

            return convertView;
        }
    }

    private class ViewHolder {
        public TextView t;
    }
}