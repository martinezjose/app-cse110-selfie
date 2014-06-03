package tests;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import classes.Item;
import database.InsertToDatabaseException;
import database.ItemDataSource;
import database.RecommendationDataSource;
import database.SelfieDatabase;

/**
 * Description: Tests RecommendationDataSource functionality
 * Dependencies: ItemDataSource must work properly
 *
 *
 * Created by edwinmo on 5/31/14.
 */
public class TestRecommendationDataSource extends AndroidTestCase {

    private ItemDataSource itemSource;
    private RecommendationDataSource recommendationSource;

    private final long[] SingleRecommendation = {1};
    private final long[] MultipleRecommendations = {1, 2, 3, 4};
    private final String TEST_FILE_PREFIX = "test_";
    private final int MAX_RECORDS = 15;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //a separate context from the application's
        RenamingDelegatingContext context =
                new RenamingDelegatingContext(getContext(), TEST_FILE_PREFIX);

        //create an ItemDataSource on test context
        itemSource = new ItemDataSource(context);

        //create a RecommendationDataSource (takes context and itemSource)
        recommendationSource = new RecommendationDataSource(context, itemSource);

        //populate the database with objects first
        //
        Item[] itemsList = new Item[MAX_RECORDS];
        for (int i = 0; i < MAX_RECORDS; ++i) {
            itemsList[i] = TestItemDataSource.startItem();
        }
        //add all items
        itemSource.addItem(itemsList);
    }

    @Override
    protected void tearDown() throws Exception {
        File database = new File(itemSource.databasePath);

        //if the database exists, delete it
        if (database.exists())
            database.delete();
    }


    /* testAddSingleRecommendation
     * tests adding a single recommendation in both valid and invalid foreign constraints
     */
    public void testAddSingleRecommendation(){

        /*************INSERTING SINGLE RECOMMENDATION VALID*************/
        //instantiate a Random object
        Random randomizer = new Random();

        //get a random ItemID between 1 and MAX_RECORDS
        long ItemID = randomizer.nextInt(MAX_RECORDS + 1); //[MAX_RECORDS) + 1

        try{
            recommendationSource.addRecommendation(ItemID,SingleRecommendation);
        } catch (InsertToDatabaseException e){
            //if exception is thrown, ERROR! this should succeed
            fail("Failed adding SingleRecommendation to table " + SelfieDatabase.TABLE_RECOMMENDATIONS);
        }

        /*************INSERTING SINGLE RECOMMENDATION INVALID*************/
        ItemID = 999;   //arbitrarily selected ItemID that definitely does not exist in database

        try{
            recommendationSource.addRecommendation(ItemID,SingleRecommendation);
        } catch (InsertToDatabaseException e){
            //if exception is thrown, OKAY! This should fail
        }
    }

    /* testAddMultipleRecommendations
     * tests adding multiple recommendations in both valid and invalid foreign constraints
     */
    public void testAddMultipleRecommendations(){
        /*************INSERTING SINGLE RECOMMENDATION VALID*************/
        //instantiate a Random object
        Random randomizer = new Random();

        //get a random ItemID between 1 and MAX_RECORDS (including MAX_RECORDS)
        int ItemID = randomizer.nextInt(MAX_RECORDS + 1); //[MAX_RECORDS) + 1

        try{
            recommendationSource.addRecommendation(ItemID,MultipleRecommendations);
        } catch (InsertToDatabaseException e){
            //if exception is thrown, ERROR! This should succeed
            fail("Failed adding MultipleRecommendations to table " + SelfieDatabase.TABLE_RECOMMENDATIONS);
        }

        /*************INSERTING SINGLE RECOMMENDATION INVALID*************/
        ItemID = 999;   //arbitrarily selected ItemID that definitely does not exist in database

        try{
            recommendationSource.addRecommendation(ItemID,MultipleRecommendations);
        } catch (InsertToDatabaseException e){
            //if exception is thrown, OKAY! This should fail
        }
    }
}
