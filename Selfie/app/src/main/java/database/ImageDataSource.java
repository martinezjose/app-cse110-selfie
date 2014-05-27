package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import classes.Item;

/**
 * Created by edwinmo on 5/25/14.
 */
public class ImageDataSource {
    private SelfieDatabase myDB;        //instance of the SelfieDatabase class
    private SQLiteDatabase db;          //instance of the SQLiteDatabase class, from which SelfieD-
                                        //atabase extends.
    public String databasePath;        //storing the path where the database exists for existence
                                        //check

    //used to retrieve all columns for basic queries
    private String [] allColumns = {SelfieDatabase.KEY_IMAGE_ID,SelfieDatabase.KEY_IMAGE_PATH,
            SelfieDatabase.KEY_ITEM_TRACK_ID};

    //CONSTRUCTOR
    public ImageDataSource(Context context){
        myDB = new SelfieDatabase(context);

        databasePath = context.getDatabasePath(myDB.DATABASE_NAME).toString();
    }

    //open_read()
    //open the database for reading
    public void open_read() { db = myDB.getReadableDatabase();}

    //open_write()
    //open the database for writing -- foreign key constraints enabled
    public void open_write() {
        db = myDB.getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(true);
    }

    //close()
    public void close() { db.close(); }

    /******************************************* CRUD *********************************************/

    /* OVERLOADED
     * public long addImage(Item item, String ImageURL) -- Create
     * Parameters: Item item, String ImageURL
     * Description: adds an ImagePath to the database and the Item object
     * PRECONDITION: item is obtained legally (through getItem) and ImageURL is a path to Image
     * POSTCONDITION: ImageURL is added to the database linked to item
     * Returns: the image_id or -1 if an error was found
     * Status: tested, works
     */
    public long addImage(Item item, String ImagePath){

        //get writable database
        open_write();

        //add item and ImageURL to ContentValues
        ContentValues values = new ContentValues();
        values.put(SelfieDatabase.KEY_IMAGE_PATH,ImagePath);
        values.put(SelfieDatabase.KEY_ITEM_TRACK_ID,item.getItemID());

        long ReturnValue = db.insert(SelfieDatabase.TABLE_IMAGE_PATHS,null,values);

        //close database
        close();


        return ReturnValue;
    }

    /* OVERLOADED
     * public long addImage(Item item, String [] ImagePaths) -- Create
     * Parameters: Item item, String [] ImageURLs
     * Description: inserts all ImageURLs linked to item. This is the overloaded method
     *              addImage for an array of ImageURLs.
     * PRECONDITION: item was obtained legally through getItem()
     * POSTCONDITION: all elements in ImageURLs are inserted to database and linked to item
     * Returns: the image_id or -1 if an error was found
     * Status: tested, works
     */
    public long addImage(Item item, String [] ImagePaths){
        //get writable database
        open_write();

        long ReturnValue=0;

        //add item and ImageURLs to ContentValues
        ContentValues values = new ContentValues();

        //insert each element in ImageURLs to values and insert to database
        for(String element:ImagePaths){
            values.put(SelfieDatabase.KEY_IMAGE_PATH,element);
            values.put(SelfieDatabase.KEY_ITEM_TRACK_ID,item.getItemID());
            ReturnValue = db.insert(SelfieDatabase.TABLE_IMAGE_PATHS,null,values);
            if(ReturnValue==-1)     //if there is an error inserting, return -1
                return ReturnValue;
        }
        return ReturnValue;
    }

    /* public String [] getImage(Item item) -- Read
     * Parameters: Item item
     * Description: retrieves all Images for an Item object
     * PRECONDITION: item was obtained legally through getItem() or equivalent
     * POSTCONDITION: an array of Strings is returned
     * Returns: an array of Strings containing ImagePaths, the array may be empty
     * Status: tested, works
     */
    public String [] getImage(Item item){
        //get readable database
        db = myDB.getReadableDatabase();

        //create a cursor pointing to the first row identified by this query
        Cursor cursor = db.query(SelfieDatabase.TABLE_IMAGE_PATHS,allColumns,
                SelfieDatabase.KEY_ITEM_TRACK_ID + " = ?",new String[] {String.valueOf(item.getItemID())},
                        null,null,null);

        //create a String of size cursor.getCount()
        String [] returnString = new String[cursor.getCount()];

        //if cursor found match
        if(cursor.moveToFirst()){
            int i = 0;
            do{
                returnString[i] = cursor.getString(cursor.getColumnIndex(SelfieDatabase.KEY_IMAGE_PATH));
                ++i;
            }while(cursor.moveToNext());
        }

        //return the returnString
        return returnString;
    }


}
