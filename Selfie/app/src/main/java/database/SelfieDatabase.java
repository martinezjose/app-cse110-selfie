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
 * TABLE_RECOMMENDATIONS = "recommendations" -- recommendations table (foreign table)
 *
 *  TABLE_ALL_ITEMS
 * ----------*-----------*-------*-------------*-------*--------*----------*-------------*-------------*---------------*------------*
 * | item_id | item_name | price | category_id | likes | active | calories |last_updated | description | daily_special | thumbnail  |
 * +---------+-----------+-------+-------------+-------+--------+----------+-------------+-----------------------------+------------+
 * |         |           |       |             |       |        |          |             |             |               |            |
 *
 * TABLE_CATEGORIES
 * --------------*---------------*--------------*
 * | category_id | category_name | last_updated |
 * +-------------+---------------+--------------+
 * |             |               |              |
 *
 * TABLE_RECOMMENDATIONS
 * --------------------*------------------*---------------*
 * | recommendation_id | recommended_item | item_track_id | <--- parent_key
 * +-------------------+------------------+---------------+
 * |                   |                  |               |
 *
 * TABLE_IMAGE_PATHS
 * -----------*------------*---------------*
 * | image_id | image_path | item_track_id | <--- parent_key
 * +----------+------------+---------------+
 * |          |            |               |
 */
public class SelfieDatabase extends SQLiteOpenHelper {
    //All static variables
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "selfieDB";
    public static final String TABLE_ALL_ITEMS = "items";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_RECOMMENDATIONS = "recommendations";
    public static final String TABLE_IMAGE_PATHS = "image_paths";

    //TABLE_ALL_ITEMS column names
    public static final String KEY_ITEM_ID = "item_id";
    public static final String KEY_ITEM_NAME = "item_name";
    public static final String KEY_PRICE = "price";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_ACTIVE = "active";
    public static final String KEY_CALORIES = "calories";
    public static final String KEY_LAST_UPDATED= "last_updated";   //last updated used for syncing
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DAILY_SPECIAL = "daily_special";
    public static final String KEY_THUMBNAIL = "thumbnail";

    //TABLE_CATEGORIES column names
    //public static final String KEY_CATEGORY_ID = "category_id"; //same as KEY_CATEGORY_ID above
    public static final String KEY_CATEGORY_NAME = "category_name";

    //TABLE_RECOMMENDATIONS
    public static final String KEY_RECOMMENDATION_ID = "recommendation_id";
    public static final String KEY_RECOMMENDED_ITEM = "recommended_item";
    //public static final String KEY_ITEM_TRACK_ID = "item_track_id";

    //TABLE_IMAGE_PATHS
    public static final String KEY_IMAGE_ID = "image_id";
    public static final String KEY_IMAGE_PATH = "image_path";
    public static final String KEY_ITEM_TRACK_ID = "item_track_id"; //<--- PARENT KEY

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
                + KEY_LAST_UPDATED + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DAILY_SPECIAL + " INTEGER,"
                + KEY_THUMBNAIL + " TEXT"
                + ");";

        //TABLE_CATEGORIES
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + KEY_CATEGORY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CATEGORY_NAME + " TEXT"
                + ");";

        //TABLE_RECOMMENDATIONS
        String CREATE_RECOMMENDATIONS_TABLE = "CREATE TABLE " + TABLE_RECOMMENDATIONS + "("
                + KEY_RECOMMENDATION_ID + " INTEGER PRIMARY KEY,"
                + KEY_RECOMMENDED_ITEM + " INTEGER,"
                + KEY_ITEM_TRACK_ID + " INTEGER,"
                + "FOREIGN KEY("+KEY_ITEM_TRACK_ID+") REFERENCES "+TABLE_ALL_ITEMS+"("+KEY_ITEM_ID+")"
                + ");";

        //TABLE_IMAGE_PATHS
        String CREATE_IMAGE_PATHS_TABLE = "CREATE TABLE " + TABLE_IMAGE_PATHS + "("
                + KEY_IMAGE_ID + " INTEGER PRIMARY KEY,"
                + KEY_IMAGE_PATH + " TEXT NOT NULL,"
                + KEY_ITEM_TRACK_ID + " INTEGER,"
                + "FOREIGN KEY("+KEY_ITEM_TRACK_ID+") REFERENCES "+TABLE_ALL_ITEMS+"("+KEY_ITEM_ID+")"
                + ");";

        db.execSQL(CREATE_ALL_ITEMS_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_RECOMMENDATIONS_TABLE);
        db.execSQL(CREATE_IMAGE_PATHS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALL_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECOMMENDATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE_PATHS);
        //re-create table
        onCreate(db);

    }




}
