package classes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by edwinmo on 5/6/14.
 * Class: Item
 * Fields:
 *      int ItemID
 *      String ItemName
 *      float Price
 *      int CategoryID
 *      int Likes
 *      int Shares
 *      boolean Active
 *      int Calories
 *      Date LastUpdated --
 *      String Description
 *      boolean DailySpecial
 *      String ImagePath
 */
public class Item {
    public int ItemID;
    public String ItemName;
    public float Price;
    public int CategoryID;
    public int Likes;
    public int Shares;
    public boolean Active;
    public int Calories;
    public String Created;
    public String LastUpdated;
    public String Description;
    public boolean DailySpecial;
    public String ImagePath;


    /*
     * Default constructor
     */
    public Item() {
    }

    /*
         * Getter Constructor
         * :: All fields -- for retrieval
         * Description: Used to retrieve information from the database. All fields have been
         * populated.
         */
    public Item(int itemID, String itemName, float price, int categoryID, int likes, int shares,
                boolean active, int calories,String created, String lastupdated,String description,
                boolean dailySpecial, String imagePath) {
        ItemID = itemID;
        ItemName = itemName;
        Price = price;
        CategoryID = categoryID;
        Likes = likes;
        Shares = shares;
        Active = active;
        Calories = calories;
        Created = created;
        LastUpdated = lastupdated;
        Description = description;
        DailySpecial = dailySpecial;
        ImagePath = imagePath;
    }

    /*
     * Setter/inserting Constructor
     * :: No ItemID -- for insertion
     * Description: Used to insert new Item to database.
     * Missing: ItemID, Created, LastUpdated
     */
    public Item(String itemName, float price, int categoryID, int likes, int shares, boolean active,
                int calories,String description, boolean dailySpecial,
                String imagePath) {
        ItemName = itemName;
        Price = price;
        CategoryID = categoryID;
        Likes = likes;
        Shares = shares;
        Active = active;
        Calories = calories;
        Created = getDateTime();        //set current time
        LastUpdated = Created;          //set current time
        Description = description;
        DailySpecial = dailySpecial;
        ImagePath = imagePath;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public int getShares() {
        return Shares;
    }

    public void setShares(int shares) {
        Shares = shares;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public int getCalories() {
        return Calories;
    }

    public void setCalories(int calories) {
        Calories = calories;
    }

    public String getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        LastUpdated = lastUpdated;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isDailySpecial() {
        return DailySpecial;
    }

    public void setDailySpecial(boolean dailySpecial) {
        DailySpecial = dailySpecial;
    }

    public String getImagePath() { return ImagePath; }

    public void setImagePath(String imagePath) { ImagePath = imagePath; }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }

    /*
     * getDateTime
     * Returns a formatted String in datetime format
     */
    static private String getDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
