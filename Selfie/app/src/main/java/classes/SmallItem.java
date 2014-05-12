package classes;

/**
 * class SmallItem extends Item
 * Description: Will never be instantiated by itself for insertion purposes! This is only a helper
 * class to retrieve minimum information from the database.
 * Created by edwinmo on 5/11/14.
 */
public class SmallItem extends Item {

    /*
     * Default Constructor
     */
    public SmallItem() {}

    /* Getter Constructor
     * Description: used to retrieve data from the database.
     */
    public SmallItem(int itemid, String name, boolean special, String thumbnail){
        this.ItemID = itemid;
        this.ItemName = name;
        this.DailySpecial = special;
        this.Thumbnail = thumbnail;
    }


}
