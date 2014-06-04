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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import classes.WebAPI;
import cse110.selfie.app.UI.R;
import database.CategoryDataSource;

public class OrderFragment extends Fragment {

    private ArrayList<OrderDetail> theOrder;
    private ArrayList<myView> myViews;

    private ListView lv;
    private OrderAdapter myAdapter;
    private MyButtonListener myButtonListener;

    private TextView subTotal, tax, total;

    private CategoryDataSource cds;

    @Override
    //instantiate the components
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_screen, container, false);

        TextView t = (TextView) getActivity().findViewById(R.id.MS_caterogory_name);
        t.setVisibility(TextView.INVISIBLE);

        lv = (ListView)  view.findViewById(R.id.CS_selectedItems);
        lv.setDivider(new ColorDrawable(Color.BLACK));
        lv.setDividerHeight(6);

        theOrder = Order.getTheOrder();
        myViews = new ArrayList<myView>();
        fillHolders(theOrder);

        cds = new CategoryDataSource(getActivity());

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
        String m = new String();
        for(int i=0; i<myViews.size(); i++) {
            if(myViews.get(i).check) {
                OrderDetail temp = myViews.get(i).orderDetail;
                m += "\n";
                m += temp.getTheItem().getItemName();
            }
        }
        return m;
    }

    //helper function that remove the rows in the ListView that are checked
    private void removeSelected() {
        int[] pos = new int[myViews.size()];
        for(int i=myViews.size()-1; i>=0; i--) {
            if(myViews.get(i).check) {
                OrderDetail temp = myAdapter.getItem(i);
                pos[i] = i;
                myAdapter.remove(temp);
                myViews.remove(i);
            }
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

    //checks or unchecks all CheckBoxes
    private void checkbox(boolean all) {
        for(int i=0; i<myViews.size(); i++) {
            if(myViews.get(i).check != all) {
                myViews.get(i).check = all;
            }
            myAdapter.notifyDataSetChanged();
        }
    }

    private void fillHolders(ArrayList<OrderDetail> order) {
        for(int i=0; i<order.size(); i++)
            myViews.add(i, new myView(order.get(i), false));
    }

    private boolean isAnySelected() {
        for(int i=0; i<myViews.size(); i++) {
            if(myViews.get(i).check == true)
                return true;
        }
        return false;
    }

    //custom adapter for the ListView
    private class OrderAdapter extends ArrayAdapter<OrderDetail> {
        private ViewHolder holder = null;

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

            OrderDetail od = myViews.get(position).orderDetail;
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster.otf");

            holder.itemId = od.getTheItem().getItemID();

            holder.itemName.setText(od.getTheItem().getItemName());
            holder.itemName.setTypeface(tf);

            float price = od.getTheItem().getPrice() * (float)od.getQuantity();
            holder.itemPrice.setText("$ " +String.format("%.2f", price));
            holder.itemPrice.setTypeface(tf);

            holder.quantity.setText(Integer.toString(theOrder.get(position).getQuantity()));
            holder.quantity.setTypeface(tf);

            holder.category.setText(cds.getCategoryName(od.getTheItem().getCategoryID()));
            holder.category.setTypeface(tf);

            holder.checkBox.setOnClickListener(myButtonListener);
            holder.checkBox.setChecked(myViews.get(position).check);

            holder.leftButton.setImageResource(R.drawable.left_arrow);
            holder.leftButton.setOnClickListener(myButtonListener);

            holder.rightButton.setImageResource(R.drawable.right_arrow);
            holder.rightButton.setOnClickListener(myButtonListener);

            return convertView;
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
                Q = myViews.get(position).orderDetail.getQuantity();
            }

            switch (view.getId()) {
                //shows a dialog confirming the removal of items
                case R.id.CS_removeSelected:
                    if(isAnySelected()) {
                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Remove Confirmation")
                                .setMessage("Removing Items: \n" +displaySelected())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        CheckBox cb = (CheckBox) getActivity().findViewById(R.id.all_checkbox);
                                        cb.setChecked(false);
                                        removeSelected();
                                        setBill();
                                        TextView orderAmountTV = (TextView) getActivity().findViewById(R.id.MS_order_amount);
                                        orderAmountTV.setText("(" +Integer.toString(Order.getSize()) +")");
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
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

                        final Context context = view.getContext();
                        final Button submitButton = (Button) view;


                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Submit Order")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        submitButton.setEnabled(false);
                                        final ProgressDialog dialog;

                                        dialog = new ProgressDialog(context);
                                        dialog.setCancelable(false);
                                        dialog.setMessage("Pinging a waiter, please wait.");
                                        dialog.show();

                                        Thread thread = new Thread() {
                                            public void run() {
                                                try {
                                                    // Get a handler that can be used to post to the main thread
                                                    Handler mainHandler = new Handler(context.getMainLooper());
                                                    long result = WebAPI.postOrders();

                                                    Runnable myRunnable = new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            dialog.hide();
                                                        }
                                                    }; // This is your code
                                                    mainHandler.post(myRunnable);

                                                    if(result == -1)
                                                        throw  new InterruptedException("This is embarrassing, but we couldn't ask" +
                                                                " for a waiter, please try again or ask for assistance.");
                                                    else
                                                    {
                                                        myRunnable = new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                new AlertDialog.Builder(context)
                                                                        .setTitle("Thanks!").setMessage("A server will be with you shortly")
                                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                            }
                                                                        })
                                                                        .show();
                                                                checkbox(true);
                                                                removeSelected();
                                                                setBill();
                                                                TextView orderAmountTV = (TextView) getActivity().findViewById(R.id.MS_order_amount);
                                                                orderAmountTV.setText("(" +Integer.toString(Order.getSize()) +")");

                                                            }
                                                        }; // This is your code
                                                        mainHandler.post(myRunnable);
                                                    }
                                                }
                                                catch (InterruptedException e) {
                                                    final InterruptedException ex = e;
                                                    // Get a handler that can be used to post to the main thread
                                                    Handler mainHandler = new Handler(context.getMainLooper());

                                                    Runnable myRunnable = new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            new AlertDialog.Builder(context)
                                                                    .setTitle("Oops!").setMessage(ex.getMessage())
                                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                        }
                                                                    })
                                                                    .show();
                                                        }
                                                    }; // This is your code
                                                    mainHandler.post(myRunnable);
                                                }
                                                catch (Exception e) {
                                                    Log.e("ITEMDATASOURCE", "SETUP EXCEPTION");
                                                }
                                                finally {
                                                    // Get a handler that can be used to post to the main thread
                                                    Handler mainHandler = new Handler(context.getMainLooper());
                                                    Runnable myRunnable = new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            submitButton.setEnabled(true);
                                                        }
                                                    }; // This is your code
                                                    mainHandler.post(myRunnable);
                                                }
                                            }
                                        };
                                        thread.start();
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
                    CheckBox rcb = (CheckBox) view;
                    myViews.get(position).check = rcb.isChecked();
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
    }

    //pattern to optimize rendering
    static private class ViewHolder {
        public TextView quantity, itemName, itemPrice, category;
        public CheckBox checkBox;
        public ImageView leftButton, rightButton;
        public long itemId;
    }

    private class myView {
        public OrderDetail orderDetail;
        public boolean check;

        public myView (OrderDetail od, boolean c) {
            this.orderDetail = od;
            this.check = c;
        }
    }
}