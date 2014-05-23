package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import classes.Item;
import classes.SmallItem;
import tests.testItemDataSource;

/**
 * Data Access Object (DAO)-- Handles Record insertion and retrieval with Database.
 * Basic functionality such as CRUD and extras are found here.
 * Status: IN PROGRESS
 *
 * Created by edwinmo on 5/7/14.
 */
public class ItemDataSource {

    private SelfieDatabase myDB;        //instance of the SelfieDatabase class
    private SQLiteDatabase db;          //instance of the SQLiteDatabase class, from which SelfieD-
                                        //atabase extends.
    private String databasePath;        //storing the path where the database exists for existence
                                        //check

    //used to retrieve all columns for basic queries
    private String [] allColumns = {SelfieDatabase.KEY_ITEM_ID,
            SelfieDatabase.KEY_ITEM_NAME,SelfieDatabase.KEY_PRICE, SelfieDatabase.KEY_CATEGORY_ID,
            SelfieDatabase.KEY_LIKES,SelfieDatabase.KEY_ACTIVE,
            SelfieDatabase.KEY_CALORIES, SelfieDatabase.KEY_CREATED,SelfieDatabase.KEY_LAST_UPDATED,
            SelfieDatabase.KEY_DESCRIPTION,SelfieDatabase.KEY_DAILY_SPECIAL,SelfieDatabase.KEY_IMAGE_PATH,
            SelfieDatabase.KEY_THUMBNAIL};

    //used to retrieve specific columns for getSmallItemFromCategory query
    private String [] smallColumns = {SelfieDatabase.KEY_ITEM_ID,SelfieDatabase.KEY_ITEM_NAME,
            SelfieDatabase.KEY_CATEGORY_ID,SelfieDatabase.KEY_LIKES,
            SelfieDatabase.KEY_DAILY_SPECIAL,SelfieDatabase.KEY_THUMBNAIL};

    //CONSTRUCTOR
    public ItemDataSource(Context context){

        //instantiate a SelfieDatabase from this context
        myDB = new SelfieDatabase(context);

        //save the absolute path for the database
        databasePath = context.getDatabasePath(myDB.DATABASE_NAME).toString();

    }

    //open()
    public void open() throws SQLiteException{
        db = myDB.getWritableDatabase();
    }

    /////////////////////////////////////////////////////////////////
    //setUp()
    //THIS IS ONLY FOR TESTING PURPOSES!!!!! DELETE THIS METHOD AFTER WE ACTUALLY HAVE A DATABASE...
    public void setUp() throws Exception{

        int numberOfItems = 100;

        //check whether a database exists already
        File database = new File(databasePath);

        //if the database already exists, do nothing and return.
        if(database.exists() && !database.isDirectory()) {
            Log.d("setUp()","Database exists already!");
            return;
        }

        //loop numberOfItems times.
        for(int i =0; i<numberOfItems; ++i) {
            if (addItem(testItemDataSource.startItem()) == -1)
                throw new Exception();  //throw exception if error adding item
        }

    }
    ////////////////////////////////////////////////////////////////

    //close()
    public void close(){
        myDB.close();
    }

    /******************************************* CRUD *********************************************/

    /* public long addItem(Item item) -- Create
     * Parameters: Item item
     * Description: adds an item to the database
     * PRECONDITION: item is created with no ID (through setter constructor)
     * POSTCONDITION: item is added to the database
     * Returns: long ID of the newly inserted Item
     * Status: works, tested but not thoroughly
     */
    public long addItem(Item item){

        //get writable database
        db = myDB.getWritableDatabase();

        //convert Item object to ContentValues
        ContentValues values = itemToContentValues(item);

        long ReturnValue = db.insert(SelfieDatabase.TABLE_ALL_ITEMS,null,values);

        //close database
        db.close();

        return ReturnValue;
    }

