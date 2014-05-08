package cse110.selfie.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JuanJ on 4/29/2014.
 */
public class MenuItemList extends ListFragment {

    final static String ARG_ITEM_ID = "ARG_ITEM_ID";
    public String [] menuItems = {"Nachos", "Chicken Wings", "Mozarella Sticks"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout list = (FrameLayout) getActivity().findViewById(R.id.MSfragment_listContainer);
        list.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));

        FrameLayout detail = (FrameLayout) getActivity().findViewById(R.id.MSfragment_detailContainer);
        detail.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));

        myListAdapter myAdapter = new myListAdapter(menuItems);
        setListAdapter(myAdapter);

    }

    //custom adapter for custom individual item display
    private class myListAdapter extends ArrayAdapter <String> {
        public myListAdapter(String[] array) {
            super(getActivity(), android.R.layout.simple_list_item_1, array);
        }

        //Gets individual list items
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.mylist_menu_item,null);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(menuItems[position]);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.ic_launcher);

            ImageView imageView1 = (ImageView) convertView.findViewById(R.id.imageView2);
            imageView1.setImageResource(R.drawable.yellow_star);

            return convertView;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bundle arguments = new Bundle();
        arguments.putInt(DetailFragment.ARG_ITEM_ID, position);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.MSfragment_detailContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getFragmentManager().findFragmentById(R.id.MSfragment_listContainer) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }
}
