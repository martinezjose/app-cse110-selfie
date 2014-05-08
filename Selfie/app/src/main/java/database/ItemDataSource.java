package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import classes.Item;

/**
 * Data Access Object (DAO)-- Handles Record insertion and retrieval with Database.
 * Basic functionality such as CRUD and extras are found here.
 * Status: NOT FINISHED
 *
 * Created by edwinmo on 5/7/14.
 */
public class ItemDataSource {

    private ItemDatabase myDB;
    private SQLiteDatabase db;

    private String [] allColumns = {ItemDatabase.KEY_ITEM_ID,
            ItemDatabase.KEY_ITEM_NAME,ItemDatabase.KEY_PRICE, ItemDatabase.KEY_CATEGORY_ID,
            ItemDatabase.KEY_LIKES, ItemDatabase.KEY_SHARES, ItemDatabase.KEY_ACTIVE,
            ItemDatabase.KEY_CALORIES, ItemDatabase.KEY_CREATED,ItemDatabase.KEY_LAST_UPDATED,
            ItemDatabase.KEY_DESCRIPTION,ItemDatabase.KEY_DAILY_SPECIAL,ItemDatabase.KEY_IMAGE_PATH};

    //CONSTRUCTOR
    public ItemDataSource(Context context){
        myDB = new ItemDatabase(context);
    }

    //open()
    public void open() throws SQLiteException{
        db = myDB.getWritableDatabase();
    }

    //close()
    public void close(){
        myDB.close();
    }

    /******************************************* CRUD *********************************************/

    /* addItem() -- Create
     * Description: adds an item to the database
     * PRECONDITION: item is created with no ID (through setter constructor)
     * POSTCONDITION: item is added to the database
     * Returns: None
     * Status: works, tested but not thoroughly
     * Keywords: create, add, new, add item
     */
    public void addItem(Item item){

        //get writable database
        db = myDB.getWritableDatabase();

        ContentValues values = itemToContentValues(item);

        db.insert(ItemDatabase.TABLE_ALL_ITEMS,null,values);
        //close database
        db.close();
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
        Cursor cursor = db.query(ItemDatabase.TABLE_ALL_ITEMS,allColumns,ItemDatabase.KEY_ITEM_ID +
        " = ?",new String[] {String.valueOf(id)},null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

        //return item
        return cursorToItem(cursor);
    }

    /* updateItem() -- Update
     * Description: updates the item in the database.
     * PRECONDITION: item is obtained through getItem() -- guarantees that item has an ItemID
     * POSTCONDITION: item in database is updated. NOTE: LastUpdated field is updated as well.
     * Returns: ItemID
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

        return db.update(ItemDatabase.TABLE_ALL_ITEMS,values,ItemDatabase.KEY_ITEM_ID + " = ?",
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


        db.delete(ItemDatabase.TABLE_ALL_ITEMS,ItemDatabase.KEY_ITEM_ID + " =?",
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
        String selectQuery = "SELECT * FROM " + ItemDatabase.TABLE_ALL_ITEMS;

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
    public int getItemCount(){

        //get readable database
        db = myDB.getReadableDatabase();

        //select all rows in TABLE_ALL_ITEMS
        String countQuery = "SELECT * FROM " + ItemDatabase.TABLE_ALL_ITEMS;

        //query for countQuery, returs a cursor to query result
        Cursor cursor = db.rawQuery(countQuery,null);
        cursor.close();

        return cursor.getCount();
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

        //getter Constructor
        return new Item(
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(ItemDatabase.KEY_ITEM_ID))),
                cursor.getString(cursor.getColumnIndex(ItemDatabase.KEY_ITEM_NAME)),
                Float.parseFloat(cursor.getString(cursor.getColumnIndex(ItemDatabase.KEY_PRICE))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(ItemDatabase.KEY_CATEGORY_ID))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(ItemDatabase.KEY_LIKES))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(ItemDatabase.KEY_SHARES))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ItemDatabase.KEY_ACTIVE))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(ItemDatabase.KEY_CALORIES))),
                cursor.getString(cursor.getColumnIndex(ItemDatabase.KEY_CREATED)),
                cursor.getString(cursor.getColumnIndex(ItemDatabase.KEY_LAST_UPDATED)),
                cursor.getString(cursor.getColumnIndex(ItemDatabase.KEY_DESCRIPTION)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ItemDatabase.KEY_DAILY_SPECIAL))),
                cursor.getString(cursor.getColumnIndex(ItemDatabase.KEY_IMAGE_PATH))
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
        values.put(ItemDatabase.KEY_ITEM_NAME,item.getItemName());
        values.put(ItemDatabase.KEY_PRICE,item.getPrice());
        values.put(ItemDatabase.KEY_CATEGORY_ID,item.getCategoryID());
        values.put(ItemDatabase.KEY_LIKES,item.getLikes());
        values.put(ItemDatabase.KEY_SHARES,item.getShares());
        values.put(ItemDatabase.KEY_ACTIVE,item.isActive());
        values.put(ItemDatabase.KEY_CALORIES,item.getCalories());
        values.put(ItemDatabase.KEY_CREATED,item.getCreated());
        values.put(ItemDatabase.KEY_LAST_UPDATED,item.getLastUpdated());
        values.put(ItemDatabase.KEY_DESCRIPTION,item.getDescription());
        values.put(ItemDatabase.KEY_DAILY_SPECIAL,item.isDailySpecial());
        values.put(ItemDatabase.KEY_IMAGE_PATH,item.getImagePath());
        //ID is automatically incremented (PRIMARY KEY)
        return values;
    }
}
