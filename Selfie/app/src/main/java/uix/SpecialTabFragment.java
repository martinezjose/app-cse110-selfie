package uix;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.io.File;

import classes.SmallItem;
import cse110.selfie.app.UI.R;
import database.ItemDataSource;

/**
 * Created by JuanJ on 5/1/2014.
 * Screen displaying the list of specials and logo of restaurant.
 * The list is dynamically populated from a list given by the database.
 * The logo is obtain from the database.
 * On clicking special item change the screen to the corresponding details of the item.
 */
public class SpecialTabFragment extends Fragment {

    WeightController weightController;
    ItemDataSource itemDataSource;

    private LinearLayout specialGallery;
    private ImageView logo;
    private ArrayList<SmallItem> specials;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_tab, container, false);

        itemDataSource = new ItemDataSource(getActivity());
        specials = itemDataSource.getSpecialSmallItem();

        weightController = new WeightController(getActivity());
        logo = (ImageView) view.findViewById(R.id.logo);
        specialGallery = (LinearLayout) view.findViewById(R.id.imageGallery);

        for(int i=0; i<specials.size(); i++) {
            ImageView iv1 = new ImageView(specialGallery.getContext());
            File img1 = new File(specials.get(i).getThumbnail());
            if(img1.exists()) {
                Bitmap pic1 = BitmapFactory.decodeFile(img1.getAbsolutePath());
                iv1.setImageBitmap(pic1);
            }
            else
                iv1.setImageResource(R.drawable.ic_launcher);

            iv1.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            iv1.setId(i);
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = view.getId();
                    Bundle argMenu = new Bundle();
                    argMenu.putInt(MenuItemList.ARG_CATEGORY_ID, specials.get(pos).getCategoryID());
                    argMenu.putInt(MenuItemList.ARG_ITEM_ID, specials.get(pos).getItemID());
                    MenuItemList m = new MenuItemList();
                    m.setArguments(argMenu);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.MSfragment_listContainer, m)
                            .addToBackStack("Menu " + Integer.toString(pos))
                            .commit();
                    weightController.changeLayoutWeight(1);
                }
            });
            specialGallery.addView(iv1);
        }

        return view;
    }
}
