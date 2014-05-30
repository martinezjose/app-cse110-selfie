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
 *      boolean Active
 *      int Calories
 *      String Created
 *      String LastUpdated
 *      String Description
 *      boolean DailySpecial
 *      String [] ImagePaths
 *      String Thumbnail
 *      int [] Recommendations
 */
public class Item {
    protected int ItemID;
    protected String ItemName;
    private float Price;
    protected int CategoryID;
    private int Likes;
    private boolean Active;
    private int Calories;
    private String LastUpdated;
    private String Description;
    protected boolean DailySpecial;
    private String [] ImagePaths;
    protected String Thumbnail;
    private int [] Recommendations;


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
    public Item(int itemID, String itemName, float price, int categoryID, int likes,
                boolean active, int calories, String lastupdated, String description,
                boolean dailySpecial, String [] imagePaths, String thumbnail, int [] recommendations) {
        ItemID = itemID;
        ItemName = itemName;
        Price = price;
        CategoryID = categoryID;
        Likes = likes;
        Active = active;
        Calories = calories;
        LastUpdated = lastupdated;
        Description = description;
        DailySpecial = dailySpecial;
        ImagePaths = imagePaths;
        Thumbnail = thumbnail;
        Recommendations = recommendations;
    }

    /*
     * Setter/inserting Constructor
     * :: No ItemID -- for insertion
     * Description: Used to insert new Item to database.
     * Missing: ItemID, LastUpdated
     */
    public Item(String itemName, float price, int categoryID, int likes, boolean active,
                int calories,String description, boolean dailySpecial,
                String thumbnail) {
        ItemName = itemName;
        Price = price;
        CategoryID = categoryID;
        Likes = likes;
        Active = active;
        Calories = calories;
        LastUpdated = getDateTime();
        Description = description;
        DailySpecial = dailySpecial;
        Thumbnail = thumbnail;
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

    public String[] getImagePaths() {
        return ImagePaths;
    }

    public void setImagePaths(String[] imagePaths) {
        ImagePaths = imagePaths;
    }

    public String getThumbnail() { return Thumbnail; }

    public void setThumbnail(String thumbnail) { Thumbnail = thumbnail; }

    public int[] getRecommendations() {
        return Recommendations;
    }

    public void setRecommendations(int[] recommendations) {
        Recommendations = recommendations;
    }

    /* getDateTime()
             * Description: returns a formatted String in datetime format yyyy-MM-dd HH:mm:ss
             * PRECONDITION: none
             * POSTCONDITION: a String in the format "yyyy-MM-dd HH:mm:ss" of the current time is returned.
             * RETURNS: String in the format "yyyy-MM-dd HH:mm:ss" of the current time
             * Status: works. tested (not thoroughly)
             * Keywords: date, get date, getdate, getdatetime, current time, now
             */
    static public String getDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
