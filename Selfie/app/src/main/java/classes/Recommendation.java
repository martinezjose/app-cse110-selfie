package classes;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by jmartinez on 5/6/14.
 * Class: Recommendation
 * Fields:
 *      long itemID
 *      long recommendedItemID
 *      String created
 *      String lastUpdated
 */
public class Recommendation {
    @SerializedName("Created")
    @Expose
    private String created;
    @SerializedName("RecommendedItemID")
    @Expose
    private long recommendedItemID;
    @SerializedName("ItemID")
    @Expose
    protected long itemID;
    @SerializedName("LastUpdated")
    @Expose
    private String lastUpdated;

    /*
     * Default constructor
     */
    public Recommendation() {
    }

    /*
     * Getter Constructor
     * :: All fields -- for retrieval
     * Description: Used to retrieve information from the database. All fields have been
     * populated.
     */
    public Recommendation(long ItemID, long RecommendedItemID, String Created,String LastUpdated) {
        itemID = ItemID;
        recommendedItemID = RecommendedItemID;
        created = Created;        //set current time
        lastUpdated = LastUpdated;          //set current time
    }


    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreated() {
        return this.created;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }
    public long getRecommendedItemID() {
        return recommendedItemID;
    }

    public void setRecommendedItemID(long itemID) {
        this.itemID = itemID;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
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
