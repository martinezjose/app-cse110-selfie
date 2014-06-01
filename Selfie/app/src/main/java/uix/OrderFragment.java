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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CheckBox;
import android.util.SparseBooleanArray;
import android.widget.Toast;

import java.util.ArrayList;

import classes.Category;
import cse110.selfie.app.UI.R;
import database.CategoryDataSource;

public class OrderFragment extends Fragment {

    private ArrayList<OrderDetail> theOrder;
    private ArrayList<ViewHolder> my_holder;
    private ArrayList<Category> cat;

    private ListView lv;
    private OrderAdapter myAdapter;
    private MyButtonListener myButtonListener;

    private TextView subTotal, tax, total;

    @Override
    //instantiate the components
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_screen, container, false);

        lv = (ListView)  view.findViewById(R.id.CS_selectedItems);
        theOrder = Order.getTheOrder();
        my_holder = new ArrayList<ViewHolder>();

        CategoryDataSource cds = new CategoryDataSource(getActivity());
        cat = cds.getAllCategories();

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
        setBill();
        return view;
    }

    //gets the name of the items selected to be removed
    private String displaySelected() {
        SparseBooleanArray checked = myAdapter.getSelected();
        String m = new String();
        for(int i=0; i<checked.size(); i++) {
            if(checked.valueAt(i)) {
                OrderDetail temp = myAdapter.getItem(checked.keyAt(i));
                m += "\n";
                m += temp.getTheItem().getItemName();
            }
        }
        return m;
    }

    //helper function that remove the rows in the ListView that are checked
    private void removeSelected() {
        SparseBooleanArray checked = myAdapter.getSelected();
        int[] pos = new int[checked.size()];
        for(int i=checked.size()-1; i>=0; i--) {
            if(checked.valueAt(i)) {
                OrderDetail temp = myAdapter.getItem(checked.keyAt(i));
                pos[i] = checked.keyAt(i);
                myAdapter.remove(temp);
            }
        }

        for(int j=0; j<pos.length; j++) {
            checked.delete(pos[j]);
        }
    }

    //helper function that updates the subtotal, tax, and total
    private void setBill() {
        float Tax = Order.getTax() * Order.getSubtotal();
        float Total = Tax + Order.getSubtotal();
        subTotal.setText(String.format("%.2f", Order.getSubtotal()));
        tax.setText(String.format("%.2f", Tax));
        total.setText(String.format("%.2f", Total));
    }

    private boolean validateUnique(int itemId) {
        boolean dup = false;
        for(int i=0; i<my_holder.size(); i++) {
            if(itemId == my_holder.get(i).itemId)
                dup = true;
        }
        return dup;
    }

    //custom adapter for the ListView
    private class OrderAdapter extends ArrayAdapter<OrderDetail> {
        private ViewHolder holder = null;
        private SparseBooleanArray mSelectedIds = new SparseBooleanArray();

        public OrderAdapter(ArrayList<OrderDetail> order) {
            super(getActivity(), android.R.layout.simple_list_item_1, order);
        }

        @Override
        //instantiate each row from items in the Order.class
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.mylist_order_item, null);

                holder = new ViewHolder();
                holder.itemName = (TextView) convertView.findViewById(R.id.checkout_itemName);
                holder.itemPrice = (TextView) convertView.findViewById(R.id.checkout_itemPrice);
                holder.quantity = (TextView) convertView.findViewById(R.id.checkout_quantityCounter);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkout_checkBox);
                holder.leftButton = (ImageView) convertView.findViewById(R.id.left);
                holder.rightButton = (ImageView) convertView.findViewById(R.id.right);
                holder.category = (TextView) convertView.findViewById(R.id.category);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            OrderDetail od = theOrder.get(position);
            holder.itemId = od.getTheItem().getItemID();

            holder.itemName.setText(od.getTheItem().getItemName());

            float price = od.getTheItem().getPrice() * (float)od.getQuantity();
            holder.itemPrice.setText("$ " +String.format("%.2f", price));

            holder.quantity.setText(Integer.toString(theOrder.get(position).getQuantity()));

            holder.category.setText(cat.get(od.getTheItem().getCategoryID()-1).getCategoryName());

            holder.checkBox.setOnClickListener(myButtonListener);

            holder.leftButton.setImageResource(R.drawable.arrow_left);
            holder.leftButton.setOnClickListener(myButtonListener);

            holder.rightButton.setImageResource(R.drawable.arrow_right);
            holder.rightButton.setOnClickListener(myButtonListener);

            if(!validateUnique(holder.itemId)) {
                my_holder.add(holder);
            }
            return convertView;
        }

        //determines if the row is selected
        public void selectedView(int position, boolean value) {
            if(value)
                mSelectedIds.put(position, value);
            else
                mSelectedIds.delete(position);
            notifyDataSetChanged();
        }

        //returns the array
        public SparseBooleanArray getSelected() {
            return mSelectedIds;
        }

        //check if any is selected
        public boolean isAnySelected() {
            for(int i=0; i<mSelectedIds.size(); i++) {
                if(mSelectedIds.valueAt(i))
                    return true;
            }
            return false;
        }

    }

    //listener to removeSelected, submitOrder, check all, individual checkboxes, increment and
    //decrement item quantity
    private class MyButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int position = lv.getPositionForView((View) view.getParent());
            int Q = 0;
            if(position != -1) {
                TextView tx1 = my_holder.get(position).quantity;
                Q = Integer.parseInt(tx1.getText().toString());
            }

            switch (view.getId()) {
                //shows a dialog confirming the removal of items
                case R.id.CS_removeSelected:
                    if(myAdapter.isAnySelected()) {
                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Remove Confirmation")
                                .setMessage("Removing Items: \n" +displaySelected())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        CheckBox cb = (CheckBox) getActivity().findViewById(R.id.all_checkbox);
                                        cb.setChecked(false);
                                        removeSelected();
                                        checkbox(false);
                                        setBill();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();
                    }
                    else
                        Toast.makeText(getActivity(), "No Item Selected", Toast.LENGTH_SHORT).show();
                    break;
                //submits the order
                case R.id.CS_summitOrder:
                    if(theOrder.size() != 0) {
                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Submit Order")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //send order
                                        for(int j=my_holder.size()-1; j>=0; j--) {
                                            OrderDetail temp = myAdapter.getItem(j);
                                            myAdapter.remove(temp);
                                        }
                                        setBill();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();
                    }
                    else
                        Toast.makeText(getActivity(), "Order has no items", Toast.LENGTH_SHORT).show();
                    break;
                //check all the checkboxes in the ListView
                case R.id.all_checkbox:
                    CheckBox cb = (CheckBox) view;
                    checkbox(cb.isChecked());
                    break;
                //check the CheckBox in the corresponding row
                case R.id.checkout_checkBox:
                    CheckBox rCB = my_holder.get(position).checkBox;
                    myAdapter.selectedView(position, rCB.isChecked());
                    break;
                //decrease the quantity, no less than 1, and updates the price, subtotal, tax, and
                //total
                case R.id.left:
                    if(Q != 1) {
                        int newQ = --Q;
                        theOrder.get(position).setQuantity(newQ);
                        setBill();
                    }
                    break;
                //increase the quantity, and updates the price, subtotal, tax, and total
                case R.id.right:
                    int newQ = ++Q;
                    theOrder.get(position).setQuantity(newQ);
                    setBill();
                    break;
            }
            //method that updates the ListView
            myAdapter.notifyDataSetChanged();
        }

        //checks or unchecks all CheckBoxes
        private void checkbox(boolean all) {
            for(int i=0; i<my_holder.size(); i++) {
                CheckBox cb = my_holder.get(i).checkBox;
                if(cb.isChecked() != all) {
                    cb.setChecked(all);
                    myAdapter.selectedView(i, all);
                }
            }

        }
    }

    //pattern to optimize rendering
    private class ViewHolder {
        public TextView quantity, itemName, itemPrice, category;
        public CheckBox checkBox;
        public ImageView leftButton, rightButton;
        public int itemId;
    }
}