package uix;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.widget.ImageView;

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
}
