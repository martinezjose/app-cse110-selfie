package cse110.selfie.app;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
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
import android.widget.Toast;

/**
 * Created by JuanJ on 4/30/2014.
 */
public class DetailFragment extends Fragment {

    public WeightController myController;
    final static String ARG_ITEM_ID = "ARG_ITEM_ID";
    //public Item theItem;
    //Top Layout
    public ImageView iv1, iv2;

    //Middle Layout
    public TextView itemName, itemDescription, quantityCounter, thumbsCounter, priceDisplay;
    public ImageButton quantityUp, quantityDown;
    public ImageView thumbsUp;
    public Button addToOrder;

    //Bottom Layout
    public LinearLayout recommendedGallery;

    @Override
    //gets and initializes components in fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        int itemPos = args.getInt(ARG_ITEM_ID);
        View view = inflater.inflate(R.layout.fragment_description_screen, container, false);

        myController = new WeightController(getActivity());
        //theItem = ItemDataSource.getItem(Integer.parseInt(ARG_ITEM_ID));

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

        updateDetail(/*theItem*/);
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
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Confirmation").setMessage("Are You Done?")
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
                    break;
                case R.id.itemDetail_quantityDownOne:
                    CurrentQuantity = Integer.parseInt(quantityCounter.getText().toString());
                    if (CurrentQuantity != 0)
                        quantityCounter.setText(Integer.toString(--CurrentQuantity));
                    break;
            }
        }

    }

    //will be taken out
    public void updateDetail(/*Item theItem*/) {
        //get details from DB and replace

        iv1.setImageResource(R.drawable.ic_launcher);
        iv2.setImageResource(R.drawable.ic_launcher);

        itemDescription.setText("test");
        priceDisplay.setText("$ 10.00");
        itemName.setText("test");
        thumbsCounter.setText("0");

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

        /*
        iv1.setImageResource(theItem.getImage(0));
        iv2.setImageResource(theItem.getImage(1));

        itemDescription.setText(theItem.getDescription());
        priceDisplay.setText("$ " +theItem.getPrice());
        itemName.setText(theItem.getName());
        thumbsCounter.setText(theItem.getThumbs());

        ArrayList<SmallItem> recommendations = theItem.getRecommendations();
        for(int i=0; i<theItem.getRecommendation().size(); i++) {
            ImageView rg1 = new ImageView(recommendedGallery.getContext());
            rg1.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            rg1.setImageResource(recommendations.get(i).getImage(0));
            rg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle argMenu = new Bundle();
                    argMenu.putInt(MenuItemList.ARG_CATEGORY_ID,
                            recommendation.get(i).getCategoryId());
                    MenuItemList m = new MenuItemList();
                    m.setArguments(argMenu);

                    Bundle argDetail = new Bundle();
                    argDetail.putInt(DetailFragment.ARG_ITEM_ID,
                            recommendation.get(i).getItemId());
                    DetailFragment d = new DetailFragment();
                    d.setArguments(argDetail);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager()
                            .beginTransaction();
                    ft.replace(R.id.MSfragment_listContainer, m)
                            .replace(R.id.MSfragment_detailContainer, d)
                            .addToBackStack("Menu " +Integer.toString(theItem.getItemId())
                            .commit();
                }
            recommendedGallery.addView(rg1);
        }
         */
    }
}