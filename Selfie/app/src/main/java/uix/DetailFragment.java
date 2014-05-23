package uix;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

import cse110.selfie.app.UI.R;
import classes.Item;
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

    ItemDataSource itemDataSource;
    WeightController myController;

    Item theItem;
    int itemId = -1;

    public TextView itemName, itemDescription, quantityCounter, thumbsCounter, priceDisplay;
    public ImageButton quantityUp, quantityDown;
    public ImageView thumbsUp, iv1, iv2;;
    public Button addToOrder;
    public LinearLayout recommendedGallery;

    @Override
    //gets and initializes components in fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        itemId = args.getInt(ARG_ITEM_ID);
        View view = inflater.inflate(R.layout.fragment_detail_screen, container, false);

        myController = new WeightController(getActivity());
        itemDataSource = new ItemDataSource(getActivity());
        theItem = itemDataSource.getItem(itemId);
        itemDataSource.close();

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
        quantityUp = (ImageButton) view.findViewById(R.id.itemDetail_quantityUpOne);
        quantityUp.setImageResource(R.drawable.arrow_up);
        quantityUp.setOnClickListener(myButtonListener);
        quantityDown = (ImageButton) view.findViewById(R.id.itemDetail_quantityDownOne);
        quantityDown.setImageResource(R.drawable.arrow_down);
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

    //custom button listener for up, down, and addToOrder
    private class MyButtonListener implements View.OnClickListener {

        int CurrentQuantity;

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //just shows an alert dialog
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
                    break;
                case R.id.itemDetail_quantityUpOne:
                    CurrentQuantity = Integer.parseInt(quantityCounter.getText().toString());
                    quantityCounter.setText(Integer.toString(++CurrentQuantity));
                    priceDisplay.setText(Float.toString(newPrice()));
                    break;
                case R.id.itemDetail_quantityDownOne:
                    CurrentQuantity = Integer.parseInt(quantityCounter.getText().toString());
                    if (CurrentQuantity != 1)
                        quantityCounter.setText(Integer.toString(--CurrentQuantity));
                    priceDisplay.setText(Float.toString(newPrice()));
                    break;
            }
        }

        private float newPrice() {
            return (float) Integer.parseInt(quantityCounter.getText().toString())
                    * theItem.getPrice();
        }
    }

    //will be taken out
    private void updateDetail() {
        //get details from DB and replace

        String[] images = theItem.getImagePath();
        File img1 = new File(images[0]);
        if(img1.exists()) {
            Bitmap bit1 = BitmapFactory.decodeFile(img1.getAbsolutePath());
            iv1.setImageBitmap(bit1);
        }
        else {
            iv1.setImageResource(R.drawable.ic_launcher);
        }
        File img2 = new File(images[1]);
        if(img2.exists()) {
            Bitmap bit2 = BitmapFactory.decodeFile(img1.getAbsolutePath());
            iv2.setImageBitmap(bit2);
        }
        else {
            iv2.setImageResource(R.drawable.ic_launcher);
        }


        itemDescription.setText(theItem.getDescription());
        priceDisplay.setText("$ " + String.format("%.2f", theItem.getPrice()));
        itemName.setText(theItem.getItemName());
        thumbsCounter.setText(Integer.toString(theItem.getLikes()));

        for (int i = 0; i < 8; i++) {
            ImageView rg1 = new ImageView(recommendedGallery.getContext());
            rg1.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            rg1.setImageResource(R.drawable.ic_launcher);
            rg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle argMenu = new Bundle();
                    argMenu.putInt(MenuItemList.ARG_CATEGORY_ID, 1);
                    MenuItemList m = new MenuItemList();
                    m.setArguments(argMenu);

                    Bundle argDetail = new Bundle();
                    argDetail.putInt(DetailFragment.ARG_ITEM_ID, 1);
                    DetailFragment d = new DetailFragment();
                    d.setArguments(argDetail);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager()
                            .beginTransaction();
                    ft.replace(R.id.MSfragment_listContainer, m)
                            .replace(R.id.MSfragment_detailContainer, d)
                            .addToBackStack("Menu 2")
                            .commit();
                }
            });
            recommendedGallery.addView(rg1);
        }
    }
}