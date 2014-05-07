package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import classes.Item;

/**
 * DatabaseHandler handles Database interaction. Basic CRUD and others.
 * Created by edwin on 4/25/14.
 *
 *  TABLE_ALL_ITEMS
 * ----------*-----------*-------*-------------*-------*--------*--------*----------*--------------*-------------*---------------*
 * | item_id | item_name | price | category_id | likes | shares | active | calories | last_updated | description | daily_special |
 * +---------+-----------+-------+-------------+-------+--------+--------+----------+--------------+-------------+---------------+
 * |         |           |       |             |       |        |        |          |              |             |               |
 *
 *
 */
public class ItemDatabaseHandler extends SQLiteOpenHelper {
    //All static variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ItemsManager";
    private static final String TABLE_ALL_ITEMS = "items";

    //TABLE_ALL_ITEMS column names
    private static final String KEY_ITEM_ID = "item_id";
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_LIKES = "likes";
    private static final String KEY_SHARES = "shares";
    private static final String KEY_ACTIVE = "active";
    private static final String KEY_CALORIES = "calories";
    private static final String KEY_LAST_UPDATED= "last_updated";   //last updated! used for syncing
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DAILY_SPECIAL = "daily_special";
    //Constructor
    public ItemDatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_ALL_ITEMS_TABLE = "CREATE TABLE " + TABLE_ALL_ITEMS + "("
                + KEY_ITEM_ID + " INTEGER PRIMARY KEY,"
                + KEY_ITEM_NAME + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_CATEGORY_ID + " INTEGER,"
                + KEY_LIKES + " INTEGER,"
                + KEY_SHARES + " INTEGER,"
                + KEY_ACTIVE + " INTEGER,"
                + KEY_CALORIES + " INTEGER,"
                + KEY_LAST_UPDATED + " TEXT,"           //might change to datetime
                + KEY_DESCRIPTION + " TEXT," + KEY_PRICE + " REAL,"
                + KEY_DAILY_SPECIAL + " INTEGER,"
                +")";

        db.execSQL(CREATE_ALL_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALL_ITEMS);

        //re-create table
        onCreate(db);

    }


    //CRUD functionality:
    /* Create - addItem
     * PRECONDITION: item is created with no ID
     * POSTCONDITION: item is added to the database
     * TODO: null/empty checks
     */
    public void addItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME,item.getItemName());
        values.put(KEY_PRICE,item.getPrice());
        values.put(KEY_CATEGORY_ID,item.getCategoryID());
        values.put(KEY_LIKES,item.getLikes());
        values.put(KEY_SHARES,item.getShares());
        values.put(KEY_ACTIVE,item.isActive());
        values.put(KEY_CALORIES,item.getCalories());
        values.put(KEY_LAST_UPDATED,item.getLastUpdated());
        values.put(KEY_DESCRIPTION,item.getDescription());
        values.put(KEY_DAILY_SPECIAL,item.isDailySpecial());
        //ID is automatically incremented (PRIMARY KEY)

        db.insert(TABLE_ALL_ITEMS,null,values);
        //close database
        db.close();
    }

    /* Read - getItem
     * PRECONDITION: id for the target item is obtained.
     * POSTCONDITION: Item object is returned
     * TODO: null/empty checks
     */
    public Item getItem(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        //create a cursor pointing to the item identified by this "id"
        Cursor cursor = db.query(TABLE_ALL_ITEMS,new String[] {KEY_ITEM_ID,KEY_ITEM_NAME,
                KEY_PRICE, KEY_CATEGORY_ID, KEY_LIKES, KEY_SHARES, KEY_ACTIVE, KEY_CALORIES,
                KEY_LAST_UPDATED, KEY_DESCRIPTION, KEY_DAILY_SPECIAL},
                KEY_ITEM_ID + "=?", new String[] {String.valueOf(id)},null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

        Item item = new Item(
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ITEM_ID))),
                cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME)),
                Float.parseFloat(cursor.getString(cursor.getColumnIndex(KEY_PRICE))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_ID))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_LIKES))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_SHARES))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_ACTIVE))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CALORIES))),
                cursor.getString(cursor.getColumnIndex(KEY_LAST_UPDATED)),
                cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_DAILY_SPECIAL)))
        );

        //return item
        return item;
    }

    /* Update - updateItem
     * PRECONDITION: item is created
     * POSTCONDITION: database is updated
     * TODO: null/empty checks
     */
    public int updateItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME,item.getItemName());
        values.put(KEY_PRICE,item.getPrice());
        values.put(KEY_CATEGORY_ID,item.getCategoryID());
        values.put(KEY_LIKES,item.getLikes());
        values.put(KEY_SHARES,item.getShares());
        values.put(KEY_ACTIVE,item.isActive());
        values.put(KEY_CALORIES,item.getCalories());
        values.put(KEY_LAST_UPDATED,item.getLastUpdated());
        values.put(KEY_DESCRIPTION,item.getDescription());
        values.put(KEY_DAILY_SPECIAL,item.isDailySpecial());

        return db.update(TABLE_ALL_ITEMS,values,KEY_ITEM_ID + " = ?",
                new String[] {String.valueOf(item.getItemID())});
    }

    //Delete
    public void deleteItem(Item item){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_ALL_ITEMS,KEY_ITEM_ID + " =?",
                new String[] {String.valueOf(item.getItemID())});
    }



    //Added functionality:

    /*
     * getAllItems
     */
    public List<Item> getAllItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Item> itemList = new ArrayList<Item>();

        String selectQuery = "SELECT * FROM " + TABLE_ALL_ITEMS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Item item = new Item(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ITEM_ID))),
                        cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME)),
                        Float.parseFloat(cursor.getString(cursor.getColumnIndex(KEY_PRICE))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_ID))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_LIKES))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_SHARES))),
                        Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_ACTIVE))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CALORIES))),
                        cursor.getString(cursor.getColumnIndex(KEY_LAST_UPDATED)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                        Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(KEY_DAILY_SPECIAL)))
                );
                itemList.add(item);
            }while(cursor.moveToNext());
        }

        return itemList;
    }

    /*
     * getCount
     * TODO: null/empty checks
     */
    public int getItemCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_ALL_ITEMS;
        Cursor cursor = db.rawQuery(countQuery,null);
        cursor.close();

        return cursor.getCount();
    }

    /*
     * getDateTime
     * Returns a formatted String in datetime format
     */
    static private String getDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
