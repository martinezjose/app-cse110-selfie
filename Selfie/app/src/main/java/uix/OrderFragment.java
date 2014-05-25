package uix;

/**
 * Created by JuanJ on 5/11/2014.
 * Screen displays the current order.
 * List is populated from the Order.class arraylist.
 * Checked checkboxes are removed from the list as well as the order on clicking 'remove items'.
 * 'Submit Order' ignores checkboxes and submit all items displayed, then clear the Order.
 * Quantity can be changed using the left and right buttons; price, subtotal, tax, and total will
 *  change accordingly.
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
import android.util.SparseBooleanArray;

import java.util.ArrayList;

import cse110.selfie.app.UI.R;

public class OrderFragment extends Fragment {

    final static float TAX = 0.0825f;
    ArrayList<OrderDetail> theOrder;
    ArrayList<View> listView;
    String[] names;

    ListView lv;
    OrderAdapter myAdapter;
    MyButtonListener myButtonListener;

    TextView subTotal, tax, total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_screen, container, false);

        lv = (ListView)  view.findViewById(R.id.CS_selectedItems);
        theOrder = Order.getTheOrder();
        listView = new ArrayList<View>();
        names = Order.getNames();

        myAdapter = new OrderAdapter(theOrder);
        lv.setAdapter(myAdapter);

        myButtonListener = new MyButtonListener();
        Button remove = (Button) view.findViewById(R.id.CS_removeSelected);
        remove.setOnClickListener(myButtonListener);
        Button submit = (Button) view.findViewById(R.id.CS_summitOrder);
        submit.setOnClickListener(myButtonListener);
        CheckBox all_chk = (CheckBox) view.findViewById(R.id.all_checkbox);
        all_chk.setOnClickListener(myButtonListener);

        subTotal = (TextView) view.findViewById(R.id.CS_totalBeforeTax);
        tax = (TextView) view.findViewById(R.id.CS_tax);
        total = (TextView) view.findViewById(R.id.CS_total);
        setCheck();
        return view;
    }

    private void removeSelected() {
        SparseBooleanArray checked = myAdapter.getSelected();

        for(int i=checked.size()-1; i>=0; i--) {
            if(checked.valueAt(i)) {
                OrderDetail temp = myAdapter.getItem(checked.keyAt(i));
                myAdapter.remove(temp);
            }
        }
    }

    private void setCheck() {
        float Tax = TAX * Order.getSubtotal();
        float Total = Tax + Order.getSubtotal();
        subTotal.setText(String.format("%.2f", Order.getSubtotal()));
        tax.setText(String.format("%.2f", Tax));
        total.setText(String.format("%.2f", Total));
    }

    private class OrderAdapter extends ArrayAdapter<OrderDetail> {
        public TextView quantity, itemName, itemPrice;
        public CheckBox checkBox;
        public ImageButton leftButton, rightButton;
        SparseBooleanArray mSelectedIds = new SparseBooleanArray();

        public OrderAdapter(ArrayList<OrderDetail> order) {
            super(getActivity(), android.R.layout.simple_list_item_1, order);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.mylist_order_item, null);
            }

            itemName = (TextView) convertView.findViewById(R.id.checkout_itemName);
            itemName.setText(theOrder.get(position).getTheItem().getItemName());

            itemPrice = (TextView) convertView.findViewById(R.id.checkout_itemPrice);
            float price = theOrder.get(position)
                    .getTheItem().getPrice() * (float)theOrder.get(position).getQuantity();
            itemPrice.setText("$ " +String.format("%.2f", price));

            quantity = (TextView) convertView.findViewById(R.id.checkout_quantityCounter);
            quantity.setText(Integer.toString(theOrder.get(position).getQuantity()));

            checkBox = (CheckBox) convertView.findViewById(R.id.checkout_checkBox);
            checkBox.setOnClickListener(myButtonListener);
            leftButton = (ImageButton) convertView.findViewById(R.id.left);
            leftButton.setImageResource(R.drawable.arrow_left);
            leftButton.setOnClickListener(myButtonListener);

            rightButton = (ImageButton) convertView.findViewById(R.id.right);
            rightButton.setImageResource(R.drawable.arrow_right);
            rightButton.setOnClickListener(myButtonListener);

            listView.add(convertView);
            return convertView;
        }

        public void toggleSelection(int position) {
            selectedView(position, !mSelectedIds.get(position));
        }

        public void selectedView(int position, boolean value) {
            if(value)
                mSelectedIds.put(position, value);
            else
                mSelectedIds.delete(position);
            notifyDataSetChanged();
        }

        public SparseBooleanArray getSelected() {
            return mSelectedIds;
        }
    }

    private class MyButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int position = lv.getPositionForView((View) view.getParent());
            TextView tx1 = (TextView)((View)listView.get(position+1)).findViewById(R.id.checkout_quantityCounter);
            int Q = Integer.parseInt(tx1.getText().toString());

            switch (view.getId()) {
                case R.id.CS_removeSelected:
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Remove Confirmation")
                            .setMessage("Removing the following")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    removeSelected();
                                    setCheck();
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
                                    //send order
                                    //clear order
                                    setCheck();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .show();
                    break;
                case R.id.all_checkbox:
                    CheckBox cb = (CheckBox) view;
                    int itemCount = lv.getCount();
                    for(int i=1; i<=itemCount; i++) {
                        CheckBox c = (CheckBox) listView.get(i).findViewById(R.id.checkout_checkBox);
                        c.setChecked(cb.isChecked());
                        myAdapter.toggleSelection(i-1);
                    }
                    break;
                case R.id.checkout_checkBox:
                    myAdapter.toggleSelection(position);
                    break;
                case R.id.left:
                    if(Q != 1) {
                        int newQ = --Q;
                        theOrder.get(position).setQuantity(newQ);
                        setCheck();
                    }
                    break;
                case R.id.right:
                    int newQ = ++Q;
                    theOrder.get(position).setQuantity(newQ);
                    setCheck();
                    break;
            }
            myAdapter.notifyDataSetChanged();
        }
    }
}