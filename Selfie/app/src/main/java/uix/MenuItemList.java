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

    ItemDataSource itemDataSource;

    int categoryId = -1;
    int itemId = -1;
    String [] names = null;
    ArrayList<SmallItem> list;

    @Override
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

        names = getNames(list);

        myListAdapter myAdapter = new myListAdapter(list);
        setListAdapter(myAdapter);
    }

    private class myListAdapter extends ArrayAdapter <String> {

        // INCLUDES DATABASE
        ArrayList<SmallItem> newMenu;

        public myListAdapter(ArrayList<SmallItem> newMenu) {
            super(getActivity(), android.R.layout.simple_list_item_1, names);
            this.newMenu = newMenu;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.mylist_menu_item, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(newMenu.get(position).getItemName());

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            File thumbnail = new File(newMenu.get(position).getThumbnail());
            if(thumbnail.exists()) {
                Bitmap img = BitmapFactory.decodeFile(thumbnail.getAbsolutePath());
                imageView.setImageBitmap(img);
            }
            else {
                imageView.setImageResource(R.drawable.ic_launcher);
            }

            ImageView imageView1 = (ImageView) convertView.findViewById(R.id.imageView2);
            imageView1.setImageResource(R.drawable.yellow_star);
            if(newMenu.get(position).isDailySpecial())
                imageView1.setAlpha(1f);
            else
                imageView1.setAlpha(0f);

            return convertView;
        }
    }

    @Override
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

    @Override
    public void onStart() {
        super.onStart();
        if(getFragmentManager().findFragmentById(R.id.MSfragment_listContainer) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    public String[] getNames(ArrayList<SmallItem> tempMenu) {
        String[] tempNames = new String[tempMenu.size()];
        for(int i=0; i<tempMenu.size(); i++) {
            tempNames[i] = tempMenu.get(i).getItemName();
        }
        return tempNames;
    }
}
