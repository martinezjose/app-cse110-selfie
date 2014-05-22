package database;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by edwinmo on 5/18/14.
 */
public class Download {

    //a maximum number of bytes. Pretty reasonable (don't expect to be saving >10mb images!)
    private final static int MAXSIZE = 10000000;

    /* saveImageFromURL()
     * Description: saves an image from a URL to the android device.
     * PRECONDITION: method is called from an activity/context
     * POSTCONDITION: the image pointed at by imageURL is saved in the android device, and this path
     * is returned to the caller.
     * Status: unhandled exceptions (they are only being caught)
     * Returns: a String of the path to the image
     */
    public static String saveImageFromURL(Context context, String imageURL) {
        //get the fileName from the imageURL
        String [] splitURL = imageURL.split("/");
        String imageName = "." + splitURL[splitURL.length-1];
        String storagePath = "";

        //create a URL object from the imageURL
        try {
            URL url = new URL(imageURL);
            try {
                InputStream input = url.openStream();
                try {
                    //get our application's directory for files from context
                    storagePath = context.getFilesDir().toString();

                    OutputStream output = new FileOutputStream(new File(storagePath, imageName));
                    try {
                        byte[] buffer = new byte[MAXSIZE];
                        int bytesRead = 0;
                        while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                            output.write(buffer, 0, bytesRead);
                        }
                    } finally {
                        output.close();
                    }
                } finally {
                    input.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (MalformedURLException e2){
            e2.printStackTrace();
        }
        return storagePath + imageName;
    }
}
