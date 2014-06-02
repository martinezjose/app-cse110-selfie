package uix;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import classes.Category;
import classes.Item;
import cse110.selfie.app.UI.R;
import database.CategoryDataSource;
import database.ItemDataSource;

/**
 * Created by JuanJ on 4/30/2014.
 * Screen displaying the details of a specific item.
 * Details are obtained from a database through the item id.
 * Quantity can be changed with the up and down button; price will change accordingly.
 * 'Add To Order' prompts the user if he/she wants to continue through the menu or go to order
 *  screen.
 * On clicking a recommended item, menu list and detail screen will display the corresponding item
 *  through the item id and category id.
 */
public class DetailFragment extends Fragment {

    final static String ARG_ITEM_ID = "ARG_ITEM_ID";

    private WeightController myController;
    private ItemDataSource itemDataSource;
    private Item theItem, temp;

    private long itemId = -1;

    private TextView itemName, itemDescription, quantityCounter, thumbsCounter, priceDisplay;
    private ImageView thumbsUp, iv1, iv2, quantityUp, quantityDown;
    private Button addToOrder;
    private LinearLayout recommendedGallery;

    @Override
    //gets and initializes components in fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        itemId = args.getLong(ARG_ITEM_ID);
        View view = inflater.inflate(R.layout.fragment_detail_screen, container, false);

        myController = new WeightController(getActivity());
        itemDataSource = new ItemDataSource(getActivity());
        try {
            theItem = itemDataSource.getItem(itemId);
        } catch (Exception e) {
            Log.e("Retrieve Item Exception", "Item #" + Long.toString(itemId));
        }

        //Top Layout
        iv1 = (ImageView) view.findViewById(R.id.itemDetail_pic1);
        iv2 = (ImageView) view.findViewById(R.id.itemDetail_pic2);

        //Middle Layout
        itemDescription = (TextView) view.findViewById(R.id.itemDetail_description);
        priceDisplay = (TextView) view.findViewById(R.id.itemDetail_price);
        itemName = (TextView) view.findViewById(R.id.itemDetail_itemName);
        quantityCounter = (TextView) view.findViewById(R.id.itemDetail_quantityDisplay);
        quantityCounter.setText("1");
        thumbsCounter = (TextView) view.findViewById(R.id.itemDetail_thumbDisplay);

        MyButtonListener myButtonListener = new MyButtonListener();
        quantityUp = (ImageView) view.findViewById(R.id.itemDetail_quantityUpOne);
        quantityUp.setImageResource(R.drawable.arrow_up2);
        quantityUp.setOnClickListener(myButtonListener);
        quantityDown = (ImageView) view.findViewById(R.id.itemDetail_quantityDownOne);
        quantityDown.setImageResource(R.drawable.arrow_down2);
        quantityDown.setOnClickListener(myButtonListener);
        addToOrder = (Button) view.findViewById(R.id.itemDetail_addToOrder);
        addToOrder.setOnClickListener(myButtonListener);

        thumbsUp = (ImageView) view.findViewById(R.id.itemDetail_thumbup);
        thumbsUp.setImageResource(R.drawable.thumbs_up_pic);

        //Bottom Layout
        recommendedGallery = (LinearLayout) view.findViewById(R.id.itemDetail_recommendedGallery);

        updateDetail();
        return view;
    }

    //fills the components with the corresponding information
    //checks if the images exist
    private void updateDetail() {
        String[] images = theItem.getImagePath();
        switch (images.length) {
            case 0:
                Helper.getImage(iv1, "");
                Helper.getImage(iv2, "");
                break;
            case 1:
                Helper.getImage(iv1, images[0]);
                Helper.getImage(iv2, "");
                break;
            case 2:
                Helper.getImage(iv1, images[0]);
                Helper.getImage(iv2, images[1]);
                break;
        }

        itemDescription.setText(theItem.getDescription());
        priceDisplay.setText("$ " + String.format("%.2f", theItem.getPrice()));
        itemName.setText(theItem.getItemName());
        thumbsCounter.setText(Integer.toString(theItem.getLikes()));

        long[] recommendations = theItem.getRecommendations();
        for (int i = 0; i <recommendations.length; i++) {


            ImageView rg1 = new ImageView(recommendedGallery.getContext());
            rg1.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            try {
                temp = itemDataSource.getItem(recommendations[i]);
            } catch (Exception e) {
                Log.e("Item Retrieve Exception", Long.toString(recommendations[i]));
            }
            Helper.getImage(rg1, temp.getThumbnail());

            rg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CategoryDataSource cds = new CategoryDataSource(getActivity());
                    ArrayList<Category> cat = cds.getAllCategories();

                    Bundle argMenu = new Bundle();
                    argMenu.putLong(MenuItemList.ARG_CATEGORY_ID, temp.getCategoryID());
                    argMenu.putString(MenuItemList.ARG_CATEGORY_NAME, cds.getCategoryName(temp.getCategoryID()));
                    argMenu.putLong(MenuItemList.ARG_ITEM_ID, temp.getItemID());
                    MenuItemList m = new MenuItemList();
                    m.setArguments(argMenu);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager()
                            .beginTransaction();
                    ft.replace(R.id.MSfragment_listContainer, m)
                            .addToBackStack("Detail " +Long.toString(temp.getItemID()))
                            .commit();
                }
            });
            recommendedGallery.addView(rg1);
        }
    }

    //custom button listener for up, down, and addToOrder
    private class MyButtonListener implements View.OnClickListener {

        private int CurrentQuantity;

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //displays an alert asking whether the users wants to continue adding items
                //or is done to show the order screen
                case R.id.itemDetail_addToOrder:
                    CurrentQuantity = Integer.parseInt(quantityCounter.getText().toString());
                    Order.add(theItem, CurrentQuantity);
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Confirmation").setMessage("Item: "
                            + itemName.getText().toString() + "\nQuantity: "
                            + Integer.toString(CurrentQuantity) + "\nAre You Done?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FragmentTransaction fTransaction = getActivity()
                                            .getSupportFragmentManager()
                                            .beginTransaction();
                                    OrderFragment checkoutFragment = new OrderFragment();

                                    fTransaction.replace(R.id.MSfragment_listContainer, checkoutFragment)
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                            .addToBackStack("Order")
                                            .commit();
                                    myController.changeLayoutWeight(2);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .show();
                    TextView orderAmountTV = (TextView) getActivity().findViewById(R.id.MS_order_amount);
                    orderAmountTV.setText("(" +Integer.toString(Order.getSize()) +")");
                    break;
                //increments the quantity and updates the price accordingly
                case R.id.itemDetail_quantityUpOne:
                    CurrentQuantity = Integer.parseInt(quantityCounter.getText().toString());
                    quantityCounter.setText(Integer.toString(++CurrentQuantity));
                    priceDisplay.setText("$ " +String.format("%.2f", newPrice()));
                    break;
                //decrements the quantity and updates the price accordingly
                //doesn't allow to go below 1
                case R.id.itemDetail_quantityDownOne:
                    CurrentQuantity = Integer.parseInt(quantityCounter.getText().toString());
                    if (CurrentQuantity != 1)
                        quantityCounter.setText(Integer.toString(--CurrentQuantity));
                    priceDisplay.setText("$ " +String.format("%.2f", newPrice()));
                    break;
            }
        }
        //helper function to calculate the price
        private float newPrice() {
            return (float) Integer.parseInt(quantityCounter.getText().toString())
                    * theItem.getPrice();
        }
    }
}