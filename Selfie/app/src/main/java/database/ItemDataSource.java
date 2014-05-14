package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import classes.Item;
import classes.SmallItem;
import tests.testItemDataSource;

/**
 * Data Access Object (DAO)-- Handles Record insertion and retrieval with Database.
 * Basic functionality such as CRUD and extras are found here.
 * Status: NOT FINISHED
 *
 * Created by edwinmo on 5/7/14.
 */
public class ItemDataSource {

    private SelfieDatabase myDB;
    private SQLiteDatabase db;

    private String [] allColumns = {SelfieDatabase.KEY_ITEM_ID,
            SelfieDatabase.KEY_ITEM_NAME,SelfieDatabase.KEY_PRICE, SelfieDatabase.KEY_CATEGORY_ID,
            SelfieDatabase.KEY_LIKES,SelfieDatabase.KEY_ACTIVE,
            SelfieDatabase.KEY_CALORIES, SelfieDatabase.KEY_CREATED,SelfieDatabase.KEY_LAST_UPDATED,
            SelfieDatabase.KEY_DESCRIPTION,SelfieDatabase.KEY_DAILY_SPECIAL,SelfieDatabase.KEY_IMAGE_PATH,
            SelfieDatabase.KEY_THUMBNAIL};

    private String [] smallColumns = {SelfieDatabase.KEY_ITEM_ID,SelfieDatabase.KEY_ITEM_NAME,
            SelfieDatabase.KEY_CATEGORY_ID,SelfieDatabase.KEY_DAILY_SPECIAL,SelfieDatabase.KEY_THUMBNAIL};

    //CONSTRUCTOR
    public ItemDataSource(Context context){
        myDB = new SelfieDatabase(context);
    }

    //open()
    public void open() throws SQLiteException{
        db = myDB.getWritableDatabase();
    }

