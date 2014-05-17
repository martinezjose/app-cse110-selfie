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

import java.util.ArrayList;

import cse110.selfie.app.UI.R;

/**
 * Created by JuanJ on 5/1/2014.
 * Controller for the Specials screen
 */
public class SpecialTabFragment extends Fragment {

    WeightController weightController;
    private LinearLayout specialGallery;
    private ArrayList<item> specials;

    //public ArrayList<SmallItem> dailySpecials;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* INCLUDES DATABASE
        View view = inflater.inflate(R.layout.fragment_special_tab, container, false);
        specialGallery = (LinearLayout) view.findViewById(R.id.imageGallery);
        for(int i=0; i<dailySpecials.size(); i++) {
            ImageView iv = new ImageView(specialGallery.getContext());
            iv1.setImageResource(dailySpecials.get(i).getThumbnail());
            iv1.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle argsMenu = new Bundle();
                    argsMenu.putInt(MenuItemList.ARG_CATEGORY_ID, dailySpecial.get(i).getCategoryId());
                    MenuItemList menuItemList = new MenuItemList();
                    menuItemList.setArguments(argsMenu);

                    Bundle argsDetail = new Bundle();
                    argsDetail.putInt(DetailFragment.ARG_ITEM_ID, dailySpecial.get(i).getItemId());
                    DetailFragment detailFragment = new DetailFragment();
                    detailFragment.setArguments(argsDetail);

                    FragmentTransaction fTransaction = getActivity().getSupportFragmentManager()
                            .beginTransaction();
                    fTransaction.replace(R.id.MSfragment_listContainer, menuItemList)
                            .replace(R.id.MSfragment_detailContainer, detailFragment)
                            .addToBackStack("Menu " +Integer.toString(dailySpecial.get(i).getCategoryId()));
                            .commit();
                }
            });
            specialGallery.addView(iv);
        }

        return view;
        */

        View view = inflater.inflate(R.layout.fragment_special_tab, container, false);

        specials = Test.getSpecials();
        weightController = new WeightController(getActivity());
        specialGallery = (LinearLayout) view.findViewById(R.id.imageGallery);

        for(int i=0; i<specials.size(); i++) {
            ImageView iv1 = new ImageView(specialGallery.getContext());
            iv1.setImageResource(R.drawable.ic_launcher);
            iv1.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            iv1.setId(i);
            Log.e(Integer.toString(iv1.getId()), specials.get(iv1.getId()).getName());
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = view.getId();
                    Log.e("SPECIAL", Integer.toString(pos));
                    Bundle argMenu = new Bundle();
                    argMenu.putInt(MenuItemList.ARG_CATEGORY_ID, specials.get(pos).getiCategory());
                    MenuItemList m = new MenuItemList();
                    m.setArguments(argMenu);

                    Bundle argDetail = new Bundle();
                    argDetail.putInt(DetailFragment.ARG_ITEM_ID, specials.get(pos).getiId());
                    DetailFragment d = new DetailFragment();
                    d.setArguments(argDetail);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.MSfragment_listContainer, m)
                            .replace(R.id.MSfragment_detailContainer, d)
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