    /* public Item getItem(int id) -- Read
     * Parameters: int id
     * Description: reads from the database; returns an Item.
     * PRECONDITION: id for the target item is provided (somehow).
     * POSTCONDITION: Item object is returned
     * Returns: found item in the database is returned.
     * Status: works, kinda tested
     */
    public Item getItem(int id){

        //get a readable database
        db = myDB.getReadableDatabase();

        //create a cursor pointing to the item identified by this "id"
        Cursor cursor = db.query(SelfieDatabase.TABLE_ALL_ITEMS,allColumns,SelfieDatabase.KEY_ITEM_ID +
        " = ?",new String[] {String.valueOf(id)},null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

        //obtain an Item from cursor
        Item returnItem = cursorToItem(cursor);

        //close database
        db.close();

        //return item
        return returnItem;
    }

    /* public int updateItem(Item item) -- Update
     * Description: updates the item in the database. >>> Handles LastUpdated field!!!!!!!!
     * PRECONDITION: item is obtained through getItem() AND the modifications have been done
     *                  prior to calling this method. item has been modified with the updated info.
     * POSTCONDITION: item in database is updated. NOTE: LastUpdated field is updated as well.
     * Returns: number of rows affected
     * Status: not tested. Nothing is updated except for the LastUpdated field. Could implement
     *          more complex updating such as field-by-field updating.
     */
    public int updateItem(Item item){

        //get writable database
        db = myDB.getWritableDatabase();

        //update LastUpdated to current time
        item.setLastUpdated(Item.getDateTime());

        //create ContentValues from item
        ContentValues values = itemToContentValues(item);

        //number of rows affected
        int affected = db.update(SelfieDatabase.TABLE_ALL_ITEMS,values,SelfieDatabase.KEY_ITEM_ID + " = ?",
                new String[] {String.valueOf(item.getItemID())});

        //close database
        db.close();

        //return number of rows affected
        return affected;
    }

    /* public void deleteItem(Item item) -- Delete
     * Parameters: Item item
     * Description: Deletes 'item' from the database.
     * PRECONDITION: item was obtained through getItem() -- guarantees that item has an ItemID
     * POSTCONDITION: item has been deleted from the database.
     * Returns: None
     * STATUS: works, but may change implementation. Dependencies are not handled.
     */
    public void deleteItem(Item item){

        //get writable database
        db = myDB.getWritableDatabase();

        //delete the record in the database matching item.ID
        db.delete(SelfieDatabase.TABLE_ALL_ITEMS,SelfieDatabase.KEY_ITEM_ID + " =?",
                new String[] {String.valueOf(item.getItemID())});

        //close database
        db.close();
    }



    /*********************************** EXTRA FUNCTIONALITY **************************************/


    /* public List<Item> getAllItems()
     * Parameters: none
     * Description: extra functionality. Returns a List of all rows (Items) in the database.
     * PRECONDITION: myDB exists
     * POSTCONDITION: a List of Items is returned
     * Returns: List of Items
     * Status: works, not thoroughly tested. Need to include checks
     */
    public List<Item> getAllItems(){

        //get readable database
        db = myDB.getReadableDatabase();

        //List to store all rows of Items
        List<Item> itemList = new ArrayList<Item>();

        //select all rows
        String selectQuery = "SELECT * FROM " + SelfieDatabase.TABLE_ALL_ITEMS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        //if query was successful
        if(cursor.moveToFirst()){
            do{
                Item item = cursorToItem(cursor);
                itemList.add(item);
            }while(cursor.moveToNext());
        }

        //close database
        db.close();

        //returns the populated itemList
        return itemList;
    }

    /* public int getCount()
     * Parameters: none
     * Description: extra functionality. Returns the number of entries in the database.
     * PRECONDITION: myDB exists
     * POSTCONDITION: the number of entries in the database is returned.
     * Returns: number of entries in the database
     * Status: not tested
     */
    public int getCount(){

        //get readable database
        db = myDB.getReadableDatabase();

        //select all rows in TABLE_ALL_ITEMS
        String countQuery = "SELECT * FROM " + SelfieDatabase.TABLE_ALL_ITEMS;

        //query for countQuery, returns a cursor to query result
        Cursor cursor = db.rawQuery(countQuery,null);

        //get count
        int count = cursor.getCount();

        //close database
        db.close();

        return count;
    }


    /************************************ Specific Querying ***************************************/

    /* public ArrayList<SmallItem> getSmallItemFromCategory(int categoryID)
     * Parameters: int categoryID
     * Description: returns an ArrayList of SmallItems that match this categoryID
     * PRECONDITION: categoryID is a valid ID
     * POSTCONDITION: an ArrayList of SmallItems is returned
     * Returns: an ArrayList of SmallItems
     * Status: no error-checking
     */
    public ArrayList<SmallItem> getSmallItemFromCategory(int categoryID){
        db = myDB.getReadableDatabase();

        ArrayList<SmallItem> smallItemsList = new ArrayList<SmallItem>();

        //select rows in TABLE_ALL_ITEMS where CategoryID == categoryID, ordered by (daily_special)
        //descending and (likes) descending
        Cursor cursor = db.query(SelfieDatabase.TABLE_ALL_ITEMS,allColumns,
                SelfieDatabase.KEY_CATEGORY_ID + " = ? ",new String [] {String.valueOf(categoryID)},
                null,null,SelfieDatabase.KEY_DAILY_SPECIAL + " desc, " + SelfieDatabase.KEY_LIKES + " desc");

        if(cursor.moveToFirst()){
            do{
                smallItemsList.add(cursorToSmallItem(cursor));
            }while(cursor.moveToNext());
        }

        //close database
        db.close();

        //return the populated smallItemsList
        return smallItemsList;
    }

    /* public ArrayList<SmallItem> getSpecialSmallItem()
     * Parameters: none
     * Description: returns all SmallItem objects that are specials (daily_special).
     * PRECONDITION: none
     * POSTCONDITION: a ListArray of SmallItem objects is returned
     * Returns: ListArray of SmallItem
     * Status: untested
     */
    public ArrayList<SmallItem> getSpecialSmallItem(){

        //get a readable database
        db = myDB.getReadableDatabase();

        ArrayList<SmallItem> smallItemList = new ArrayList<SmallItem>();

        //query for all specials
        Cursor cursor = db.query(SelfieDatabase.TABLE_ALL_ITEMS,smallColumns,
                SelfieDatabase.KEY_DAILY_SPECIAL + " = 1 ",null,null,null,null,null);

        if(cursor.moveToFirst()){
            do{
               smallItemList.add(cursorToSmallItem(cursor));
            }while(cursor.moveToNext());
        }

        //return populated smallItemList
        return smallItemList;
    }

    /************************************** HELPER METHODS ****************************************/

    /* private Item cursorToItem(Cursor cursor)
     * Parameters: Cursor cursor
     * Description: returns an Item object pointed at by the cursor in the database.
     * PRECONDITION: cursor points to a valid entry in the database.
     * POSTCONDITION: an Item object is populated and returned
     * Returns: Item
     * Status: works. Not thoroughly tested.
     */
    private Item cursorToItem(Cursor cursor){

        int IsActive = cursor.getInt(cursor.getColumnIndex(SelfieDatabase.KEY_ACTIVE));
        int IsSpecial = cursor.getInt(cursor.getColumnIndex(SelfieDatabase.KEY_DAILY_SPECIAL));

        //getter Constructor
        return new Item(
                cursor.getInt(cursor.getColumnIndex(SelfieDatabase.KEY_ITEM_ID)),
                cursor.getString(cursor.getColumnIndex(SelfieDatabase.KEY_ITEM_NAME)),
                cursor.getFloat((cursor.getColumnIndex(SelfieDatabase.KEY_PRICE))),
                cursor.getInt((cursor.getColumnIndex(SelfieDatabase.KEY_CATEGORY_ID))),
                cursor.getInt((cursor.getColumnIndex(SelfieDatabase.KEY_LIKES))),
                (IsActive==1)?true:false,
                cursor.getInt((cursor.getColumnIndex(SelfieDatabase.KEY_CALORIES))),
                cursor.getString(cursor.getColumnIndex(SelfieDatabase.KEY_CREATED)),
                cursor.getString(cursor.getColumnIndex(SelfieDatabase.KEY_LAST_UPDATED)),
                cursor.getString(cursor.getColumnIndex(SelfieDatabase.KEY_DESCRIPTION)),
                (IsSpecial==1)?true:false,
                stringToArray(cursor.getString(cursor.getColumnIndex(SelfieDatabase.KEY_IMAGE_PATH))),
                cursor.getString(cursor.getColumnIndex(SelfieDatabase.KEY_THUMBNAIL))
        );
    }

    /* private SmallItem cursorToSmallItem(Cursor cursor)
     * Description: returns a SmallItem object pointed at by the cursor in the database.
     * PRECONDITION: cursor points to a valid entry in the database.
     * POSTCONDITION: a SmallItem object is populated and returned
     * Returns: SmallItem
     * Status:
     */
    private SmallItem cursorToSmallItem(Cursor cursor){
        int IsSpecial = cursor.getInt(cursor.getColumnIndex(SelfieDatabase.KEY_DAILY_SPECIAL));
        //getter Constructor
        return new SmallItem(
                cursor.getInt(cursor.getColumnIndex(SelfieDatabase.KEY_ITEM_ID)),
                cursor.getString(cursor.getColumnIndex(SelfieDatabase.KEY_ITEM_NAME)),
                cursor.getInt(cursor.getColumnIndex(SelfieDatabase.KEY_CATEGORY_ID)),
                (IsSpecial==1)?true:false,
                cursor.getString(cursor.getColumnIndex(SelfieDatabase.KEY_THUMBNAIL))
        );
    }

    /* private ContentValues itemToContentValues(Item item)
     * Parameters: Item item;
     * Description: helper function. Returns a ContentValues populated by item's fields.
     * PRECONDITION: item exists.
     * POSTCONDITION: a ContentValues is returned
     * Returns: ContentValues (populated)
     * Status: works, not thoroughly tested.
     */
    private ContentValues itemToContentValues(Item item){
        ContentValues values = new ContentValues();
        values.put(SelfieDatabase.KEY_ITEM_NAME,item.getItemName());
        values.put(SelfieDatabase.KEY_PRICE,item.getPrice());
        values.put(SelfieDatabase.KEY_CATEGORY_ID,item.getCategoryID());
        values.put(SelfieDatabase.KEY_LIKES,item.getLikes());
        values.put(SelfieDatabase.KEY_ACTIVE,item.isActive());
        values.put(SelfieDatabase.KEY_CALORIES,item.getCalories());
        values.put(SelfieDatabase.KEY_CREATED,item.getCreated());
        values.put(SelfieDatabase.KEY_LAST_UPDATED,item.getLastUpdated());
        values.put(SelfieDatabase.KEY_DESCRIPTION,item.getDescription());
        values.put(SelfieDatabase.KEY_DAILY_SPECIAL,item.isDailySpecial());
        values.put(SelfieDatabase.KEY_IMAGE_PATH,arrayToString(item.getImagePath()));
        values.put(SelfieDatabase.KEY_THUMBNAIL,item.getThumbnail());
        //ID is automatically incremented (PRIMARY KEY)
        return values;
    }

    /* String [] stringToArray(String image_paths) -- retrieval
     * Description: converts the image_paths column to an array of Strings, delimited by ;(semicolon)
     * PRECONDITION: image_paths is some string that may or may not contain ";"
     * POSTCONDITION: an array of strings split from ";" is returned
     * Returns: array of strings
     * Status: not thoroughly tested.
     */
    private String [] stringToArray(String image_paths){
        return image_paths.split(";");
    }

    /* String arrayToString(String [] image_paths) -- insertion
     * Description: converts an array of Strings to one String, delimited by ; (semicolon)
     * PRECONDITION: image_paths is an array containing zero or more strings
     * POSTCONDITION: a string containing every element in image_paths is returned, delimited by ;
     * Returns: a string delimited by ;
     * Status: works, untested.
     */
    private String arrayToString(String [] image_paths){
        String returnValue = "";

        //for every string in image_paths
        for(String string:image_paths){
            //append to returnValue the string
            returnValue = returnValue + string;

            //if it's not the last string in image_paths, append ;
            if(string != image_paths[image_paths.length-1])
                returnValue = returnValue+";";
        }

        return returnValue;
    }

}
