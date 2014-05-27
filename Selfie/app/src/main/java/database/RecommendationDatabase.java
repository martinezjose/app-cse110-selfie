package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jmember on 5/11/14.
 */
public class RecommendationDatabase extends SQLiteOpenHelper
{
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "selfieDB";
    public static final String TABLE_RECOMMENDATIONS = "recommendations";
    // table column names
    public static final String KEY_SRC_ITEM_ID = "src_item_id";
    public static final String KEY_TARGET_ITEM_IDS = "target_item_ids";
    public ItemDataSource myItemDB;
    // constructor
    public RecommendationDatabase(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        myItemDB = new ItemDataSource( context );
    }
    // oncreate
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        String create_recommendation_table = "CREATE TABLE " + TABLE_RECOMMENDATIONS + "("
                + KEY_SRC_ITEM_ID + "INTEGER PRIMARY KEY,"
                + KEY_TARGET_ITEM_IDS + "TEXT"
                +");";
        sqLiteDatabase.execSQL(create_recommendation_table);
    }
    // onupgrade
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}
