package cse110.selfie.app;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by JuanJ on 4/30/2014.
 */
public class DetailFragment extends Fragment {

    final static String ARG_ITEM_ID = "ARG_ITEM_ID";
    public String test = "test";
    public String des = "This is a description of the item. It is a test and it should be long enough that it will require two lines to show.\n" + " hopefully it works";
    //Top Layout
    public ImageView iv1, iv2;

    //Middle Layout
    public TextView itemName, itemDescription, quantityCounter, thumbsCounter;
    public ImageButton quantityUp, quantityDown, addToOrder;
    public ImageView thumbsUp;

    //Bottom Layout
    public LinearLayout recommendedGallery;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description_screen, container, false);

        //Top Layout
        iv1 = (ImageView) view.findViewById(R.id.itemDetail_pic1);
        iv1.setImageResource(R.drawable.ic_launcher);
        iv2 = (ImageView) view.findViewById(R.id.itemDetail_pic2);
        iv2.setImageResource(R.drawable.ic_launcher);

        //Middle Layout
        itemDescription = (TextView) view.findViewById(R.id.itemDetail_description);
        itemDescription.setText(des);
        itemName = (TextView) view.findViewById(R.id.itemDetail_itemName);
        itemName.setText(test);
        quantityCounter = (TextView) view.findViewById(R.id.itemDetail_quantityDisplay);
        quantityCounter.setText("0");
        quantityCounter.setGravity(View.TEXT_ALIGNMENT_CENTER);
        thumbsCounter = (TextView) view.findViewById(R.id.itemDetail_thumbDisplay);
        thumbsCounter.setText("0");

        MyButtonListener myButtonListener = new MyButtonListener();
        quantityUp = (ImageButton) view.findViewById(R.id.itemDetail_quantityUpOne);
        quantityUp.setImageResource(R.drawable.arrow_up);
        quantityUp.setOnClickListener(myButtonListener);
        quantityDown = (ImageButton) view.findViewById(R.id.itemDetail_quantityDownOne);
        quantityDown.setImageResource(R.drawable.arrow_down);
        quantityDown.setOnClickListener(myButtonListener);
        addToOrder = (ImageButton) view.findViewById(R.id.itemDetail_addToOrder);
        addToOrder.setImageResource(R.drawable.add_to_order);
        addToOrder.setOnClickListener(myButtonListener);

        thumbsUp = (ImageView) view.findViewById(R.id.itemDetail_thumbup);
        thumbsUp.setImageResource(R.drawable.thumbs_up_pic);

        //Bottom Layout
        recommendedGallery = (LinearLayout) view.findViewById(R.id.itemDetail_recommendedGallery);
        ImageView rg1 = new ImageView(recommendedGallery.getContext());
        rg1.setLayoutParams(new ActionBar.LayoutParams(100, 100));
        rg1.setImageResource(R.drawable.ic_launcher);
        recommendedGallery.addView(rg1);
        ImageView rg2 = new ImageView(recommendedGallery.getContext());
        rg2.setLayoutParams(new ActionBar.LayoutParams(100, 100));
        rg2.setImageResource(R.drawable.ic_launcher);
        recommendedGallery.addView(rg2);
        ImageView rg3 = new ImageView(recommendedGallery.getContext());
        rg3.setLayoutParams(new ActionBar.LayoutParams(100, 100));
        rg3.setImageResource(R.drawable.ic_launcher);
        recommendedGallery.addView(rg3);
        ImageView rg4 = new ImageView(recommendedGallery.getContext());
        rg4.setLayoutParams(new ActionBar.LayoutParams(100, 100));
        rg4.setImageResource(R.drawable.ic_launcher);
        recommendedGallery.addView(rg4);
        ImageView rg5 = new ImageView(recommendedGallery.getContext());
        rg5.setLayoutParams(new ActionBar.LayoutParams(100, 100));
        rg5.setImageResource(R.drawable.ic_launcher);
        recommendedGallery.addView(rg5);
        ImageView rg6 = new ImageView(recommendedGallery.getContext());
        rg6.setLayoutParams(new ActionBar.LayoutParams(100, 100));
        rg6.setImageResource(R.drawable.ic_launcher);
        recommendedGallery.addView(rg6);
        ImageView rg7 = new ImageView(recommendedGallery.getContext());
        rg7.setLayoutParams(new ActionBar.LayoutParams(100, 100));
        rg7.setImageResource(R.drawable.ic_launcher);
        recommendedGallery.addView(rg7);
        ImageView rg8 = new ImageView(recommendedGallery.getContext());
        rg8.setLayoutParams(new ActionBar.LayoutParams(100, 100));
        rg8.setImageResource(R.drawable.ic_launcher);
        recommendedGallery.addView(rg8);


        return view;
    }

    private class MyButtonListener implements View.OnClickListener {

        int CurrentQuantity;

        public MyButtonListener () {
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.itemDetail_addToOrder:
                    CurrentQuantity = Integer.parseInt(quantityCounter.getText().toString());
                    Toast.makeText(view.getContext(),"Item Selected", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.itemDetail_quantityUpOne:
                    CurrentQuantity = Integer.parseInt(quantityCounter.getText().toString());
                    quantityCounter.setText(Integer.toString(++CurrentQuantity));
                    break;
                case R.id.itemDetail_quantityDownOne:
                    CurrentQuantity = Integer.parseInt(quantityCounter.getText().toString());
                    if(CurrentQuantity != 0)
                        quantityCounter.setText(Integer.toString(--CurrentQuantity));
                    break;
            }
        }

    }

    public void updateDetail(int itemId) {
        //get details from DB and replace
    }
}
