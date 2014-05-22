package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import classes.Item;

/**
 * Created by jmember on 5/11/14.
 */
public class RecommendationDataSource {
    private RecommendationDatabase myDB;
    private SQLiteDatabase db;
    private String[] allCols = {RecommendationDatabase.KEY_SRC_ITEM_ID,
            RecommendationDatabase.KEY_TARGET_ITEM_IDS};
    // constructor
    public RecommendationDataSource( Context context ) { myDB = new RecommendationDatabase(context); }
    // add a recommendation to the provided item
    public void addRecommendation(Item src, Item dest){
        // insert to the recommendation database table with provided id
        db = myDB.getWritableDatabase();
        // get the ContentValues format
        ContentValues tmp = new ContentValues();
        tmp.put( RecommendationDatabase.KEY_TARGET_ITEM_IDS, src.getItemID() );
        // if no recommendations have previously been added, directly add to the database*
        if( getRecommendationsString( src.getItemID() ).isEmpty() )
        {
            // add recommendation to the the beginning of list
            tmp.put( RecommendationDatabase.KEY_TARGET_ITEM_IDS, dest.getItemID() );
            // insert the recommendation to database
            db.insert(RecommendationDatabase.TABLE_RECOMMENDATIONS, null, tmp);
        }
        // otherwise, do an update
        else
        {
            // add recommendation to previous list
            tmp.put( RecommendationDatabase.KEY_TARGET_ITEM_IDS,
                    getRecommendationsString( src.getItemID() ) + ' ' + dest.getItemID() );
            db.update(RecommendationDatabase.TABLE_RECOMMENDATIONS, tmp,
                    RecommendationDatabase.KEY_SRC_ITEM_ID + " = ?",
                    new String[]{String.valueOf(src.getItemID( ))});
        }
        // close the db
        db.close();
    }
    // method to get the current recommendations of an item
    private String getRecommendationsString( int id )
    {
        // get a readable db
        db = myDB.getReadableDatabase();
        // create a cursor pointing to item with given ID
        Cursor cursor = db.query( RecommendationDatabase.TABLE_RECOMMENDATIONS, allCols,
                RecommendationDatabase.KEY_SRC_ITEM_ID + " = ?",
                new String[] {String.valueOf(id)}, null, null, null );
        if( cursor != null )
        {
            cursor.moveToFirst();
        }
        // return the string of recommendations*
        return cursor.getString( cursor.getColumnIndex(RecommendationDatabase.KEY_TARGET_ITEM_IDS));
    }
    // get a list of items that are recommended to go with src item
    public List<Item> getRecommendations(int itemID)
    {
        // for storing retreived items
        List<Item> itemList = new ArrayList<Item>();
        String idList, ids[];
        // to allow access to database
        db = myDB.getReadableDatabase();
        // get cursor pointing to the item with provided id
        Cursor myCursor = db.query(RecommendationDatabase.TABLE_RECOMMENDATIONS,allCols,
                RecommendationDatabase.KEY_SRC_ITEM_ID + "=?",new String[] {String.valueOf(itemID)},
                null,null,null);
        if(myCursor != null) {
            myCursor.moveToFirst();
        }
        idList =
                myCursor.getString(myCursor.getColumnIndex(RecommendationDatabase.KEY_TARGET_ITEM_IDS));
        ids = idList.split("\\s");
        for( int i = 0; i < ids.length; i++ )
        {
            // problem*********
            ItemDataSource a = new ItemDataSource();
            itemList.add(a.getItem(Integer.parseInt(ids[i])));
        }
        return itemList;
    }
}
