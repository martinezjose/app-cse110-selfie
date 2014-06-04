package uix;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import cse110.selfie.app.UI.R;

/**
 * Created by JuanJ on 5/31/2014.
 */
public class Helper {

    public static void getImage(ImageView place, String path) {
        File img1 = new File(path);
        if (img1.exists()) {
            Bitmap pic1 = BitmapFactory.decodeFile(img1.getAbsolutePath());
            place.setImageBitmap(pic1);
        } else
            place.setImageResource(R.drawable.ic_launcher);
    }

    public static void updateOrderQuantity(Activity activity) {
        Activity ac = activity;
        TextView tx = (TextView) ac.findViewById(R.id.MS_order_amount);
        tx.setText("(" +Order.getSize() +")");
    }

    public static void changeWeight(Activity activity, int type) {
        float frag1 = 1.0f, frag2 = 2.0f;
        FrameLayout list = (FrameLayout) activity.findViewById(R.id.MSfragment_listContainer);
        FrameLayout detail = (FrameLayout) activity.findViewById(R.id.MSfragment_detailContainer);

        switch(type) {
            //eg. category-specials
            case 0:
                frag1 = 1.0f; frag2 = 2.0f;
                break;
            //eg. menu-detail
            case 1:
                frag1 = 1.0f; frag2 = 1.0f;
                break;
            //eg. order
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
