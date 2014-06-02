package tests;

import android.test.AndroidTestCase;

import java.io.File;

import database.Download;

/**
 * Created by edwinmo on 5/31/14.
 */
public class TestDownload extends AndroidTestCase {

    public void testDownload(){
        Download download = new Download();
        String myFileName = "myFile";

        //URL-blob provided by WebApp!
        String path = download.saveImageFromURL(getContext(),"http://lobster-nachos.appspot.com/blobstore/serve/AMIfv97DoTl7zQC8JY4yNeNDpQTkqffQ1sj80uSy-d-oqwKaqdbrcghghMyerLa79JdhQF8QRnkQXkWiJnZD7ZGTA7DrcBBHujSBzAv-E7q45ssfB3bk38V6nfSttuQpOvQAsh3ZgD7wMOL7458Y5Dcn9rPvPk9d22cOKktntxEHqep970GIOIA",
                myFileName);

        File filePath = new File(path);
        if(!filePath.exists())
            fail("Failed downloading image from the internet.");

    }
}
