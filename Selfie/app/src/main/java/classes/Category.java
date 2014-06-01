package classes;

/**
 * Created by edwinmo on 5/10/14.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Category {

    @SerializedName("Created")
    @Expose
    private String created;
    @SerializedName("CategoryID")
    @Expose
    private Long categoryID;
    @SerializedName("LastUpdated")
    @Expose
    private String lastUpdated;
    @SerializedName("CategoryName")
    @Expose
    private String categoryName;

    public Category(long id, String name)
    {
        this.categoryID = id;
        this.categoryName = name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}