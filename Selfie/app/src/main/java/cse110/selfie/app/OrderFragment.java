package cse110.selfie.app;

/**
 * Created by JuanJ on 5/11/2014.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderFragment extends Fragment {

    ArrayList<OrderDetail> theOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_screen, container, false);

        ListView lv = (ListView)  view.findViewById(R.id.CS_selectedItems);
        theOrder = Order.getTheOrder();
        checkoutAdapter myAdapter = new checkoutAdapter(Order.getNames());
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
        TextView itemName, itemPrice, quantity;

        public checkoutAdapter(String[] dishes) {
            super(getActivity(), android.R.layout.simple_list_item_1, dishes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //ViewHolder holder = null;
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.mylist_checkout_item, null);
            }

            itemName = (TextView) convertView.findViewById(R.id.checkout_itemName);
            itemPrice = (TextView) convertView.findViewById(R.id.checkout_itemPrice);
            quantity = (TextView) convertView.findViewById(R.id.checkout_quantityCounter);
            updateList(position);

            ImageButton left = (ImageButton) convertView.findViewById(R.id.left);
            left.setImageResource(R.drawable.arrow_left);

            ImageButton right = (ImageButton) convertView.findViewById(R.id.right);
            right.setImageResource(R.drawable.arrow_right);

            return convertView;
        }

        private void updateList(int position) {
            float newPrice = theOrder.get(position).getTheItem().getPrice()
                * (float)theOrder.get(position).getQuantity();

            itemName.setText(theOrder.get(position).getTheItem().getName());
            itemPrice.setText("$ " +Float.toString(newPrice));
            quantity.setText(Integer.toString(theOrder.get(position).getQuantity()));
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
