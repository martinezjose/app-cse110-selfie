package uix;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

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
    final static String ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME";
    final static String ARG_ITEM_ID = "ARG_ITEM_ID";

    private ItemDataSource itemDataSource;
    private ArrayList<SmallItem> list;
    private ArrayList<ViewHolder> views;

        MenuAdapter myAdapter;
    
    private long categoryId = -1, itemId = -1;
    private String categoryName;

    @Override
    //instantiate classes and retrieve the categoryId and itemId to display
    //if itemId equals -1, the first item in that category is displayed
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        categoryId = args.getLong(ARG_CATEGORY_ID);
        categoryName = args.getString(ARG_CATEGORY_NAME);
        itemId = args.getLong(ARG_ITEM_ID);

        TextView t = (TextView) getActivity().findViewById(R.id.MS_caterogory_name);
        t.setText(categoryName);
        t.setVisibility(TextView.VISIBLE);

        itemDataSource = new ItemDataSource(getActivity());
        list = itemDataSource.getSmallItemFromCategory(categoryId);
        views = new ArrayList<ViewHolder>();

        Bundle firstArgs = new Bundle();
        if(itemId == -1) {
            firstArgs.putLong(DetailFragment.ARG_ITEM_ID, list.get(0).getItemID());
            itemId = list.get(0).getItemID();
        }
        else
            firstArgs.putLong(DetailFragment.ARG_ITEM_ID, itemId);

        DetailFragment firstItem = new DetailFragment();
        firstItem.setArguments(firstArgs);
        FragmentTransaction fTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fTransaction.replace(R.id.MSfragment_detailContainer, firstItem)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("Detail " +Long.toString(itemId))
                .commit();

        myAdapter = new MenuAdapter(list);
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

    //get position to highlight
    private int getPosition(long itemId) {
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
        arguments.putLong(DetailFragment.ARG_ITEM_ID, list.get(position).getItemID());
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.MSfragment_detailContainer, fragment)
                .addToBackStack("Detail " +Long.toString(list.get(position).getItemID()))
                .commit();
    }

    //custom adapter: contains 1 TextView(name), 2 ImageViews(special, picture)
    private class MenuAdapter extends ArrayAdapter <SmallItem> {

        public MenuAdapter(ArrayList<SmallItem> newMenu) {
            super(getActivity(), android.R.layout.simple_list_item_1, newMenu);
        }

        @Override
        //instantiates each row in the list
        //checks if the image exists
        //checks if item is special to display the star that designates daily special
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.mylist_menu_item, null);
                holder = new ViewHolder();
                holder.itemName = (TextView) convertView.findViewById(R.id.item_name);
                holder.specialStar = (ImageView) convertView.findViewById(R.id.imageView2);
                holder.itemThumbnail = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            }
            else
                holder = (ViewHolder) convertView.getTag();

            holder.itemName.setText(list.get(position).getItemName());
            holder.itemName.setTypeface(Helper.getFont(getActivity(), 1));

            Helper.getImage(holder.itemThumbnail, list.get(position).getThumbnail());

            holder.specialStar.setImageResource(R.drawable.yellow_star);
            if(list.get(position).isDailySpecial())
                holder.specialStar.setAlpha(1f);
            else
                holder.specialStar.setAlpha(0f);

            return convertView;
        }
    }

    //optimize render
    private class ViewHolder {
        public TextView itemName;
        public ImageView specialStar, itemThumbnail;
    }
}
