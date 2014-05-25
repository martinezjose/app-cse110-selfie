package classes;

/**
 * Created by edwinmo on 5/10/14.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("CategoryID")
    @Expose
    public int CategoryID;

    @SerializedName("CategoryName")
    @Expose
    public String CategoryName;

    /*
     * Default constructor
     */
    public Category() {
    }

    /* Getter constructor
     * :: All fields -- for retrieval
     * Description: used to retrieve a category from the database.
     */
    public Category(int categoryID, String categoryName) {
        CategoryID = categoryID;
        CategoryName = categoryName;
    }

    /* Setter/insert constructor
     * :: Only CategoryName field -- for insertion
     * Description: used to insert a category to the database
     */
    public Category(String categoryName) {
        CategoryName = categoryName;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
