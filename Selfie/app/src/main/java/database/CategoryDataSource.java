package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import classes.Category;

/**
 * Created by edwinmo on 5/10/14.
 */
public class CategoryDataSource {
    private SelfieDatabase myDB;
    private SQLiteDatabase db;

    private String [] allColumns ={SelfieDatabase.KEY_CATEGORY_ID,SelfieDatabase.KEY_CATEGORY_NAME};

    //CONSTRUCTOR
    public CategoryDataSource(Context context) {myDB = new SelfieDatabase(context);}

    //open()
    public void open(){db = myDB.getWritableDatabase();}

    //close()
    public void close(){ myDB.close(); }

    /******************************************* CRUD *********************************************/

    /* addCategory() -- Create
     * Description: adds a category to the database
     * PRECONDITION: category is created with no ID (through setter/insert constructor)
     * POSTCONDITION: category is added to the database
     * Returns: long CategoryID
     * Status:
     * Keywords: create, add, new, add category
     */
    public long addCategory(Category category){
        db = myDB.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SelfieDatabase.KEY_CATEGORY_NAME,category.getCategoryName());

        long ReturnValue = db.insert(SelfieDatabase.TABLE_CATEGORIES,null,values);

        //close database
        db.close();

        return ReturnValue;
    }

    /* getCategory() -- Read
    * Description: reads from the database; returns a Category.
    * PRECONDITION: id for the target item is provided (somehow).
    * POSTCONDITION: Category object is returned
    * Returns: found category in the database is returned.
    * Status:
    * Keywords: read, getCategory, get category, retrieve
    */
    public Category getCategory(int id) {

        //get a readable database
        db = myDB.getReadableDatabase();

        //create a cursor pointing to the category identified by this "id"
        Cursor cursor = db.query(SelfieDatabase.TABLE_CATEGORIES, allColumns,
                SelfieDatabase.KEY_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        //return category
        return cursorToCategory(cursor);
    }

    /* updateCategory() -- Update
     * Description: updates the category in the database.
     * PRECONDITION:  The following conditions apply:
     *          (1) category must have been obtained through getCategory() method
     *          (2) category must have been modified outside this method.
     *          (3) CategoryID must not be modified
     * POSTCONDITION: category in database is updated.
     * Returns: number of rows affected
     * Status:
     * Keywords: update, updating, category
     */
    public int updateCategory(Category category){

        //get writable database
        db = myDB.getWritableDatabase();

        //create ContentValues from item
        ContentValues values = categoryToContentValues(category);

        return db.update(SelfieDatabase.TABLE_CATEGORIES,values,SelfieDatabase.KEY_CATEGORY_ID+"=?",
                new String[] {String.valueOf(category.getCategoryID())});
    }

    /* deleteCategory() -- Delete
     * Description: Deletes 'category' from the database.
     * PRECONDITION: category was obtained through getCategory()--guarantees that item has an ItemID
     * POSTCONDITION: category has been deleted from the database.
     * Returns: None
     * STATUS: TODO what happens to Items that are in this deleted category?
     * Keywords: delete, delete category
     */
    public void deleteCategory(Category category){

        //get writable database
        db = myDB.getWritableDatabase();

        //may want to return # of rows affected...
        db.delete(SelfieDatabase.TABLE_CATEGORIES,SelfieDatabase.KEY_CATEGORY_ID + " =?",
                new String[] {String.valueOf(category.getCategoryID())});
    }


    /*********************************** EXTRA FUNCTIONALITY **************************************/


    /* getAllCategories()
     * Description: extra functionality. Returns a List of all rows (Category) in the database.
     * PRECONDITION: myDB exists
     * POSTCONDITION: a List of Categories is returned
     * Returns: List of Categories
     * Status:
     * Keywords: get all categories, retrieve all
     */
    public List<Category> getAllCategory(){

        //get readable database
        db = myDB.getReadableDatabase();

        //List to store all rows of Items
        List<Category> categoryList = new ArrayList<Category>();

        //select all rows
        String selectQuery = "SELECT * FROM " + SelfieDatabase.TABLE_CATEGORIES;

        Cursor cursor = db.rawQuery(selectQuery, null);

        //if query was successful
        if(cursor.moveToFirst()){
            do{
                Category category = cursorToCategory(cursor);
                categoryList.add(category);
            }while(cursor.moveToNext());
        }

        return categoryList;
    }

    /* getCount()
     * Description: extra functionality. Returns the number of entries in the database.
     * PRECONDITION: myDB exists
     * POSTCONDITION: the number of entries in the database is returned.
     * Returns: number of entries in the database
     * Status: not tested
     * Keywords: count, size, number
     */
    public int getCount(){

        //get readable database
        db = myDB.getReadableDatabase();

        //select all rows in TABLE_ALL_ITEMS
        String countQuery = "SELECT * FROM " + SelfieDatabase.TABLE_CATEGORIES;

        //query for countQuery, returns a cursor to query result
        Cursor cursor = db.rawQuery(countQuery,null);
        cursor.close();

        return cursor.getCount();
    }


    /************************************** HELPER METHODS ****************************************/

    /* cursorToItem()
     * Description: returns a category object pointed at by the cursor in the database.
     * PRECONDITION: cursor points to a valid entry in the database.
     * POSTCONDITION: a Category object is populated and returned
     * Returns: Category
     * Status:
     * Keywords: helper, helper function, cursor to Category, cursorToCategory
     */
    private Category cursorToCategory(Cursor cursor){

        //Getter constructor
        return new Category(
                cursor.getInt(cursor.getColumnIndex(SelfieDatabase.KEY_CATEGORY_ID)),
                cursor.getString(cursor.getColumnIndex(SelfieDatabase.KEY_CATEGORY_NAME))
        );
    }

    /* categoryToContentValues()
     * Description: helper function. Returns a ContentValues populated by category's fields.
     * PRECONDITION: category exists.
     * POSTCONDITION: a ContentValues is returned
     * Returns: ContentValues (populated)
     * Status:
     * Keywords: content values, contentvalues, categoryToContentValues
     */
    private ContentValues categoryToContentValues(Category category){
        ContentValues values = new ContentValues();

        values.put(SelfieDatabase.KEY_CATEGORY_ID,category.getCategoryID());
        values.put(SelfieDatabase.KEY_CATEGORY_NAME,category.getCategoryName());

        return values;
    }
}

