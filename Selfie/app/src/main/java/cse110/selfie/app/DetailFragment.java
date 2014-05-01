package cse110.selfie.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by JuanJ on 4/30/2014.
 */
public class DetailFragment extends Fragment {

    //Top Layout
    public ImageView iv1, iv2, iv3;

    //Middle Layout
    public TextView itemName, itemDescription, allergyInfo, quantityCounter, thumbsCounter;
    public ImageButton quantityUp, quantityDown, addToOrder;
    public ImageView thumbsUp;

    //Bottom Layout
    public LinearLayout recommendedGallery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description_screen, container, false);

        //Top Layout
        iv1 = (ImageView) view.findViewById(R.id.itemDetail_pic1);
        //get and set image from DB
        iv2 = (ImageView) view.findViewById(R.id.itemDetail_pic2);
        //get and set image from DB
        iv3 = (ImageView) view.findViewById(R.id.itemDetail_pic3);
        //get and set image from DB

        //Middle Layout
        itemDescription = (TextView) view.findViewById(R.id.itemDetail_description);
        //get and set name from DB
        itemName = (TextView) view.findViewById(R.id.itemDetail_itemName);
        //get and set name from DB
        allergyInfo = (TextView) view.findViewById(R.id.itemDetail_allergy);
        //get and set name from DB
        quantityCounter = (TextView) view.findViewById(R.id.itemDetail_quantityDisplay);
        //get and set name from DB
        thumbsCounter = (TextView) view.findViewById(R.id.itemDetail_thumbDisplay);
        //get and set name from DB

        MyButtonListener myButtonListener = new MyButtonListener();
        quantityUp = (ImageButton) view.findViewById(R.id.itemDetail_quantityUpOne);
        quantityUp.setOnClickListener(myButtonListener);
        quantityDown = (ImageButton) view.findViewById(R.id.itemDetail_quantityDownOne);
        quantityDown.setOnClickListener(myButtonListener);
        addToOrder = (ImageButton) view.findViewById(R.id.itemDetail_addToOrder);
        addToOrder.setOnClickListener(myButtonListener);

        thumbsUp = (ImageView) view.findViewById(R.id.itemDetail_thumbUp);
        //get and set image from DB

        //Bottom Layout
        recommendedGallery = (LinearLayout) view.findViewById(R.id.itemDetail_recommendedGallery);
        //populate horizontalscrollview

        return view;
    }

    private class MyButtonListener implements View.OnClickListener {

        int CurrentQuantity;
        int CurrentThumb;
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.itemDetail_addToOrder:
                    CurrentQuantity = Integer.parseInt(quantityCounter.getText().toString());

                    break;
                case R.id.itemDetail_quantityUpOne:
                    CurrentQuantity = Integer.parseInt(quantityCounter.getText().toString());
                    quantityCounter.setText(Integer.toString(++CurrentQuantity));
                    break;
                case R.id.itemDetail_quantityDownOne:
                    CurrentQuantity = Integer.parseInt(quantityCounter.getText().toString());
                    quantityCounter.setText(Integer.toString(--CurrentQuantity));
                    break;
                case R.id.itemDetail_thumbUp:
                    CurrentThumb = Integer.parseInt(thumbsCounter.getText().toString());
                    thumbsCounter.setText(Integer.toString(++CurrentThumb));
                    break;
            }
        }

        public MyButtonListener() {
            CurrentQuantity = 0;
            CurrentThumb = 0;
        }
    }
}
