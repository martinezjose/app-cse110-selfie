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

import classes.Category;
import classes.Item;
import classes.SmallItem;
import tests.TestItemDataSource;

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
    public String databasePath;        //storing the path where the database exists for existence
                                        //check

    private CategoryDataSource categorySource;  //TODO: DELETE THIS
    private ImageDataSource imageSource; //DAO to return image path for a specific item
    private RecommendationDataSource recommendationSource;  //DAO to return recommendations for item

    //used to retrieve all columns for basic queries
    private String [] allColumns = {SelfieDatabase.KEY_ITEM_ID,
            SelfieDatabase.KEY_ITEM_NAME,SelfieDatabase.KEY_PRICE, SelfieDatabase.KEY_CATEGORY_ID,
            SelfieDatabase.KEY_LIKES,SelfieDatabase.KEY_ACTIVE,
            SelfieDatabase.KEY_CALORIES,SelfieDatabase.KEY_LAST_UPDATED,
            SelfieDatabase.KEY_DESCRIPTION,SelfieDatabase.KEY_DAILY_SPECIAL,
            SelfieDatabase.KEY_THUMBNAIL};

    //used to retrieve specific columns for getSmallItemFromCategory query
    private String [] smallColumns = {SelfieDatabase.KEY_ITEM_ID,SelfieDatabase.KEY_ITEM_NAME,
            SelfieDatabase.KEY_CATEGORY_ID,SelfieDatabase.KEY_LIKES,
            SelfieDatabase.KEY_DAILY_SPECIAL,SelfieDatabase.KEY_THUMBNAIL};

    //CONSTRUCTOR
    public ItemDataSource(Context context){
        //instantiate a SelfieDatabase from this context
        myDB = new SelfieDatabase(context);

        categorySource = new CategoryDataSource(context); //TODO: DELETE THIS

        imageSource = new ImageDataSource(context);     //instantiate an ImageDataSource
        recommendationSource = new RecommendationDataSource(context,this); //instantiate RecommendationsDataSource

        //save the absolute path for the database
        databasePath = context.getDatabasePath(myDB.DATABASE_NAME).toString();

    }

    //open_read()
    //open the database for reading
    public void open_read() throws SQLiteException{
        db = myDB.getReadableDatabase();
    }

    //open_write()
    //open the database for writing
    public void open_write(){ db = myDB.getWritableDatabase(); }

    //close()
    public void close(){
        db.close();
    }

    /////////////////////////////////////////////////////////////////
    //setUp() TODO: INSERT IMAGE PATHS
    //THIS IS ONLY FOR TESTING PURPOSES!!!!! DELETE THIS METHOD AFTER WE ACTUALLY HAVE A DATABASE...
    public void setUp() throws InsertToDatabaseException{

        int numberOfItems = 100;
        final String [] imagePath = {"/res/image1","/res/thumb1","www.locomoco.com/what.jpg"};
        final long [] recommendations = {3,4,1,6};

        //check whether a database exists already
        File database = new File(databasePath);

        //if the database already exists, do nothing and return.
        if(database.exists() && !database.isDirectory()) {
            Log.d("setUp()","Database exists already!");
            return;
        }

        //
        ArrayList<Item> itemsList = new ArrayList<Item>();

        //loop numberOfItems times.
        for(int i =0; i<numberOfItems; ++i) {
            itemsList.add(TestItemDataSource.startItem());
        }

        int i = 0;
        //handle imagepath and recommendations
        for(Item item:itemsList){
            ++i;    //increment counter
            //add an ImagePath to odd Items
            if(i%2==1){
               imageSource.addImage(item.getItemID(),imagePath);
            }
            //add recommendations to multiples of 3
            if(i%3==0){
                recommendationSource.addRecommendation(item.getItemID(),recommendations);
            }
        }

        Category appetizers = new Category(1,"Appetizers");
        Category entrees = new Category(2,"Entrees");
        Category dessert = new Category(3,"Dessert");
        Category drinks = new Category(4,"Drinks");

        categorySource.addCategory(appetizers);
        categorySource.addCategory(entrees);
        categorySource.addCategory(dessert);
        categorySource.addCategory(drinks);
    }
    //TODO: DELETE THIS
    ////////////////////////////////////////////////////////////////



    /******************************************* CRUD *********************************************/

    /* public void addItem(ArrayList<Item> items) throws InsertToDatabaseException -- Create
     * Parameters: ArrayList<Item> items
     * Description: adds all elements in 'items' to the database
     * PRECONDITION: all elements in 'items' were created legally
     * POSTCONDITION: all elements in 'items' are added to database
     * Returns: none
     * Status: works and tested!
     */
    public void addItem(ArrayList<Item> items) throws InsertToDatabaseException{

        //get writable database
        open_write();

        for(Item element:items) {

            //convert Item object to ContentValues
            ContentValues values = itemToContentValues(element);

            if (db.insert(SelfieDatabase.TABLE_ALL_ITEMS, null, values) == -1) ;
            throw new InsertToDatabaseException("Failed inserting Item <" + element.getItemName() +
                    "> to table " + SelfieDatabase.TABLE_ALL_ITEMS);
        }

        //close database
        close();
    }

    /* public Item getItem(long id) throws RetrieveFromDatabaseException -- Read
     * Parameters: long id
     * Description: reads from the database; returns an Item.
     * PRECONDITION: id for the target item is obtained legally.
     * POSTCONDITION: Item object is returned
     * Returns: found item in the database is returned.
     * Status: works, tested!
     */
    public Item getItem(long id) throws RetrieveFromDatabaseException{

        //get a readable database
        open_read();

        //create a cursor pointing to the item identified by this "id"
        Cursor cursor = db.query(SelfieDatabase.TABLE_ALL_ITEMS,allColumns,SelfieDatabase.KEY_ITEM_ID +
        " = ?",new String[] {String.valueOf(id)},null,null,null);

        if(cursor==null)
            throw new RetrieveFromDatabaseException("Failed retrieving <"+id+"> from table " +
            SelfieDatabase.TABLE_ALL_ITEMS);

        //else, move the cursor to first target
        cursor.moveToFirst();

        //obtain an Item from cursor
        Item returnItem = cursorToItem(cursor);

        //close database
        close();

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
        open_write();

        //update LastUpdated to current time
        item.setLastUpdated(Item.getDateTime());

        //create ContentValues from item
        ContentValues values = itemToContentValues(item);

        //number of rows affected
        int affected = db.update(SelfieDatabase.TABLE_ALL_ITEMS,values,SelfieDatabase.KEY_ITEM_ID + " = ?",
                new String[] {String.valueOf(item.getItemID())});

        //close database
        close();

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
    public void deleteItem(Item item) throws DeleteFromDatabaseException{

        //get writable database
        open_write();

        //delete the record in the database matching item.ID
        int success = db.delete(SelfieDatabase.TABLE_ALL_ITEMS,SelfieDatabase.KEY_ITEM_ID + " =?",
                new String[] {String.valueOf(item.getItemID())});

        if(success == 0)
            throw new DeleteFromDatabaseException();
        //close database
        close();
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
        open_read();

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
        close();

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
        open_read();

        //select all rows in TABLE_ALL_ITEMS
        String countQuery = "SELECT * FROM " + SelfieDatabase.TABLE_ALL_ITEMS;

        //query for countQuery, returns a cursor to query result
        Cursor cursor = db.rawQuery(countQuery,null);

        //get count
        int count = cursor.getCount();

        //close database
        close();

        return count;
    }


    /************************************ Specific Querying ***************************************/

    /* public ArrayList<SmallItem> getSmallItemFromCategory(long categoryID)
     * Parameters: long categoryID
     * Description: returns an ArrayList of SmallItems that match this categoryID
     * PRECONDITION: categoryID is a valid ID
     * POSTCONDITION: an ArrayList of SmallItems is returned
     * Returns: an ArrayList of SmallItems
     * Status: no error-checking
     */
    public ArrayList<SmallItem> getSmallItemFromCategory(long categoryID){

        open_read();

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
        close();

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
        open_read();

        ArrayList<SmallItem> smallItemList = new ArrayList<SmallItem>();

        //query for all specials
        Cursor cursor = db.query(SelfieDatabase.TABLE_ALL_ITEMS,smallColumns,
                SelfieDatabase.KEY_DAILY_SPECIAL + " = 1 ",null,null,null,null,null);

        if(cursor.moveToFirst()){
            do{
               smallItemList.add(cursorToSmallItem(cursor));
            }while(cursor.moveToNext());
        }

        //close database
        close();

        //return populated smallItemList
        return smallItemList;
    }

    /* public SmallItem getSmallItem(long ItemID)
     * Parameters: long ItemID
     * Description: returns a SmallItem matching Item with ItemID
     * PRECONDITION:
     * POSTCONDITION:
     * Returns:
     * Status: untested
     */
    public SmallItem getSmallItem(long ItemID){
        //open database for read
        open_read();

        //query: select item from table_all_items that match KEY_ITEM_ID == ItemID
        Cursor cursor = db.query(SelfieDatabase.TABLE_ALL_ITEMS,smallColumns,SelfieDatabase.KEY_ITEM_ID +
        " = ? ",new String [] {String.valueOf(ItemID)},null,null,null);
        SmallItem smallItem = new SmallItem();

        //close database
        close();
        return smallItem;
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
                cursor.getLong(cursor.getColumnIndex(SelfieDatabase.KEY_ITEM_ID)),
                cursor.getString(cursor.getColumnIndex(SelfieDatabase.KEY_ITEM_NAME)),
                cursor.getFloat((cursor.getColumnIndex(SelfieDatabase.KEY_PRICE))),
                cursor.getInt((cursor.getColumnIndex(SelfieDatabase.KEY_CATEGORY_ID))),
                cursor.getInt((cursor.getColumnIndex(SelfieDatabase.KEY_LIKES))),
                (IsActive==1)?true:false,
                cursor.getInt((cursor.getColumnIndex(SelfieDatabase.KEY_CALORIES))),
                cursor.getString(cursor.getColumnIndex(SelfieDatabase.KEY_LAST_UPDATED)),
                cursor.getString(cursor.getColumnIndex(SelfieDatabase.KEY_DESCRIPTION)),
                (IsSpecial==1)?true:false,
                imageSource.getImage(cursor.getInt(cursor.getColumnIndex(SelfieDatabase.KEY_ITEM_ID))),
                cursor.getString(cursor.getColumnIndex(SelfieDatabase.KEY_THUMBNAIL)),
                recommendationSource.getRecommendations(cursor.getLong(cursor.getColumnIndex(SelfieDatabase.KEY_ITEM_ID)))
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
        values.put(SelfieDatabase.KEY_LAST_UPDATED,item.getLastUpdated());
        values.put(SelfieDatabase.KEY_DESCRIPTION,item.getDescription());
        values.put(SelfieDatabase.KEY_DAILY_SPECIAL,item.isDailySpecial());
        values.put(SelfieDatabase.KEY_THUMBNAIL,item.getThumbnail());
        //ID is automatically incremented (PRIMARY KEY)
        return values;
    }

}