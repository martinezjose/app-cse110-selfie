package cse110.selfie.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by JuanJ on 4/30/2014.
 */

public class CheckoutFragment extends Fragment {

    public String[] name_placeholders = {"One", "Two", "Three"};
    public String[] price_placeholders = {"1", "2", "3"};
    public String[] quantity_placeholders = {"1", "2", "3"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_screen, container, false);

        ListView lv = (ListView)  view.findViewById(R.id.CS_selectedItems);
        checkoutAdapter myAdapter = new checkoutAdapter(name_placeholders);
        lv.setAdapter(myAdapter);

        MyButtonListener myButtonListener = new MyButtonListener();
        Button remove = (Button) view.findViewById(R.id.CS_removeSelected);
        remove.setOnClickListener(myButtonListener);
        Button submit = (Button) view.findViewById(R.id.CS_summitOrder);
        submit.setOnClickListener(myButtonListener);

        return view;
    }

    //custom adapter for custom individual item display
    private class checkoutAdapter extends ArrayAdapter<String> {

        private class ViewHolder {
            TextView quantity, price;
            CheckBox checkBox;
            ImageButton add, sub;
        }

        public checkoutAdapter(String[] dishes) {
            super(getActivity(), android.R.layout.simple_list_item_1, dishes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.mylist_checkout_item, null);
                /*
                holder.quantity = (TextView) convertView.findViewById(R.id.checkout_quantityCounter);
                holder.price = (TextView) convertView.findViewById(R.id.checkout_itemPrice);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkout_checkBox);
                holder.add = (ImageButton) convertView.findViewById(R.id.right);
                holder.sub = (ImageButton) convertView.findViewById(R.id.left);

                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;

                    }
                }); */
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.checkBox.setChecked(false);

            TextView itemName = (TextView) convertView.findViewById(R.id.checkout_itemName);
            //gets from DB
            itemName.setText(name_placeholders[position]);

            TextView itemPrice = (TextView) convertView.findViewById(R.id.checkout_itemPrice);
            //gets from DB
            itemPrice.setText(price_placeholders[position]);

            TextView quantity = (TextView) convertView.findViewById(R.id.checkout_quantityCounter);
            //gets from local DB
            quantity.setText(quantity_placeholders[position]);

            ImageButton left = (ImageButton) convertView.findViewById(R.id.left);
            left.setImageResource(R.drawable.arrow_left);

            ImageButton right = (ImageButton) convertView.findViewById(R.id.right);
            right.setImageResource(R.drawable.arrow_right);

            return convertView;
        }
    }

    private class MyButtonListener implements View.OnClickListener{
        @Override
        //just shows alert dialogs
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.CS_removeSelected:
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Remove Confirmation")
                            .setMessage("Removing the following")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                    break;
                case R.id.CS_summitOrder:
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Submit Order")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                    break;
            }
        }
    }
}
