package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SelfieDatabase -- Extension of SQLiteOpenHelper for SelfieDatabase
 * This is ONLY for the table "items"
 * Created by edwin on 4/25/14.
 *
 * BASIC INFO:
 *
 * DATABASE_NAME = "selfieDB"   -- data/data/cse110.selfie.app/databases/selfieDB
 * TABLE_ALL_ITEMS = "items"    -- basic table "items"
 * TABLE_CATEGORIES = "categories" -- categories table
 *
 *  TABLE_ALL_ITEMS
 * ----------*-----------*-------*-------------*-------*--------*----------*---------*-------------*-------------*---------------*------------*------------*
 * | item_id | item_name | price | category_id | likes | active | calories | created |last_updated | description | daily_special | image_path | thumbnail  |
 * +---------+-----------+-------+-------------+-------+--------+----------+---------+---------------------------+---------------+------------+------------+
 * |         |           |       |             |       |        |          |         |             |             |               |            |            |
 *
 * TABLE_CATEGORIES
 * --------------*---------------*
 * | category_id | category_name |
 * +-------------+---------------+
 * |             |               |
 *
 *
 */
public class SelfieDatabase extends SQLiteOpenHelper {
    //All static variables
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "selfieDB";
    public static final String TABLE_ALL_ITEMS = "items";
    public static final String TABLE_CATEGORIES = "categories";

    //TABLE_ALL_ITEMS column names
    public static final String KEY_ITEM_ID = "item_id";
    public static final String KEY_ITEM_NAME = "item_name";
    public static final String KEY_PRICE = "price";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_ACTIVE = "active";
    public static final String KEY_CALORIES = "calories";
    public static final String KEY_CREATED = "created";
    public static final String KEY_LAST_UPDATED= "last_updated";   //last updated used for syncing
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DAILY_SPECIAL = "daily_special";
    public static final String KEY_IMAGE_PATH = "image_path";
    public static final String KEY_THUMBNAIL = "thumbnail";

    //TABLE_CATEGORIES column names
    //public static final String KEY_CATEGORY_ID = "category_id"; //same as KEY_CATEGORY_ID above
    public static final String KEY_CATEGORY_NAME = "category_name";

    //Constructor
    public SelfieDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        //TABLE_ALL_ITEMS
        String CREATE_ALL_ITEMS_TABLE = "CREATE TABLE " + TABLE_ALL_ITEMS + "("
                + KEY_ITEM_ID + " INTEGER PRIMARY KEY,"
                + KEY_ITEM_NAME + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_CATEGORY_ID + " INTEGER,"
                + KEY_LIKES + " INTEGER,"
                + KEY_ACTIVE + " INTEGER,"
                + KEY_CALORIES + " INTEGER,"
                + KEY_CREATED + " TEXT,"
                + KEY_LAST_UPDATED + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DAILY_SPECIAL + " INTEGER,"
                + KEY_IMAGE_PATH + " TEXT,"
                + KEY_THUMBNAIL + " TEXT"
                +");";

        //TABLE_CATEGORIES
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + KEY_CATEGORY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CATEGORY_NAME + " TEXT"
                + ");";

        db.execSQL(CREATE_ALL_ITEMS_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALL_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);

        //re-create table
        onCreate(db);

    }




}
