package uix;

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
import android.widget.CheckBox;
import android.util.Log;

import java.util.ArrayList;

import cse110.selfie.app.UI.R;

public class OrderFragment extends Fragment {

    ArrayList<OrderDetail> theOrder;
    ArrayList<View> listView;
    String[] names;
    boolean[] selected;

    OrderAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_screen, container, false);

        ListView lv = (ListView)  view.findViewById(R.id.CS_selectedItems);
        theOrder = Order.getTheOrder();
        listView = new ArrayList<View>();
        names = Order.getNames();
        selected = new boolean[theOrder.size()];

        myAdapter = new OrderAdapter(theOrder);
        lv.setAdapter(myAdapter);

        MyButtonListener myButtonListener = new MyButtonListener();
        Button remove = (Button) view.findViewById(R.id.CS_removeSelected);
        remove.setOnClickListener(myButtonListener);
        Button submit = (Button) view.findViewById(R.id.CS_summitOrder);
        submit.setOnClickListener(myButtonListener);

        CheckBox all_cb = (CheckBox) view.findViewById(R.id.all_checkbox);
        all_cb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                if(cb.isChecked()) {
                    for(int i=0; i< listView.size(); i++) {
                        CheckBox c = (CheckBox) listView.get(i).findViewById(R.id.checkout_checkBox);
                        c.setChecked(true);
                        selected[i] = true;
                    }
                }
                else {
                    for(int i=0; i< listView.size(); i++) {
                        CheckBox c = (CheckBox) listView.get(i).findViewById(R.id.checkout_checkBox);
                        c.setChecked(false);
                        selected[i] = false;
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    //custom adapter for custom individual item display
    private class OrderAdapter extends ArrayAdapter<String> {

        public OrderAdapter(ArrayList<OrderDetail> order) {
            super(getActivity(), android.R.layout.simple_list_item_1, names);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            final int pos = position;
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.mylist_checkout_item, null);
                holder = new ViewHolder();
                holder.itemName = (TextView) convertView.findViewById(R.id.checkout_itemName);
                holder.itemPrice = (TextView) convertView.findViewById(R.id.checkout_itemPrice);
                holder.quantity = (TextView) convertView.findViewById(R.id.checkout_quantityCounter);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkout_checkBox);
                holder.leftButton = (ImageButton) convertView.findViewById(R.id.left);
                holder.rightButton = (ImageButton) convertView.findViewById(R.id.right);
                convertView.setTag(holder);

                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        CheckBox cb = (CheckBox) view;
                        if(cb.isChecked())
                            selected[pos] = true;
                        else
                            selected[pos] = false;
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.leftButton.setImageResource(R.drawable.arrow_left);
            holder.rightButton.setImageResource(R.drawable.arrow_right);
            holder.itemName.setText(theOrder.get(position).getTheItem().getName());
            int q = theOrder.get(position).getQuantity();
            holder.quantity.setText(Integer.toString(q));
            holder.itemPrice.setText("$ " +Float.toString(theOrder.get(position)
                    .getTheItem().getPrice() * (float)q));

            listView.add(convertView);
            return convertView;
        }
    }

    private void removedSelected() {
        for(int i=0; i<selected.length; i++) {
            if(selected[i]) {
                theOrder.remove(i);
                Log.e("*****ORDER*****", Integer.toString(i));
            }
        }
    }

    private class MyButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.CS_removeSelected:
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Remove Confirmation")
                            .setMessage("Removing the following")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    removedSelected();
                                    myAdapter.notifyDataSetChanged();
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
                                    Order.clear();
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

    private class ViewHolder {
        TextView itemName, itemPrice, quantity;
        ImageButton leftButton, rightButton;
        CheckBox checkBox;
    }
}
