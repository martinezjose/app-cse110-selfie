package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import classes.SmallItem;

/**
 * Created by jmember on 5/11/14.
 */
public class RecommendationDataSource {
    private SelfieDatabase myDB;        //instance of the SelfieDatabase class
    private SQLiteDatabase db;          //instance of the SQLiteDatabase class, from which SelfieD-
                                        //atabase extends.
    private ItemDataSource itemSource;  //itemSource required to pull Items from RecommendationIDs

    //all columns to be retrieved from the table
    private String [] allColumns = {SelfieDatabase.KEY_RECOMMENDATION_ID,
            SelfieDatabase.KEY_RECOMMENDED_ITEM,SelfieDatabase.KEY_ITEM_TRACK_ID};

    //CONSTRUCTOR
    public RecommendationDataSource(Context context, ItemDataSource _itemSource){
        //instantiate myDB to gain access to database
        myDB = new SelfieDatabase(context);
        //instantiate itemSource
        itemSource = _itemSource;
    }

    //open_read()
    //open the database for reading
    public void open_read(){ db = myDB.getReadableDatabase(); }

    //open_write()
    //open the database for writing. set the foreign key constraints!
    public void open_write() {
        db = myDB.getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(true);   //enable foreign key constraints!
    }

    //close()
    public void close() { db.close(); }

    /******************************************* CRUD *********************************************/

    /* OVERLOADED METHOD --- accepts an array of RecommendedItemIDs
    *public void addRecommendation(int ItemID, int [] RecommendedItemIDs) throws InsertToDatabaseException
    * Paremeters: int ItemID, int [] RecommendedItemIDs
    * Description: inserts all elements in RecommendedItemID to the database, linked to 'ItemID' (foreign key)
    * PRECONDITION: ItemID and all elements in RecommendedItemID are obtained legally (through getItem or equivalent)
    * POSTCONDITION: all elements in RecommendedItemID is inserted into the Recommendations table
    * Returns: nothing
    * Status: untested
    */
    public void addRecommendation(int ItemID, int [] RecommendedItemIDs) throws InsertToDatabaseException{
        //open the database for writing (sets foreign key constraints!)
        open_write();

        //iterate over all elements in RecommendedItemIDs
        for(int element:RecommendedItemIDs) {
            //add RecommendedItem and ItemID to ContentValues
            ContentValues values = new ContentValues();
            values.put(SelfieDatabase.KEY_RECOMMENDED_ITEM, element);
            values.put(SelfieDatabase.KEY_ITEM_TRACK_ID, ItemID);

            //insert to database
            long ReturnValue = db.insert(SelfieDatabase.TABLE_RECOMMENDATIONS, null, values);

            //if error adding, throw exception
            if (ReturnValue == -1)
                throw new InsertToDatabaseException("Inserting recommendation <" + element +
                        "> to table " + SelfieDatabase.TABLE_RECOMMENDATIONS + " linked to " + ItemID);
        }
        //close database
        close();
    }

    /* public int [] getRecommendations(int ItemID)
     * Parameters: int ItemID
     * Description: retrieves all recommendations for track_item_id ItemID
     * PRECONDITION: ItemID was obtained legally through an Item obtained through getItem()
     * POSTCONDITION: an array of int is returned
     * Returns: int [] of RecommendedItems
     * Status: untested
     */
    public int [] getRecommendations(int ItemID){
        //open database
        open_read();

        //query table
        Cursor cursor = db.query(SelfieDatabase.TABLE_RECOMMENDATIONS,allColumns,
        SelfieDatabase.KEY_ITEM_TRACK_ID + " = ?",new String[] {String.valueOf(ItemID)},
                null,null,null);

        //create an array of int of size cursor.getCount()
        int [] returnValue = new int[cursor.getCount()];

        //iterate over all results
        if(cursor.moveToFirst()){
            int i = 0; //counter
            do{
                returnValue[i] = cursor.getInt(cursor.getColumnIndex(SelfieDatabase.KEY_RECOMMENDED_ITEM));
            }while(cursor.moveToNext());
        }
        //close database
        close();

        //return the populated array of int
        return returnValue;
    }
    /***********************************Specific Querying******************************************/

    /* public static ArrayList<SmallItem> getRecommendedSmallItems(int ItemID)
     * parameters: int ItemID
     * Description: Returns an array list of SmallItem of recommendations retrieved from getRecommendations()
     * PRECONDITION: ItemID is obtained legally and belongs to an Item currently in TABLE_ALL_ITEMS
     * POSTCONDITION:
     * Returns: ArrayList<SmallItem>
     * Status: untested
     */
    public ArrayList<SmallItem> getRecommendedSmallItems(int ItemID) throws RetrieveFromDatabaseException {

        ArrayList<SmallItem> returnArray = new ArrayList<SmallItem>();
        int [] Recommendations = getRecommendations(ItemID);

        for(int element_id:Recommendations){
            returnArray.add(itemSource.getSmallItem(element_id));
        }

        return returnArray;
    }
}
