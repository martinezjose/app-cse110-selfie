package classes;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by edwinmo on 5/6/14.
 * Class: Item
 * Fields:
 *      long itemID
 *      String itemName
 *      float price
 *      long categoryID
 *      int likes
 *      boolean active
 *      int calories
 *      String created
 *      String lastUpdated
 *      String description
 *      boolean dailySpecial
 *      String [] imagePath
 *      String thumbnail
 *      int [] recommendations
 */
public class Item {
    @SerializedName("Created")
    @Expose
    private String created;
    @SerializedName("Calories")
    @Expose
    private Integer calories;
    @SerializedName("ItemName")
    @Expose
    protected String itemName;
    @SerializedName("ItemID")
    @Expose
    protected long itemID;
    @SerializedName("ImagePath")
    @Expose
    private String[] imagePath;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Price")
    @Expose
    private float price;
    @SerializedName("LastUpdated")
    @Expose
    private String lastUpdated;
    @SerializedName("DailySpecial")
    @Expose
    protected Boolean dailySpecial;
    @SerializedName("Likes")
    @Expose
    private Integer likes;
    @SerializedName("Active")
    @Expose
    private Boolean active;
    @SerializedName("CategoryID")
    @Expose
    protected long categoryID;

    protected String thumbnail;

    private long [] recommendations;


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
     public Item(long ItemID, String ItemName, float Price, long CategoryID, int Likes, boolean Active,
     int Calories, String LastUpdated, String Description, boolean DailySpecial,
     String [] ImagePath, String Thumbnail, long [] Recommendations) {
         itemID = ItemID;
         itemName = ItemName;
         price = Price;
         categoryID = CategoryID;
         likes = Likes;
         active = Active;
         calories = Calories;
         created = getDateTime();        //set current time
         lastUpdated = created;          //set current time
         description = Description;
         dailySpecial = DailySpecial;
         imagePath = ImagePath;
         thumbnail = Thumbnail;
         recommendations = Recommendations;
    }

    /*
     * Setter/inserting Constructor
     * :: No ItemID -- for insertion
     * Description: Used to insert new Item to database.
     * Missing: ItemID, LastUpdated
     */
    public Item(String ItemName, float Price, long CategoryID, int Likes, boolean Active,
                int Calories, String Description, boolean DailySpecial,
                String [] ImagePath, String Thumbnail) {
        itemName = ItemName;
        price = Price;
        categoryID = CategoryID;
        likes = Likes;
        active = Active;
        calories = Calories;
        created = getDateTime();        //set current time
        lastUpdated = created;          //set current time
        description = Description;
        dailySpecial = DailySpecial;
        imagePath = ImagePath;
        thumbnail = Thumbnail;
    }


    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreated() {
        return this.created;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public String[] getImagePath() {
        return imagePath;
    }

    public void setImagePath(String[] imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    public Boolean isDailySpecial() {
        return dailySpecial;
    }

    public void setDailySpecial(Boolean dailySpecial) {
        this.dailySpecial = dailySpecial;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail()
    {
        return this.thumbnail;
    }

    public long[] getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(long[] Recommendations) {
        recommendations = recommendations;
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
