package cse110.selfie.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by JuanJ on 4/30/2014.
 */

//not intended to be a listfragment
public class CheckoutFragment extends ListFragment {

    public String[] name_placeholders = {"One", "Two", "Three"};
    public String[] price_placeholders = {"1", "2", "3"};
    public String[] quantity_placeholders = {"1", "2", "3"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_screen, container, false);

        checkoutAdapter myAdapter = new checkoutAdapter(name_placeholders);
        setListAdapter(myAdapter);
        return view;
    }

    //custom adapter for custom individual item display
    private class checkoutAdapter extends ArrayAdapter<String> {
        public checkoutAdapter(String[] dishes) {
            super(getActivity(), android.R.layout.simple_list_item_1, dishes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.mylist_checkout_item, null);
            }

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkout_checkBox);
            checkBox.setChecked(false);

            TextView itemName = (TextView) convertView.findViewById(R.id.checkout_itemName);
            //gets from DB
            itemName.setText(name_placeholders[position]);

            TextView itemPrice = (TextView) convertView.findViewById(R.id.checkout_itemPrice);
            //gets from DB
            itemPrice.setText(price_placeholders[position]);

            TextView quantity = (TextView) convertView.findViewById(R.id.checkout_quantityCounter);
            //gets from local DB
            quantity.setText(quantity_placeholders[position]);

            return convertView;
        }
    }
}
