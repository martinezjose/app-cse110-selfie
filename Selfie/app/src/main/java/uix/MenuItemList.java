package uix;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
 * Controller for the Menu ListView
 */

public class MenuItemList extends ListFragment {

    final static String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";

    ItemDataSource itemDataSource;
    WeightController weightController;

    int categoryId = -1;
    String [] names = null;
    ArrayList<SmallItem> list;
    //public ArrayList<SmallItem> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        categoryId = args.getInt(ARG_CATEGORY_ID);
        ItemDataSource itemDataSource = new ItemDataSource(getActivity());
        try {
            itemDataSource.setUp();
        } catch (Exception e) {
            new Exception("SETUP ERROR");
        }

        if(list == null) {
            Log.e("DATABASE", "LIST IS NULL");
            list = itemDataSource.getSmallItemFromCategory(categoryId);
        }
        else {
            Log.e("DATABASE", "LIST IS NOT NULL");
            list.clear();
            list = itemDataSource.getSmallItemFromCategory(categoryId);
        }
        Log.e("DATABASE", Integer.toString(list.size()));
        itemDataSource.close();

        names = getNames(list);

        myListAdapter myAdapter = new myListAdapter(list);
        //list = ItemDataSource.getAllFromCategory(Integer.parseInt(ARG_CATEGORY_ID)+1);
        setListAdapter(myAdapter);
    }

    //custom adapter for custom individual item display
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
        /*
        TextView textView;

        public myListAdapter(String[] menu) {
            super(getActivity(), android.R.layout.simple_list_item_1, menu);
        }

        //Gets individual list items
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.mylist_menu_item, null);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(Test.getMenu(categoryId).get(position).getName());

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.ic_launcher);

            ImageView imageView1 = (ImageView) convertView.findViewById(R.id.imageView2);
            imageView1.setImageResource(R.drawable.yellow_star);

            if(Test.getMenu(categoryId).get(position).getSpecial())
                imageView1.setAlpha(1f);
            else
                imageView1.setAlpha(0f);

            return convertView;
        }
        */
    }

    @Override
    //list's listener
    public void onListItemClick(ListView l, View v, int position, long id) {
        /*INCLUDES DATABASE
        Bundle arguments = new Bundle();
        arguments.putInt(DetailFragment.ARG_ITEM_ID, list.get(i).id);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.MSfragment_detailContainer, fragment)
                .addToBackStack("Menu " +ARG_CATEGORY_ID)
                .commit();
         */

        int theId = Test.getMenu(categoryId).get(position).getiId();
        Bundle arguments = new Bundle();
        arguments.putInt(DetailFragment.ARG_ITEM_ID, theId);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);

        //renders details fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.MSfragment_detailContainer, fragment)
                .addToBackStack("Menu " +Integer.toString(theId))
                .commit();
    }

    @Override
    //highlights the item selected
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
