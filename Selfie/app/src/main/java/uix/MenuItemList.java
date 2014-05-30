package uix;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;
import java.io.File;

import classes.SmallItem;
import cse110.selfie.app.UI.R;
import database.ItemDataSource;

/**
 * Created by JuanJ on 4/29/2014.
 * Screen displaying the list of menu items.
 * The list is dynamically populated with SmallItem from a list given by the database through
 *  the category id.
 * On row click will open a details for the corresponding to the item in that row.
 */

public class MenuItemList extends ListFragment {

    final static String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";
    final static String ARG_ITEM_ID = "ARG_ITEM_ID";

    private ItemDataSource itemDataSource;

    private ArrayList<SmallItem> list;
    
    private int categoryId = -1, itemId = -1;

    @Override
    //instantiate classes and retrieve the categoryId and itemId to display
    //if itemId equals -1, the first item in that category is displayed
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        categoryId = args.getInt(ARG_CATEGORY_ID);
        itemId = args.getInt(ARG_ITEM_ID);
        itemDataSource = new ItemDataSource(getActivity());

        list = itemDataSource.getSmallItemFromCategory(categoryId);

        Bundle firstArgs = new Bundle();
        if(itemId == -1) {
            firstArgs.putInt(DetailFragment.ARG_ITEM_ID, list.get(0).getItemID());
            itemId = list.get(0).getItemID();
        }
        else
            firstArgs.putInt(DetailFragment.ARG_ITEM_ID, itemId);

        DetailFragment firstItem = new DetailFragment();
        firstItem.setArguments(firstArgs);
        FragmentTransaction fTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fTransaction.replace(R.id.MSfragment_detailContainer, firstItem)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("Detail " +Integer.toString(itemId))
                .commit();

        myListAdapter myAdapter = new myListAdapter(list);
        setListAdapter(myAdapter);
    }

    @Override
    //sets single choice mode and highlights the first item
    public void onStart() {
        super.onStart();
        if(getFragmentManager().findFragmentById(R.id.MSfragment_listContainer) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
        getListView().setItemChecked(getPosition(itemId), true);
    }

    private int getPosition(int itemId) {
        int position = 0;
        for(int i=0; i<list.size(); i++) {
            if(list.get(i).getItemID() == itemId)
                position = i;
        }
        return position;
    }

    @Override
    //listener for row clicked
    //sends the corresponding itemId and calls the detail screen
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bundle arguments = new Bundle();
        arguments.putInt(DetailFragment.ARG_ITEM_ID, list.get(position).getItemID());
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.MSfragment_detailContainer, fragment)
                .addToBackStack("Detail " +Integer.toString(list.get(position).getItemID()))
                .commit();
    }

    //custom adapter: contains 1 TextView(name), 2 ImageViews(special, picture)
    private class myListAdapter extends ArrayAdapter <SmallItem> {

        public myListAdapter(ArrayList<SmallItem> newMenu) {
            super(getActivity(), android.R.layout.simple_list_item_1, newMenu);
        }

        @Override
        //instantiates each row in the list
        //checks if the image exists
        //checks if item is special to display the star that designates daily special
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.mylist_menu_item, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(list.get(position).getItemName());

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            File thumbnail = new File(list.get(position).getThumbnail());
            if(thumbnail.exists()) {
                Bitmap img = BitmapFactory.decodeFile(thumbnail.getAbsolutePath());
                imageView.setImageBitmap(img);
            }
            else {
                imageView.setImageResource(R.drawable.ic_launcher);
            }

            ImageView imageView1 = (ImageView) convertView.findViewById(R.id.imageView2);
            imageView1.setImageResource(R.drawable.yellow_star);
            if(list.get(position).isDailySpecial())
                imageView1.setAlpha(1f);
            else
                imageView1.setAlpha(0f);

            return convertView;
        }
    }
}
