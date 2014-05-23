package uix;

import android.app.Activity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cse110.selfie.app.UI.R;

/**
 * Created by JuanJ on 5/10/2014.
 * This class controls the screens weight in order to display the correct sizes
 */

public class WeightController {

    public FrameLayout list, detail;
    public Activity theActivity;

    public WeightController(Activity newContext) {
        this.theActivity = newContext;
    }

    //sets the sizes of the layouts depending on the screens
    public void changeLayoutWeight (int changeType) {
        float frag1 = 1.0f, frag2 = 4.0f;
        list = (FrameLayout) theActivity.findViewById(R.id.MSfragment_listContainer);
        detail = (FrameLayout) theActivity.findViewById(R.id.MSfragment_detailContainer);

        switch(changeType) {
            case 0:
                frag1 = 1.0f; frag2 = 2.0f;
                break;
            case 1:
                frag1 = 1.0f; frag2 = 1.0f;
                break;
            case 2:
                frag1 = 1.0f; frag2 = 0.0f;
                break;
        }
        list.setLayoutParams(
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, frag1));
        detail.setLayoutParams(
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, frag2));

    }
}
