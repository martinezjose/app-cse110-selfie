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
        ImageView iv1 = new ImageView(specialGallery.getContext());
        iv1.setImageResource(R.drawable.ic_launcher);
        iv1.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        specialGallery.addView(iv1);

        ImageView iv2 = new ImageView(specialGallery.getContext());
        iv2.setImageResource(R.drawable.ic_launcher);
        iv2.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        specialGallery.addView(iv2);

        ImageView iv3 = new ImageView(specialGallery.getContext());
        iv3.setImageResource(R.drawable.ic_launcher);
        iv3.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        specialGallery.addView(iv3);

        ImageView iv4 = new ImageView(specialGallery.getContext());
        iv4.setImageResource(R.drawable.ic_launcher);
        iv4.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        specialGallery.addView(iv4);

        ImageView iv5 = new ImageView(specialGallery.getContext());
        iv5.setImageResource(R.drawable.ic_launcher);
        iv5.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        specialGallery.addView(iv5);

        ImageView iv6 = new ImageView(specialGallery.getContext());
        iv6.setImageResource(R.drawable.ic_launcher);
        iv6.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        specialGallery.addView(iv6);

        ImageView iv7 = new ImageView(specialGallery.getContext());
        iv7.setImageResource(R.drawable.ic_launcher);
        iv7.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        specialGallery.addView(iv7);

        return view;
    }
}
