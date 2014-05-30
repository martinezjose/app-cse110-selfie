package tests;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.io.File;

import classes.Item;
import database.ImageDataSource;
import database.InsertToDatabaseException;
import database.ItemDataSource;

/**
 * Created by edwinmo on 5/25/14.
 */
public class testImageDataSource extends AndroidTestCase {
    private ImageDataSource imageSource;
    private ItemDataSource itemSource;
    private final String TEST_FILE_PREFIX = "test_";
    private final int MAX_RECORDS = 15;

    //test values
    private final String [] SingleImagePath = {"/res/drawable-hdpi"};
    private final String [] MultipleImagePaths = new String [] {"/res/drawable-hdpi/image1",
            "/res/drawable-hdpi/thumb1",
            "http://www.domain.com/image1z"};

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        //a separate context from the application's
        RenamingDelegatingContext context =
                new RenamingDelegatingContext(getContext(),TEST_FILE_PREFIX);

        //create an ItemDataSource on test context
        itemSource = new ItemDataSource(context);

        //create an ImageDataSource on text context
        imageSource = new ImageDataSource(context);

        //populate the database with objects first
        for(int i=0; i<MAX_RECORDS; ++i){
            Item item = testItemDataSource.startItem();
            long result = itemSource.addItem(item);
            if(result==-1)
                throw new Exception();
        }
    }

    @Override
    protected void tearDown() throws Exception{
        File database = new File(imageSource.databasePath);

        //if the database exists, delete it
        if(database.exists())
            database.delete();
    }

    /* testSingleAddImage()
     * Tests adding a single ImagePath to Item #1
     */
    public void testSingleAddImage(){

        /*********TEST VALID INSERT********/
        int ItemID = 1;
        //retrieve the item from the table
        Item retrievedItem = itemSource.getItem(ItemID);


        //test valid single ImagePaths insert
        try{
            imageSource.addImage(retrievedItem.getItemID(),SingleImagePath);
        } catch (InsertToDatabaseException e){
            //if exception is thrown, UNEXPECTED!
            fail("Failed adding valid single ImagePath");
        }

        //test valid multiple ImagePaths insert
        try{
            imageSource.addImage(retrievedItem.getItemID(),MultipleImagePaths);
        } catch (InsertToDatabaseException e){
            fail("Failed adding valid multiple ImagePaths");
        }

        /*********TEST INVALID INSERT********/
        //invalid insert: foreign key dependency is violated.

        //create an Item that is not in the database (illegal Item)
        Item illegalItem = testItemDataSource.startItem();
        illegalItem.setItemID(999); //fake ItemID

        //test invalid single ImagePath insert
        try{
            imageSource.addImage(illegalItem.getItemID(), SingleImagePath);
        } catch(InsertToDatabaseException e){
            //if exception is thrown, OK
        }

        //test invalid multiple ImagePath insert
        try{
            imageSource.addImage(illegalItem.getItemID(), MultipleImagePaths);
        } catch(InsertToDatabaseException e){
            //if exception is thrown, OK
        }
    }

    /* testGetImagePaths()
     * tests retrieving ImagePaths from the database
     */
    public void testGetImagePaths(){

        /*********TEST VALID RETRIEVE********/
        int ItemID = 1; //we will grab the very first Item

        Item retrievedItem = itemSource.getItem(ItemID);

        //insert ImagePaths to the image table first
        try {
            imageSource.addImage(retrievedItem.getItemID(), MultipleImagePaths);
        } catch (InsertToDatabaseException e){
            fail("Error adding MultipleImagePaths to database for testing in testGetImagePaths");
        }
        //now, retrieve from the table
        String [] actual = imageSource.getImage(retrievedItem.getItemID());

        //assert that actual is the same as ImagePaths
        assertTrue(actual.length != 0);
        assertEquals(MultipleImagePaths.length,actual.length);
        assertEquals(MultipleImagePaths[0],actual[0]);
        assertEquals(MultipleImagePaths[1],actual[1]);
        assertEquals(MultipleImagePaths[2],actual[2]);


        /*********TEST INVALID RETRIEVE********/
        ItemID = 3; //grab third item

        //retrieve this Item
        retrievedItem = itemSource.getItem(ItemID);
        //DON'T ADD IMAGE PATHS FOR THIS ITEM

        //attempt to retrieve ImagePaths for this Item
        actual = imageSource.getImage(retrievedItem.getItemID());

        //assert that the returned array of Strings is empty (length==0)
        assertTrue(actual.length == 0);
    }


}
