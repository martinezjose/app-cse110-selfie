package uix;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cse110.selfie.app.UI.R;

/**
 * Created by JuanJ on 4/29/2014.
 * Controller for the Menu ListView
 */

public class MenuItemList extends ListFragment {

    WeightController weightController;
    static String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";
    //public ArrayList<SmallItem> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARG_CATEGORY_ID = Integer.toString(getArguments().getInt(ARG_CATEGORY_ID));

        myListAdapter myAdapter = new myListAdapter(Test.getNames(Integer.parseInt(ARG_CATEGORY_ID)));
        //list = ItemDataSource.getAllFromCategory(Integer.parseInt(ARG_CATEGORY_ID)+1);
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
        TextView textView;

        public myListAdapter(String[] menu) {
            super(getActivity(), android.R.layout.simple_list_item_1, menu);
        }

        //Gets individual list items
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.mylist_menu_item,null);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(Test.getMenu(Integer.parseInt(ARG_CATEGORY_ID)).get(position).getName());

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.ic_launcher);

            ImageView imageView1 = (ImageView) convertView.findViewById(R.id.imageView2);
            imageView1.setImageResource(R.drawable.yellow_star);

            if(Test.getMenu(Integer.parseInt(ARG_CATEGORY_ID)).get(position).getSpecial())
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

        int theId = Test.getMenu(Integer.parseInt(ARG_CATEGORY_ID)).get(position).getiId();
        Bundle arguments = new Bundle();
        arguments.putInt(DetailFragment.ARG_ITEM_ID, theId);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);

        //renders details fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.MSfragment_detailContainer, fragment)
                .addToBackStack("Menu " + Test.getMenu(Integer.parseInt(ARG_CATEGORY_ID))
                        .get(position).getiId())
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
}
