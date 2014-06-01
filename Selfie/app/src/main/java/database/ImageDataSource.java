package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by edwinmo on 5/25/14.
 */
public class ImageDataSource {
    private SelfieDatabase myDB;        //instance of the SelfieDatabase class
    private SQLiteDatabase db;          //instance of the SQLiteDatabase class, from which SelfieD-
                                        //atabase extends.

    //used to retrieve all columns for basic queries
    private String [] allColumns = {SelfieDatabase.KEY_IMAGE_ID,SelfieDatabase.KEY_IMAGE_PATH,
            SelfieDatabase.KEY_ITEM_TRACK_ID};

    //CONSTRUCTOR
    public ImageDataSource(Context context){
        myDB = new SelfieDatabase(context);
    }

    //open_read()
    //open the database for reading
    public void open_read() { db = myDB.getReadableDatabase();}

    //open_write()
    //open the database for writing -- foreign key constraints enabled
    public void open_write() {
        db = myDB.getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(true);   //set foreign key constraints
    }

    //close()
    public void close() { db.close(); }

    /******************************************* CRUD *********************************************/

    /* OVERLOADED --- accepts an array of ImagePaths
     * public void addImage(int ItemID, String [] ImagePaths) throws InsertToDatabaseException-- Create
     * Parameters: int ItemID, String [] ImageURLs
     * Description: inserts all ImageURLs linked to ItemID. This is the overloaded method
     *              addImage for an array of ImageURLs.
     * PRECONDITION: ItemID was obtained legally through a legally obtained Item
     * POSTCONDITION: all elements in ImageURLs are inserted to database and linked to item
     * Returns: nothing
     * Status: tested, works
     */
    public void addImage(int ItemID, String [] ImagePaths) throws InsertToDatabaseException{
        //get writable database
        open_write();

        //insert each element in ImageURLs to values and insert to database
        for(String element:ImagePaths) {
            //add item and ImageURLs to ContentValues
            ContentValues values = new ContentValues();
            values.put(SelfieDatabase.KEY_IMAGE_PATH, element);
            values.put(SelfieDatabase.KEY_ITEM_TRACK_ID, ItemID);
            long ReturnValue = db.insert(SelfieDatabase.TABLE_IMAGE_PATHS, null, values);

            if (ReturnValue == -1) {        //throw exception if error adding
                throw new InsertToDatabaseException("Inserting ImagePath: <" + element +
                "> to table " + SelfieDatabase.TABLE_IMAGE_PATHS + " linked to " + ItemID);
            }
        }
    }

    /* public String [] getImage(int ItemID) -- Read
     * Parameters: int ItemID
     * Description: retrieves all Images for an Item object
     * PRECONDITION: ItemID was obtained legally from an Item retrieved with getItem()
     * POSTCONDITION: an array of Strings is returned
     * Returns: an array of Strings containing ImagePaths, the array may be empty
     * Status: tested, works
     */
    public String [] getImage(int ItemID){
        //get readable database
        open_read();

        //create a cursor pointing to the first row identified by this query
        Cursor cursor = db.query(SelfieDatabase.TABLE_IMAGE_PATHS,allColumns,
                SelfieDatabase.KEY_ITEM_TRACK_ID + " = ?",new String[] {String.valueOf(ItemID)},
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
        //close database
        close();

        //return the returnString
        return returnString;
    }


}
