package cse110.selfie.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JuanJ on 4/29/2014.
 */
//this is a comment
public class MenuItemList extends ListFragment {

    final static String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";
    Test myTest, myTest1, myTest2, myTest3;
    //public ArrayList<SmallItem> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myListAdapter myAdapter;

        Bundle args = getArguments();
        int myNum = args.getInt(ARG_CATEGORY_ID);

        //list = database.getMenu(Integer.parseInt(ARG_CATEGORY_ID)+1);

        init(); //for my (Juan's) testing only
        if(myNum == 0)
            myAdapter = new myListAdapter(myTest);
        else if(myNum == 1)
            myAdapter = new myListAdapter(myTest1);
        else if(myNum == 2)
            myAdapter = new myListAdapter(myTest2);
        else
            myAdapter = new myListAdapter(myTest3);

        setListAdapter(myAdapter);
    }

    //custom adapter for custom individual item display
    private class myListAdapter extends ArrayAdapter <String> {

        /* INCLUDES DATABASE
        ArrayList<SmallItem> newMenu
          String [] name;

          public String[] getNames(ArrayList<SmallItem> menu) {
            name = new String[](newMenu.size());
            for(int i=0; i<menu.size(); i++) {
                name[i] = menu.get(i).getName();
            }
          }

          public myListAdapter(ArrayList<SmallItem> newMenu) {
            super.(getActivity(), android.R.layout.simple_list_item_1, name);
            this.newMenu = newMenu;
          }
          @Override
          public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.myList_menu_item, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(name[position]);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageView.setImageResource(newMenu.get(position).getThumbnail());

            ImageView imageView1 = (ImageView) convertView.findViewById(R.id.imageView2);
            imageView1.setImageResource(R.drawable.yellow_star);
            if(newMenu.get(position).isSpecial)
                imageView1.setAlpha(1f);
            else
                imageView1.setAlpha(0f);

            return convertView;
          }
        */
        Test menu;
        public myListAdapter(Test menu) {
            super(getActivity(), android.R.layout.simple_list_item_1, menu.getNames());
            this.menu = menu;
        }

        //Gets individual list items
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.mylist_menu_item,null);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(menu.getMenu().get(position).getName());

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.ic_launcher);

            ImageView imageView1 = (ImageView) convertView.findViewById(R.id.imageView2);
            imageView1.setImageResource(R.drawable.yellow_star);

            if(menu.getMenu().get(position).getSpecial())
                imageView1.setAlpha(1f);
            else
                imageView1.setAlpha(0f);

            return convertView;
        }
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

        Bundle arguments = new Bundle();
        arguments.putInt(DetailFragment.ARG_ITEM_ID, position);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);

        //renders details fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.MSfragment_detailContainer, fragment)
                .addToBackStack("Menu " + ARG_CATEGORY_ID)
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

    //for my (Juan's) testing only
    public void init() {
        myTest = new Test();
        myTest.addMenu(new item("Triple Dipper", 10.79f, true ));
        myTest.addMenu(new item("Southwestern Eggrolls", 8.29f, true ));
        myTest.addMenu(new item("Loaded Potato Skins", 7.09f, false));
        myTest.addMenu(new item("Classic Nachos", 7.69f, true));
        myTest.addMenu(new item("Triple Dipper", 10.79f, true ));
        myTest.addMenu(new item("Southwestern Eggrolls", 8.29f, true ));
        myTest.addMenu(new item("Loaded Potato Skins", 7.09f, false));
        myTest.addMenu(new item("Classic Nachos", 7.69f, true));
        myTest.addMenu(new item("Triple Dipper", 10.79f, true ));
        myTest.addMenu(new item("Southwestern Eggrolls", 8.29f, true ));
        myTest.addMenu(new item("Loaded Potato Skins", 7.09f, false));
        myTest.addMenu(new item("Classic Nachos", 7.69f, true));
        myTest.addMenu(new item("Triple Dipper", 10.79f, true ));
        myTest.addMenu(new item("Southwestern Eggrolls", 8.29f, true ));
        myTest.addMenu(new item("Loaded Potato Skins", 7.09f, false));
        myTest.addMenu(new item("Classic Nachos", 7.69f, true));

        myTest1 = new Test();
        myTest1.addMenu(new item("Bacon Burger", 9.59f, false));
        myTest1.addMenu(new item("Cheese Burger", 8.59f, true));
        myTest1.addMenu(new item("Veggie Burger", 7.59f, true));
        myTest1.addMenu(new item("Bacon Cheese Burger", 11.59f, false));

        myTest2 = new Test();
        myTest2.addMenu(new item("Chocolate Cake", 6.29f, false));
        myTest2.addMenu(new item("Chocolate Ice Cream", 6.09f, false));
        myTest2.addMenu(new item("Chocolate Cheesecake", 7.39f, true));

        myTest3 = new Test();
        myTest3.addMenu(new item("Apple Juice", 3.43f, true));
        myTest3.addMenu(new item("Mango Juice", 3.43f, false));
        myTest3.addMenu(new item("Orange Juice", 3.43f, false));
    }
}