    /////////////////////////////////////////////////////////////////
    //setUp()
    //THIS IS ONLY FOR TESTING PURPOSES!!!!! DELETE THIS METHOD AFTER WE ACTUALLY HAVE A DATABASE...
    public void setUp() throws Exception{
        for(int i =0; i<100; ++i) {
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

    /* addItem() -- Create
     * Description: adds an item to the database
     * PRECONDITION: item is created with no ID (through setter constructor)
     * POSTCONDITION: item is added to the database
     * Returns: long ID of the newly inserted Item
     * Status: works, tested but not thoroughly
     * Keywords: create, add, new, add item
     */
    public long addItem(Item item){

        //get writable database
        db = myDB.getWritableDatabase();

        ContentValues values = itemToContentValues(item);

        long ReturnValue = db.insert(SelfieDatabase.TABLE_ALL_ITEMS,null,values);
        //close database
        db.close();

        return ReturnValue;
    }

    /* getItem() -- Read
     * Description: reads from the database; returns an Item.
     * PRECONDITION: id for the target item is provided (somehow).
     * POSTCONDITION: Item object is returned
     * Returns: found item in the database is returned.
     * Status: works, kinda tested
     * Keywords: read, getItem, get item, retrieve
     */
    public Item getItem(int id){

        //get a readable database
        db = myDB.getReadableDatabase();

        //create a cursor pointing to the item identified by this "id"
        Cursor cursor = db.query(SelfieDatabase.TABLE_ALL_ITEMS,allColumns,SelfieDatabase.KEY_ITEM_ID +
        " = ?",new String[] {String.valueOf(id)},null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

        //return item
        return cursorToItem(cursor);
    }

    /* updateItem() -- Update
     * Description: updates the item in the database. >>> Handles LastUpdated field!!!!!!!!
     * PRECONDITION: item is obtained through getItem() AND the modifications have been done
     *                  prior to calling this method. item has been modified with the updated info.
     * POSTCONDITION: item in database is updated. NOTE: LastUpdated field is updated as well.
     * Returns: number of rows affected
     * Status: not tested. Nothing is updated except for the LastUpdated field. Could implement
     *          more complex updating such as field-by-field updating.
     * Keywords: update, updating, not done
     */
    public int updateItem(Item item){

        //get writable database
        db = myDB.getWritableDatabase();

        //update LastUpdated to current time
        item.setLastUpdated(Item.getDateTime());

        //create ContentValues from item
        ContentValues values = itemToContentValues(item);

        return db.update(SelfieDatabase.TABLE_ALL_ITEMS,values,SelfieDatabase.KEY_ITEM_ID + " = ?",
                new String[] {String.valueOf(item.getItemID())});
    }

    /* deleteItem() -- Delete
     * Description: Deletes 'item' from the database.
     * PRECONDITION: item was obtained through getItem() -- guarantees that item has an ItemID
     * POSTCONDITION: item has been deleted from the database.
     * Returns: None
     * STATUS: works, but may change implementation.
     * Keywords: delete, delete item, done
     */
    public void deleteItem(Item item){

        //get writable database
        db = myDB.getWritableDatabase();


        db.delete(SelfieDatabase.TABLE_ALL_ITEMS,SelfieDatabase.KEY_ITEM_ID + " =?",
                new String[] {String.valueOf(item.getItemID())});
    }



    /*********************************** EXTRA FUNCTIONALITY **************************************/


    /* getAllItems()
     * Description: extra functionality. Returns a List of all rows (Items) in the database.
     * PRECONDITION: myDB exists
     * POSTCONDITION: a List of Items is returned
     * Returns: List of Items
     * Status: works, not thoroughly tested. Need to include checks
     * Keywords: get all items, retrieve all
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

        return itemList;
    }

    /* getCount()
     * Description: extra functionality. Returns the number of entries in the database.
     * PRECONDITION: myDB exists
     * POSTCONDITION: the number of entries in the database is returned.
     * Returns: number of entries in the database
     * Status: not tested
     * Keywords: count, size, number
     */
    public int getCount(){

        //get readable database
        db = myDB.getReadableDatabase();

        //select all rows in TABLE_ALL_ITEMS
        String countQuery = "SELECT * FROM " + SelfieDatabase.TABLE_ALL_ITEMS;

        //query for countQuery, returns a cursor to query result
        Cursor cursor = db.rawQuery(countQuery,null);
        cursor.close();

        return cursor.getCount();
    }


    /************************************ Specific Querying ***************************************/

    /* getSmallItemFromCategory()
     * Description: returns an ArrayList of SmallItems that match this categoryID
     * PRECONDITION: categoryID is a valid ID
     * POSTCONDITION: an ArrayList of SmallItems is returned
     * Returns: an ArrayList of SmallItems
     * Status: no error-checking
     * Keywords:
     */
    public ArrayList<SmallItem> getSmallItemFromCategory(int categoryID){
        db = myDB.getReadableDatabase();

        ArrayList<SmallItem> smallItemsList = new ArrayList<SmallItem>();

        //select rows in TABLE_ALL_ITEMS that match categoryID
        Cursor cursor = db.query(SelfieDatabase.TABLE_ALL_ITEMS,smallColumns,
                SelfieDatabase.KEY_CATEGORY_ID + " = ? ",new String [] {String.valueOf(categoryID)},
                null,null,null);

        if(cursor.moveToFirst()){
            do{
                smallItemsList.add(cursorToSmallItem(cursor));
            }while(cursor.moveToNext());
        }

        return smallItemsList;
    }

    /************************************** HELPER METHODS ****************************************/

    /* cursorToItem()
     * Description: returns an Item object pointed at by the cursor in the database.
     * PRECONDITION: cursor points to a valid entry in the database.
     * POSTCONDITION: an Item object is populated and returned
     * Returns: Item
     * Status: works. Not thoroughly tested.
     * Keywords: helper, helper function, cursor to Item, cursorToItem
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

    /* cursorToSmallItem()
     * Description: returns a SmallItem object pointed at by the cursor in the database.
     * PRECONDITION: cursor points to a valid entry in the database.
     * POSTCONDITION: a SmallItem object is populated and returned
     * Returns: SmallItem
     * Status:
     * Keywords: helper, helper function, cursor to SmallItem, cursorToSmallItem
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

    /* itemToContentValues()
     * Description: helper function. Returns a ContentValues populated by item's fields.
     * PRECONDITION: item exists.
     * POSTCONDITION: a ContentValues is returned
     * Returns: ContentValues (populated)
     * Status: works, not thoroughly tested.
     * Keywords: content values, contentvalues, itemToContentValues
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

    /* stringToArray() -- retrieval
     * Description:converts the image_paths column to an array of Strings, delimited by ;(semicolon)
     * PRECONDITION:
     * POSTCONDITION:
     * Returns:
     * Status:
     * Keywords:
     */
    private String [] stringToArray(String image_paths){
        return image_paths.split(";");
    }

    /* arrayToString() -- insertion
     * Description: converts an array of Strings to one String, delimited by ; (semicolon)
     * PRECONDITION:
     * POSTCONDITION:
     * Returns:
     * Status:
     * Keywords:
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
