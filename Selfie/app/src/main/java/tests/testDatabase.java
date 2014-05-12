package tests;

import android.test.AndroidTestCase;

import database.CategoryDataSource;
import database.ItemDataSource;

/**
 * Created by edwinmo on 5/11/14.
 */
public class testDatabase extends AndroidTestCase {

    /* testCreateCategoryDAO()
     * Tests creation of a CategoryDataSource and ItemDataSource
     *
     */
    public void testCreateCategoryDAO() throws Exception{

        CategoryDataSource categorySource = new CategoryDataSource(getContext());

        //test open
        categorySource.open();

        //test close
        categorySource.close();
    }

    /* testCreateItemDAO()
     * Tests creation of a CategoryDataSource and ItemDataSource
     *
     */
    public void testCreateItemDAO() throws Exception{

        ItemDataSource itemSource = new ItemDataSource(getContext());

        //test open
        itemSource.open();

        //test close
        itemSource.close();
    }


}
