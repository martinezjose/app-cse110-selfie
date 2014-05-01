package cse110.selfie.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by JuanJ on 4/29/2014.
 */
public class MenuItemList extends ListFragment {

    public String [] menuItems = {"Nachos", "Chicken Wings", "Mozarella Sticks"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myListAdapter myAdapter = new myListAdapter(menuItems);
        setListAdapter(myAdapter);
    }

    //custom adapter for custom individual item display
    private class myListAdapter extends ArrayAdapter <String> {
        public myListAdapter(String [] array) {
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

            return convertView;
        }
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    }
}
