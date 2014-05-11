package cse110.selfie.app;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by JuanJ on 5/1/2014.
 */
public class SpecialTabFragment extends Fragment {

    private LinearLayout specialGallery;
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
                    Bundle argsDetail = new Bundle();
                    argsDetail.putInt(DetailFragment.ARG_ITEM_ID, dailySpecial.get(i).getItemId());
                    MenuItemList menuItemList = new MenuItemList();
                    menuItemList.setArguments(argsMenu);
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

        specialGallery = (LinearLayout) view.findViewById(R.id.imageGallery);
        for(int i=0; i<8; i++) {
            ImageView iv1 = new ImageView(specialGallery.getContext());
            iv1.setImageResource(R.drawable.ic_launcher);
            iv1.setLayoutParams(new ViewGroup.LayoutParams(100, 100));

            specialGallery.addView(iv1);
        }

        return view;
    }
}
