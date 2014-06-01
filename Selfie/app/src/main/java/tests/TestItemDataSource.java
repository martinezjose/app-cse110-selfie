package tests;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import classes.Item;
import classes.SmallItem;
import database.InsertToDatabaseException;
import database.ItemDataSource;
import database.RetrieveFromDatabaseException;
import database.SelfieDatabase;

/**
 * Created by edwinmo on 5/11/14.
 */
public class TestItemDataSource extends AndroidTestCase{

    private ItemDataSource itemSource;
    private final String TEST_FILE_PREFIX = "test_";
    private final int MAX_RECORDS = 15;

    /* setUp method @Override
     * setups variables/properties
     */
    @Override
    protected void setUp() throws Exception{
        super.setUp();
        //use this context to use a separate, temporary database!
        //otherwise use getContext()
        RenamingDelegatingContext context =
                new RenamingDelegatingContext(getContext(),TEST_FILE_PREFIX);


        //create an ItemDataSource on test context/db
        itemSource = new ItemDataSource(context);

        //populate the database
        ArrayList<Item> itemsList = new ArrayList<Item>();
        for(int i =0; i<MAX_RECORDS; ++i){
            itemsList.add(startItem());
        }
        //add all items
        itemSource.addItem(itemsList);
    }


    @Override
    protected void tearDown() throws Exception{
        File database = new File(itemSource.databasePath);

        //if the database exists, delete it
        if(database.exists())
            database.delete();
    }



    /************************************** TESTS *************************************************/

    /* testAddItem()
     * Tests the insertion of an Item into the Item table
     */
    public void testAddItem() {

        ArrayList<Item> itemsList = new ArrayList<Item>();
        Item item = TestItemDataSource.startItem();
        itemsList.add(item);
        try{
            itemSource.addItem(itemsList);
        } catch (InsertToDatabaseException e){
            //if exception is thrown, fail test...
            fail("Failed inserting <"+item.getItemName()+"> to table "+ SelfieDatabase.TABLE_ALL_ITEMS);
        }
    }

    /* testGetItem()
     * Tests the retrieval of an Item from the Item table
     */
    public void testGetItem() throws Exception {

        long ItemID = 1;

        //retrieve from table
        Item retrievedItem = null;

        try{
            retrievedItem = itemSource.getItem(ItemID);
        } catch (RetrieveFromDatabaseException e){
            //if exception is thrown while retrieving...
            fail("Failed retrieving <"+ItemID+"> from table " + SelfieDatabase.TABLE_ALL_ITEMS);
        }
    }

    /* testGetSmallItemFromCategory()
     * tests the method getSmallItemFromCategory()
     */
    public void testGetSmallItemFromCategory() throws Exception{
        long CategoryID = 1;
        ArrayList<SmallItem> list = itemSource.getSmallItemFromCategory(CategoryID);
        for(SmallItem item : list){
            //LogCat
            Log.d("testGetSmallItemFromCategory()",item.getItemID() + " " + item.getItemName() +
                " " + item.getCategoryID() + " " + item.isDailySpecial() + " " +
                item.getThumbnail());
            //item is null or item ID <= 0, error!
            if(item == null || item.getItemID()<=0 || item.getCategoryID()!=CategoryID)
                throw new Exception();
        }
    }

    /* testGetCount()
     * tests the method getCount
     */
    public void testGetCount() {
        int count = itemSource.getCount();
        assertEquals(MAX_RECORDS,count);
    }

    /************************************** HELPER METHODS ****************************************/

    /* startItem
     * Instantiates an object of the Item class for testing purposes
     * The Item object is instantiated from a predefined set of possible
     * fields.
     * TODO make private and not static after we actually have a database (this method should ONLY
     * TODO be used in this package for testing)
     */
    public static Item startItem(){
        int LikesLimit = 100;
        int PriceLimit = 75;
        int CategoryIDLimit = 4;        //NEED TO TAKE OUT MAGIC NUMBERS
        int CaloriesLimit = 5000;
        //array of Strings to choose from
        String [] EntreeNames = {"Garlic & Butter Lobster",
                           "Nachos with Chili",
                           "Chicken Fresco",
                           "Smoky Mountain Chicken",
                           "Chicken Bella",
                           "Grilled Chicken Salad",
                           "Garden Bar & Soup",
                           "New Orleans Seafood",
                           "Grilled Salmon",
                           "Louisiana Fried Shrimp",
                           "Herb-Crusted Tilapia",
                           "Jumbo Skewered Shrimp",
                           "Shellfish Trio",
                           "BBQ Wings",
                           "Queso & Chips"};
        String [] EntreeDescriptions = {"Nulla facilisi. Aliquam nisl erat, tincidunt at lacus a, " +
                "elementum ultricies turpis. Mauris eu congue erat, vel commodo lorem. Sed nec arcu" +
                " cursus, consequat felis sed, aliquam nibh.",
                                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                                              "tiam iaculis velit a augue interdum, sed bibendum " +
                                              "justo commodo. ",
                                        "Nam imperdiet mauris quis tristique euismod. Vestibulum " +
                                                "nec sapien at elit malesuada congue."};
        Random myRandom = new Random();

        int randomNameIndex = myRandom.nextInt(EntreeNames.length-1);
        int randomDescriptionIndex = myRandom.nextInt(EntreeDescriptions.length-1);

        Item myItem = new Item();
        myItem.setItemID(myRandom.nextLong());
        myItem.setItemName(EntreeNames[randomNameIndex]);
        myItem.setPrice(myRandom.nextFloat()+myRandom.nextInt(PriceLimit));
        myItem.setCategoryID(myRandom.nextInt(CategoryIDLimit) + 1);    //+1 to always avoid category 0
        myItem.setLikes(myRandom.nextInt(LikesLimit));
        myItem.setActive(myRandom.nextBoolean());
        myItem.setCalories(myRandom.nextInt(CaloriesLimit));
        myItem.setLastUpdated(myItem.getDateTime());
        myItem.setDescription(EntreeDescriptions[randomDescriptionIndex]);
        myItem.setDailySpecial(myRandom.nextBoolean());
        myItem.setThumbnail("/res/drawables/thumb_image.jpeg");
        return myItem;
    }


}
