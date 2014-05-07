package classes;

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
    public String LastUpdated;
    public String Description;
    public boolean DailySpecial;

    /*
     * Constructor
     * :: All fields
     */
    public Item(int itemID, String itemName, float price, int categoryID, int likes, int shares, boolean active, int calories, String lastUpdated, String description, boolean dailySpecial) {
        ItemID = itemID;
        ItemName = itemName;
        Price = price;
        CategoryID = categoryID;
        Likes = likes;
        Shares = shares;
        Active = active;
        Calories = calories;
        LastUpdated = lastUpdated;
        Description = description;
        DailySpecial = dailySpecial;
    }

    /*
     * Constructor
     * :: No ItemID (to be incremented by SQLiteDatabase)
     */
    public Item(String itemName, float price, int categoryID, int likes, int shares, boolean active, int calories, String lastUpdated, String description, boolean dailySpecial) {
        ItemName = itemName;
        Price = price;
        CategoryID = categoryID;
        Likes = likes;
        Shares = shares;
        Active = active;
        Calories = calories;
        LastUpdated = lastUpdated;
        Description = description;
        DailySpecial = dailySpecial;
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
}
