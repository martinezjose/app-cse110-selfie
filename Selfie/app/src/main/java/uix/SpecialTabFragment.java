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

import classes.Category;
import classes.SmallItem;
import cse110.selfie.app.UI.R;
import database.CategoryDataSource;
import database.ItemDataSource;

/**
 * Created by JuanJ on 5/1/2014.
 * Screen displaying the list of specials and logo of restaurant.
 * The list is dynamically populated from a list given by the database.
 * The logo is obtain from the database.
 * On clicking special item change the screen to the corresponding details of the item.
 */
public class SpecialTabFragment extends Fragment {

    private ItemDataSource itemDataSource;
    private CategoryDataSource cds;

    private LinearLayout specialGallery;
    private ImageView logo;

    private ArrayList<SmallItem> specials;

    @Override
    //instantiate classes and populates the HorizontalScrollView and logo
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_tab, container, false);

        itemDataSource = new ItemDataSource(getActivity());
        specials = itemDataSource.getSpecialSmallItem();

        cds = new CategoryDataSource(getActivity());

        //there will be a function to retrieve the logo
        logo = (ImageView) view.findViewById(R.id.logo);
        logo.setImageResource(R.drawable.lobster_nachos_logo);

        specialGallery = (LinearLayout) view.findViewById(R.id.imageGallery);

        for(int i=0; i<specials.size(); i++) {
            ImageView iv1 = new ImageView(specialGallery.getContext());
            Helper.getImage(iv1, specials.get(i).getThumbnail());
            iv1.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
            iv1.setPadding(5,0,5,0);
            iv1.setId(i);
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = view.getId();
                    long id = specials.get(pos).getCategoryID();

                    Bundle argMenu = new Bundle();
                    argMenu.putLong(MenuItemList.ARG_CATEGORY_ID, specials.get(pos).getCategoryID());
                    argMenu.putLong(MenuItemList.ARG_ITEM_ID, specials.get(pos).getItemID());
                    argMenu.putString(MenuItemList.ARG_CATEGORY_NAME, cds.getCategoryName(id));
                    MenuItemList m = new MenuItemList();
                    m.setArguments(argMenu);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.MSfragment_listContainer, m)
                            .addToBackStack("Detail " + Long.toString(id))
                            .commit();
                    Helper.changeWeight(getActivity(), 1);
                }
            });
            specialGallery.addView(iv1);
        }

        return view;
    }
}
