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
import android.widget.AdapterView;
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

    ListView lv;
    OrderAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_screen, container, false);

        lv = (ListView)  view.findViewById(R.id.CS_selectedItems);
        theOrder = Order.getTheOrder();
        listView = new ArrayList<View>();
        names = Order.getNames();

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
                int itemCount = lv.getCount();
                for(int i=0; i<=itemCount; i++) {
                    CheckBox c = (CheckBox) listView.get(i).findViewById(R.id.checkout_checkBox);
                    c.setChecked(cb.isChecked());
                }
            }
        });
        return view;
    }

    private void removeSelected() {
        CheckBox cb;
        int itemCount = lv.getCount();

        for(int i=itemCount; i>0; i--) {
            cb = (CheckBox) listView.get(i).findViewById(R.id.checkout_checkBox);
            if(cb.isChecked()) {
                Log.e("ORDERFRAGMENT", Integer.toString(listView.size()));
                for(int j=0; j<listView.size(); j++) {
                    TextView tx = (TextView) listView.get(j).findViewById(R.id.checkout_itemName);
                    Log.e("ORDERFRAGMENT", tx.getText().toString() +Integer.toString(j));
                }

                Order.remove(i-1);
                listView.remove(i);
            }
        }
        theOrder = Order.getTheOrder();
    }

    private class OrderAdapter extends ArrayAdapter<String> {
        public TextView quantity, itemName, itemPrice;
        public CheckBox checkBox;
        public ImageButton leftButton, rightButton;

        public OrderAdapter(ArrayList<OrderDetail> order) {
            super(getActivity(), android.R.layout.simple_list_item_1, names);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.mylist_checkout_item, null);
            }

            itemName = (TextView) convertView.findViewById(R.id.checkout_itemName);
            itemName.setText(theOrder.get(position).getTheItem().getItemName());

            itemPrice = (TextView) convertView.findViewById(R.id.checkout_itemPrice);
            itemPrice.setText("$ " +Float.toString(theOrder.get(position)
                    .getTheItem().getPrice() * (float)theOrder.get(position).getQuantity()));

            quantity = (TextView) convertView.findViewById(R.id.checkout_quantityCounter);
            quantity.setText(Integer.toString(theOrder.get(position).getQuantity()));

            checkBox = (CheckBox) convertView.findViewById(R.id.checkout_checkBox);

            leftButton = (ImageButton) convertView.findViewById(R.id.left);
            leftButton.setImageResource(R.drawable.arrow_left);
            leftButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                int Q = Integer.parseInt(quantity.getText().toString());
                if(Q != 0) {
                    quantity.setText(--Q);
                }
                }
            });

            rightButton = (ImageButton) convertView.findViewById(R.id.right);
            rightButton.setImageResource(R.drawable.arrow_right);
            rightButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                int Q = Integer.parseInt(quantity.getText().toString());
                quantity.setText(++Q);
                }
            });

            listView.add(convertView);
                return convertView;
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
                                    removeSelected();
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
}
