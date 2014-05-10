package cse110.selfie.app;

import android.app.ActionBar;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
